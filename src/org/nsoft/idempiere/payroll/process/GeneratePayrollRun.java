package org.nsoft.idempiere.payroll.process;

import org.compiere.process.SvrProcess;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.nsoft.idempiere.payroll.calc.ProgressiveTaxCalculator;
import org.nsoft.idempiere.payroll.calc.TERCalculator;
import org.nsoft.idempiere.payroll.calc.TaxBracket;
import org.nsoft.idempiere.payroll.calc.WageCapUtil;
import org.nsoft.idempiere.payroll.model.bpjs.MPayrollBPJSRate;
import org.nsoft.idempiere.payroll.model.bpjs.MPayrollEmployeeProgram;
import org.nsoft.idempiere.payroll.model.tax.MPayrollEmployeeTaxProfile;
import org.nsoft.idempiere.payroll.model.tax.MPayrollPTKPRate;
import org.nsoft.idempiere.payroll.model.tax.MPayrollTaxRate;
import org.nsoft.idempiere.payroll.model.component.MPayrollComponentInput;
import org.nsoft.idempiere.payroll.model.run.MPayrollRunLine;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * AD_Process: X_GeneratePayrollRun
 * Parameter: X_Payroll_Period_ID (mandatory), RunType (mandatory,
 *            default 'REGULAR' — REGULAR/BONUS/THR/OFF_CYCLE)
 *
 * Satu proses = satu transaksi DB penuh. Gagal di tengah = rollback
 * total (tidak ada kondisi "5 dari 10 employee sudah ke-commit").
 *
 * STRUKTUR GAJI: KOMPONEN. GrossIncome TIDAK diambil dari satu kolom
 * tetap, tapi dihitung dari X_Payroll_ComponentInput (input HR sebelum
 * run) yang di-snapshot ke X_Payroll_RunLineComponent.
 *
 * TIGA MAKNA INCOME YANG BERBEDA (lihat komentar di masing2 method):
 *   - CashGrossIncome    → basis take-home pay (NetIncome)
 *   - TaxableGrossIncome → basis lookup TER/Progresif (termasuk natura BPJS)
 *   - BPJSBaseIncome     → basis hitung iuran BPJS (sebelum capping)
 *
 * MULTI-RUN PER PERIODE: PPh21 dihitung KUMULATIF terhadap run lain
 * (RunType berbeda, mis. BONUS lalu REGULAR) yang sudah Complete di
 * periode yang sama. BPJS TIDAK kumulatif — basis per run saja.
 */
public class GeneratePayrollRun extends SvrProcess {

    private static final java.util.logging.Logger log =
        java.util.logging.Logger.getLogger(GeneratePayrollRun.class.getName());

    private int p_PayrollPeriodID = 0;
    private String p_RunType = "REGULAR";

    @Override
    protected void prepare() {
        for (ProcessInfoParameter para : getParameter()) {
            String name = para.getParameterName();
            if (name.equals("X_Payroll_Period_ID")) {
                p_PayrollPeriodID = para.getParameterAsInt();
            } else if (name.equals("RunType")) {
                String val = para.getParameterAsString();
                if (val != null && !val.trim().isEmpty()) p_RunType = val.trim();
            }
        }
        if (p_PayrollPeriodID <= 0) {
            throw new IllegalArgumentException("Payroll Period wajib diisi.");
        }
    }

    @Override
    protected String doIt() throws Exception {
        String trxName = get_TrxName();
        int adClientId = Env.getAD_Client_ID(getCtx());
        int adOrgId = Env.getAD_Org_ID(getCtx());
        int adUserId = Env.getAD_User_ID(getCtx());

        // ── 1. Load period + guard ────────────────────────────────────
        PeriodInfo period = loadPeriod(p_PayrollPeriodID, trxName);
        if ("CO".equals(period.docStatus)) {
            throw new IllegalStateException(
                "Periode " + period.periodName + " sudah DITUTUP. " +
                "Buka kembali periode dulu kalau perlu proses run tambahan."
            );
        }

        // ── 2. Guard duplikasi RunType — cegah run tipe SAMA diproses
        //    dua kali untuk periode yang sama (BUKAN mencegah kombinasi
        //    RunType berbeda seperti REGULAR+BONUS) ────────────────────
        String existingStatus = DB.getSQLValueStringEx(trxName,
            "SELECT DocStatus FROM X_Payroll_Run WHERE X_Payroll_Period_ID=? AND RunType=? " +
            "AND DocStatus IN ('DR','CO') ORDER BY Created DESC LIMIT 1",
            p_PayrollPeriodID, p_RunType);
        if (existingStatus != null) {
            throw new IllegalStateException(
                "Run tipe '" + p_RunType + "' untuk periode ini sudah ada (status: " +
                existingStatus + "). Void run yang ada dulu kalau perlu diproses ulang."
            );
        }

        // ── 3. Buat header X_Payroll_Run ──────────────────────────────
        int runId = DB.getSQLValueEx(trxName, "SELECT nextval('X_Payroll_Run_seq')");
        DB.executeUpdateEx(
            "INSERT INTO X_Payroll_Run " +
            "(X_Payroll_Run_ID, AD_Client_ID, AD_Org_ID, IsActive, " +
            " Created, CreatedBy, Updated, UpdatedBy, " +
            " X_Payroll_Period_ID, RunType, ProcessedDate, DocStatus) " +
            "VALUES (?, ?, ?, 'Y', now(), ?, now(), ?, ?, ?, now(), 'DR')",
            new Object[]{ runId, adClientId, adOrgId, adUserId, adUserId, p_PayrollPeriodID, p_RunType },
            trxName
        );

        // ── 4. Preload rate/bracket sekali di luar loop ───────────────
        List<TaxBracket> terBrackets = MPayrollTaxRate.getActiveBracketsAsTaxBracket(
            "TER", period.dateFrom, trxName);
        List<TaxBracket> progressiveBrackets = period.isDecemberReconciliation
            ? MPayrollTaxRate.getActiveBracketsAsTaxBracket("PROGRESSIVE", period.dateFrom, trxName)
            : null;
        List<MPayrollBPJSRate> activeBpjsRates = MPayrollBPJSRate.getActiveRatesForDate(
            period.dateFrom, trxName);

        // ── 5. Loop tiap employee aktif ────────────────────────────────
        List<Integer> employeeIds = getActiveEmployeeIds(period, trxName);
        int processedCount = 0;

        for (int employeeId : employeeIds) {
            EmployeeSnapshot emp = loadEmployeeSnapshot(employeeId, trxName);

            // ── 5a. Komponen gaji — baca ComponentInput, hitung 3 basis ──
            if (!MPayrollComponentInput.hasAnyInput(employeeId, p_PayrollPeriodID, trxName)) {
                log.log(Level.WARNING, "Employee {0} tidak punya X_Payroll_ComponentInput " +
                    "untuk periode {1} — di-skip dari run ini.",
                    new Object[]{ employeeId, p_PayrollPeriodID });
                continue;
            }
            List<MPayrollComponentInput.ResolvedInput> components =
                MPayrollComponentInput.getResolvedInputs(employeeId, p_PayrollPeriodID, trxName);
            
            for (MPayrollComponentInput.ResolvedInput c : components) {
                if ("EARNING".equals(c.componentType)) {
                    cashGrossIncome = cashGrossIncome.add(c.amount);
                    if (c.isTaxable) taxableEarningIncome = taxableEarningIncome.add(c.amount);
                    if (c.isBpjsBase) bpjsBaseIncome = bpjsBaseIncome.add(c.amount);
                }
            }

            // ── 5b. BPJS — loop generik semua program aktif ─────────────
            List<BpjsDetailResult> bpjsDetails = new ArrayList<>();
            BigDecimal totalBpjsEmployeeDeduction = BigDecimal.ZERO;
            BigDecimal employerTaxableAddition = BigDecimal.ZERO; // natura BPJS yg IsEmployerContributionTaxable='Y'

            for (MPayrollBPJSRate rate : activeBpjsRates) {
                boolean isEnrolled = MPayrollEmployeeProgram.isEmployeeEnrolled(
                    employeeId, rate.getProgramType(), period.dateFrom, trxName);
                if (!isEnrolled) continue;

                BigDecimal wageBase = WageCapUtil.applyCap(
                    bpjsBaseIncome, rate.getWageCapLower(), rate.getWageCapUpper());
                BigDecimal employeeAmount = wageBase.multiply(rate.getEmployeeRate())
                    .setScale(0, RoundingMode.HALF_UP);
                BigDecimal employerAmount = wageBase.multiply(rate.getEmployerRate())
                    .setScale(0, RoundingMode.HALF_UP);

                bpjsDetails.add(new BpjsDetailResult(
                    rate.getProgramType(), wageBase,
                    rate.getEmployeeRate(), rate.getEmployerRate(),
                    employeeAmount, employerAmount, rate.get_ID()
                ));

                totalBpjsEmployeeDeduction = totalBpjsEmployeeDeduction.add(employeeAmount);
                if (rate.isEmployerContributionTaxable()) {
                    employerTaxableAddition = employerTaxableAddition.add(employerAmount);
                }
            }

            BigDecimal taxableGrossIncome = taxableEarningIncome.add(employerTaxableAddition);

            // ── 5c. PPh21 — kumulatif lintas run dalam periode yang sama ─
            BigDecimal pph21;
            String terCategoryUsed = null;
            BigDecimal terRateUsed = null;
            BigDecimal cumulativeGrossBeforeThisRun = BigDecimal.ZERO;
            BigDecimal withheldPreviouslyThisPeriod = BigDecimal.ZERO;

            MPayrollEmployeeTaxProfile taxProfile =
                MPayrollEmployeeTaxProfile.getActiveProfile(employeeId, period.dateFrom, trxName);
            String schemeType = (taxProfile != null) ? taxProfile.getSchemeType() : "TER";
            boolean hasNPWP = (taxProfile == null) || "Y".equals(taxProfile.getHasNPWP());
            BigDecimal npwpMultiplier = hasNPWP ? BigDecimal.ONE : new BigDecimal("1.2");

            if (period.isDecemberReconciliation) {
                // ── Rekonsiliasi tahunan — SELALU progresif ─────────────
                BigDecimal annualGrossIncome = MPayrollRunLine.getAnnualTaxableGross(employeeId, period.dateFrom, trxName)
                    .add(taxableGrossIncome);
                BigDecimal alreadyWithheldYTD = MPayrollRunLine.getAlreadyWithheldYTD(employeeId, period.dateFrom, trxName);
                BigDecimal ptkpAmount = MPayrollPTKPRate.lookupAnnualAmount(
                    emp.ptkpStatus, period.dateFrom, trxName);

                pph21 = ProgressiveTaxCalculator.calculateDecemberAmount(
                    annualGrossIncome, ptkpAmount, alreadyWithheldYTD, progressiveBrackets);

                if (pph21.compareTo(BigDecimal.ZERO) < 0) {
                    log.log(Level.WARNING, "PPh21 Desember NEGATIF (lebih bayar) untuk employee " +
                        "{0}: {1} — TIDAK di-clamp, catat untuk kompensasi/restitusi sesuai " +
                        "kebijakan finance, bukan dianggap 0.", new Object[]{ employeeId, pph21 });
                }

            } else if ("PASAL26".equals(schemeType)) {
                List<TaxBracket> pasal26Brackets = MPayrollTaxRate.getActiveBracketsAsTaxBracket(
                    "PASAL26", period.dateFrom, trxName);
                if (pasal26Brackets.isEmpty()) {
                    throw new IllegalStateException(
                        "Employee " + employeeId + " berskema PASAL26 tapi tidak ada " +
                        "X_Payroll_TaxRate aktif untuk SchemeType='PASAL26'."
                    );
                }
                pph21 = taxableGrossIncome.multiply(pasal26Brackets.get(0).rate)
                    .setScale(0, RoundingMode.HALF_UP);

            } else {
                // ── Default: TER, KUMULATIF terhadap run lain periode ini ──
                String terCategory = (taxProfile != null && taxProfile.getTER_CategoryOverride() != null)
                    ? taxProfile.getTER_CategoryOverride()
                    : emp.terCategory;
                if (terCategory == null) {
                    throw new IllegalStateException(
                        "Employee " + employeeId + " tidak punya X_TER_Category di HR_Employee " +
                        "maupun override di X_Payroll_EmployeeTaxProfile."
                    );
                }

                cumulativeGrossBeforeThisRun = MPayrollRunLine.getCumulativeTaxableGrossThisPeriod(
                    employeeId, p_PayrollPeriodID, trxName);
                withheldPreviouslyThisPeriod = MPayrollRunLine.getCumulativeWithheldThisPeriod(
                    employeeId, p_PayrollPeriodID, trxName);

                BigDecimal combinedGross = cumulativeGrossBeforeThisRun.add(taxableGrossIncome);
                terRateUsed = TERCalculator.lookupRate(terCategory, combinedGross, terBrackets);
                terCategoryUsed = terCategory;

                BigDecimal pph21OnCombined = combinedGross.multiply(terRateUsed)
                    .multiply(npwpMultiplier).setScale(0, RoundingMode.HALF_UP);

                pph21 = pph21OnCombined.subtract(withheldPreviouslyThisPeriod);
                if (pph21.compareTo(BigDecimal.ZERO) < 0) {
                    log.log(Level.WARNING, "PPh21 kumulatif negatif untuk employee {0} run {1} " +
                        "— di-clamp ke 0 (indikasi masalah pembulatan di batas bracket, cek data).",
                        new Object[]{ employeeId, runId });
                    pph21 = BigDecimal.ZERO;
                }
            }

            // ── 5d. Totals ────────────────────────────────────────────────
            BigDecimal totalDeduction = pph21.add(totalBpjsEmployeeDeduction);
            BigDecimal netIncome = cashGrossIncome.subtract(totalDeduction); // BUKAN dari taxableGrossIncome

            // ── 5e. Insert RunLine + breakdown ────────────────────────────
            int runLineId = insertRunLine(runId, employeeId,
                cashGrossIncome, taxableGrossIncome, bpjsBaseIncome,
                terCategoryUsed, terRateUsed, pph21,
                cumulativeGrossBeforeThisRun, withheldPreviouslyThisPeriod,
                totalDeduction, netIncome, adClientId, adOrgId, adUserId, trxName);

            for (MPayrollComponentInput.ResolvedInput c : components) {
                insertRunLineComponent(runLineId, c.componentId, c.amount, adClientId, adOrgId, adUserId, trxName);
            }
            for (BpjsDetailResult detail : bpjsDetails) {
                insertRunLineDetail(runLineId, detail, adClientId, adOrgId, adUserId, trxName);
            }

            processedCount++;
        }

        // ── 6. Mark run Complete. PERIODE TIDAK otomatis ditutup — itu
        //    lewat proses "Tutup Periode" terpisah, karena periode boleh
        //    punya banyak run (REGULAR+BONUS) ────────────────────────────
        DB.executeUpdateEx(
            "UPDATE X_Payroll_Run SET DocStatus='CO' WHERE X_Payroll_Run_ID=?",
            new Object[]{ runId }, trxName);

        // ── 7. GL Journal — belum diimplementasi, item terbuka ───────────
        // generateGLJournal(runId, trxName);

        return "@OK@ - " + processedCount + " employee diproses pada Run #" + runId +
            " (RunType: " + p_RunType + ")";
    }

    // ═══════════════════════════════════════════════════════════════════
    // Helper — Period
    // ═══════════════════════════════════════════════════════════════════

    private static class PeriodInfo {
        String periodName, docStatus;
        Timestamp dateFrom, dateTo;
        boolean isDecemberReconciliation;
    }

    private PeriodInfo loadPeriod(int periodId, String trxName) {
        String sql = "SELECT PeriodName, DocStatus, DateFrom, DateTo, IsDecemberReconciliation " +
            "FROM X_Payroll_Period WHERE X_Payroll_Period_ID=?";
        PeriodInfo info = new PeriodInfo();
        try (PreparedStatement pstmt = DB.prepareStatement(sql, trxName)) {
            pstmt.setInt(1, periodId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (!rs.next()) throw new IllegalStateException("Periode ID " + periodId + " tidak ditemukan.");
                info.periodName = rs.getString("PeriodName");
                info.docStatus = rs.getString("DocStatus");
                info.dateFrom = rs.getTimestamp("DateFrom");
                info.dateTo = rs.getTimestamp("DateTo");
                info.isDecemberReconciliation = "Y".equals(rs.getString("IsDecemberReconciliation"));
            }
        } catch (Exception e) {
            throw new RuntimeException("Gagal load X_Payroll_Period: " + e.getMessage(), e);
        }
        return info;
    }

    // ═══════════════════════════════════════════════════════════════════
    // Helper — Employee & Komponen
    // ═══════════════════════════════════════════════════════════════════

    private static class EmployeeSnapshot {
        String terCategory, ptkpStatus, npwp;
    }

    private static class ComponentInputRow {
        int componentId;
        String componentType; // EARNING/DEDUCTION
        boolean isTaxable, isBpjsBase;
        BigDecimal amount;
    }
    private List<Integer> getActiveEmployeeIds(PeriodInfo period, String trxName) {
        List<Integer> ids = new ArrayList<>();
        String sql = "SELECT HR_Employee_ID FROM HR_Employee " +
            "WHERE IsActive='Y' " +
            "AND StartDate <= ? " +
            "AND (EndDate IS NULL OR EndDate >= ?)";
        try (PreparedStatement pstmt = DB.prepareStatement(sql, trxName)) {
            pstmt.setTimestamp(1, period.dateTo);
            pstmt.setTimestamp(2, period.dateFrom);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) ids.add(rs.getInt(1));
            }
        } catch (Exception e) {
            throw new RuntimeException("Gagal load daftar employee aktif: " + e.getMessage(), e);
        }
        return ids;
    }  
    private EmployeeSnapshot loadEmployeeSnapshot(int employeeId, String trxName) {
        String sql = "SELECT X_TER_Category, X_PTKPStatus, X_NPWP FROM HR_Employee WHERE HR_Employee_ID=?";
        EmployeeSnapshot snap = new EmployeeSnapshot();
        try (PreparedStatement pstmt = DB.prepareStatement(sql, trxName)) {
            pstmt.setInt(1, employeeId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    snap.terCategory = rs.getString("X_TER_Category");
                    snap.ptkpStatus = rs.getString("X_PTKPStatus");
                    snap.npwp = rs.getString("X_NPWP");
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Gagal load snapshot employee " + employeeId + ": " + e.getMessage(), e);
        }
        return snap;
    }

    /**
     * Baca X_Payroll_ComponentInput employee untuk periode ini, JOIN ke
     * X_Payroll_Component untuk ambil flag IsTaxable/IsBPJSBase/Type.
     */
    private List<ComponentInputRow> loadComponentInputs(int employeeId, int periodId, String trxName) {
        List<ComponentInputRow> rows = new ArrayList<>();
        String sql =
            "SELECT ci.X_Payroll_Component_ID, ci.Amount, " +
            "       c.ComponentType, c.IsTaxable, c.IsBPJSBase " +
            "FROM X_Payroll_ComponentInput ci " +
            "JOIN X_Payroll_Component c ON c.X_Payroll_Component_ID = ci.X_Payroll_Component_ID " +
            "WHERE ci.HR_Employee_ID=? AND ci.X_Payroll_Period_ID=? AND ci.IsActive='Y'";
        try (PreparedStatement pstmt = DB.prepareStatement(sql, trxName)) {
            pstmt.setInt(1, employeeId);
            pstmt.setInt(2, periodId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    ComponentInputRow row = new ComponentInputRow();
                    row.componentId = rs.getInt("X_Payroll_Component_ID");
                    row.amount = rs.getBigDecimal("Amount");
                    row.componentType = rs.getString("ComponentType");
                    row.isTaxable = "Y".equals(rs.getString("IsTaxable"));
                    row.isBpjsBase = "Y".equals(rs.getString("IsBPJSBase"));
                    rows.add(row);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Gagal load ComponentInput employee " + employeeId + ": " + e.getMessage(), e);
        }
        return rows;
    }

    // ═══════════════════════════════════════════════════════════════════
    // Helper — Query kumulatif (multi-run per periode, & rekonsiliasi tahunan)
    // Semua query di sini filter DocStatus='CO' — run yang di-void ('VO')
    // OTOMATIS terkecuali tanpa logic tambahan.
    // ═══════════════════════════════════════════════════════════════════

    private BigDecimal getCumulativeTaxableGrossThisPeriod(int employeeId, int periodId, String trxName) {
        BigDecimal result = DB.getSQLValueBDEx(trxName,
            "SELECT COALESCE(SUM(rl.TaxableGrossIncome), 0) FROM X_Payroll_RunLine rl " +
            "JOIN X_Payroll_Run r ON r.X_Payroll_Run_ID = rl.X_Payroll_Run_ID " +
            "WHERE rl.HR_Employee_ID=? AND r.X_Payroll_Period_ID=? AND r.DocStatus='CO'",
            employeeId, periodId);
        return result != null ? result : BigDecimal.ZERO;
    }

    private BigDecimal getCumulativeWithheldThisPeriod(int employeeId, int periodId, String trxName) {
        BigDecimal result = DB.getSQLValueBDEx(trxName,
            "SELECT COALESCE(SUM(rl.PPh21_Amount), 0) FROM X_Payroll_RunLine rl " +
            "JOIN X_Payroll_Run r ON r.X_Payroll_Run_ID = rl.X_Payroll_Run_ID " +
            "WHERE rl.HR_Employee_ID=? AND r.X_Payroll_Period_ID=? AND r.DocStatus='CO'",
            employeeId, periodId);
        return result != null ? result : BigDecimal.ZERO;
    }

    private BigDecimal getAnnualTaxableGross(int employeeId, PeriodInfo period, String trxName) {
        BigDecimal result = DB.getSQLValueBDEx(trxName,
            "SELECT COALESCE(SUM(rl.TaxableGrossIncome), 0) FROM X_Payroll_RunLine rl " +
            "JOIN X_Payroll_Run r ON r.X_Payroll_Run_ID = rl.X_Payroll_Run_ID " +
            "JOIN X_Payroll_Period p ON p.X_Payroll_Period_ID = r.X_Payroll_Period_ID " +
            "WHERE rl.HR_Employee_ID=? AND EXTRACT(YEAR FROM p.DateFrom) = EXTRACT(YEAR FROM ?::date) " +
            "AND r.DocStatus='CO'",
            employeeId, period.dateFrom);
        return result != null ? result : BigDecimal.ZERO;
    }

    private BigDecimal getAlreadyWithheldYTD(int employeeId, PeriodInfo period, String trxName) {
        BigDecimal result = DB.getSQLValueBDEx(trxName,
            "SELECT COALESCE(SUM(rl.PPh21_Amount), 0) FROM X_Payroll_RunLine rl " +
            "JOIN X_Payroll_Run r ON r.X_Payroll_Run_ID = rl.X_Payroll_Run_ID " +
            "JOIN X_Payroll_Period p ON p.X_Payroll_Period_ID = r.X_Payroll_Period_ID " +
            "WHERE rl.HR_Employee_ID=? AND EXTRACT(YEAR FROM p.DateFrom) = EXTRACT(YEAR FROM ?::date) " +
            "AND r.DocStatus='CO'",
            employeeId, period.dateFrom);
        return result != null ? result : BigDecimal.ZERO;
    }

    // ═══════════════════════════════════════════════════════════════════
    // Helper — Insert hasil
    // ═══════════════════════════════════════════════════════════════════

    private static class BpjsDetailResult {
        String programType;
        BigDecimal wageBase, employeeRate, employerRate, employeeAmount, employerAmount;
        int rateId;

        BpjsDetailResult(String programType, BigDecimal wageBase, BigDecimal employeeRate,
                          BigDecimal employerRate, BigDecimal employeeAmount,
                          BigDecimal employerAmount, int rateId) {
            this.programType = programType;
            this.wageBase = wageBase;
            this.employeeRate = employeeRate;
            this.employerRate = employerRate;
            this.employeeAmount = employeeAmount;
            this.employerAmount = employerAmount;
            this.rateId = rateId;
        }
    }

    private int insertRunLine(int runId, int employeeId,
                               BigDecimal cashGrossIncome, BigDecimal taxableGrossIncome, BigDecimal bpjsBaseIncome,
                               String terCategory, BigDecimal terRateApplied, BigDecimal pph21,
                               BigDecimal cumulativeGrossBeforeThisRun, BigDecimal withheldPreviouslyThisPeriod,
                               BigDecimal totalDeduction, BigDecimal netIncome,
                               int adClientId, int adOrgId, int adUserId, String trxName) {
        int runLineId = DB.getSQLValueEx(trxName, "SELECT nextval('X_Payroll_RunLine_seq')");
        DB.executeUpdateEx(
            "INSERT INTO X_Payroll_RunLine " +
            "(X_Payroll_RunLine_ID, AD_Client_ID, AD_Org_ID, IsActive, " +
            " Created, CreatedBy, Updated, UpdatedBy, " +
            " X_Payroll_Run_ID, HR_Employee_ID, " +
            " CashGrossIncome, TaxableGrossIncome, BPJSBaseIncome, " +
            " TER_Category, TER_RateApplied, PPh21_Amount, " +
            " CumulativeGrossBeforeThisRun, PPh21WithheldPreviouslyThisPeriod, " +
            " TotalDeduction, NetIncome) " +
            "VALUES (?, ?, ?, 'Y', now(), ?, now(), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
            new Object[]{
                runLineId, adClientId, adOrgId, adUserId, adUserId,
                runId, employeeId,
                cashGrossIncome, taxableGrossIncome, bpjsBaseIncome,
                terCategory, terRateApplied, pph21,
                cumulativeGrossBeforeThisRun, withheldPreviouslyThisPeriod,
                totalDeduction, netIncome
            },
            trxName
        );
        return runLineId;
    }

    private void insertRunLineComponent(int runLineId, ComponentInputRow c,
                                         int adClientId, int adOrgId, int adUserId, String trxName) {
        int id = DB.getSQLValueEx(trxName, "SELECT nextval('X_Payroll_RunLineComponent_seq')");
        DB.executeUpdateEx(
            "INSERT INTO X_Payroll_RunLineComponent " +
            "(X_Payroll_RunLineComponent_ID, AD_Client_ID, AD_Org_ID, IsActive, " +
            " Created, CreatedBy, Updated, UpdatedBy, " +
            " X_Payroll_RunLine_ID, X_Payroll_Component_ID, Amount) " +
            "VALUES (?, ?, ?, 'Y', now(), ?, now(), ?, ?, ?, ?)",
            new Object[]{ id, adClientId, adOrgId, adUserId, adUserId, runLineId, c.componentId, c.amount },
            trxName
        );
    }

    private void insertRunLineDetail(int runLineId, BpjsDetailResult detail,
                                      int adClientId, int adOrgId, int adUserId, String trxName) {
        int detailId = DB.getSQLValueEx(trxName, "SELECT nextval('X_Payroll_RunLineDetail_seq')");
        DB.executeUpdateEx(
            "INSERT INTO X_Payroll_RunLineDetail " +
            "(X_Payroll_RunLineDetail_ID, AD_Client_ID, AD_Org_ID, IsActive, " +
            " Created, CreatedBy, Updated, UpdatedBy, " +
            " X_Payroll_RunLine_ID, ProgramType, WageBase, " +
            " EmployeeRateApplied, EmployerRateApplied, EmployeeAmount, EmployerAmount, " +
            " X_Payroll_BPJS_Rate_ID) " +
            "VALUES (?, ?, ?, 'Y', now(), ?, now(), ?, ?, ?, ?, ?, ?, ?, ?, ?)",
            new Object[]{
                detailId, adClientId, adOrgId, adUserId, adUserId,
                runLineId, detail.programType, detail.wageBase,
                detail.employeeRate, detail.employerRate,
                detail.employeeAmount, detail.employerAmount, detail.rateId
            },
            trxName
        );
    }
}
