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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * AD_Process: X_GeneratePayrollRun
 * Parameter: X_Payroll_Period_ID (mandatory)
 *
 * Satu proses = satu transaksi DB penuh (SvrProcess otomatis jalan dalam
 * 1 Trx via get_TrxName()). Kalau satu employee gagal dihitung di tengah
 * jalan, SELURUH run rollback — tidak ada kondisi "5 dari 10 employee
 * sudah ke-commit". Ini alasan utama kenapa proses ini server-side,
 * bukan rangkaian POST dari React.
 *
 * ASUMSI SAAT INI: struktur gaji FLAT (1 angka GrossIncome per employee,
 * diambil dari kolom X_MonthlyGrossIncome di HR_Employee — GANTI method
 * calculateGrossIncome() kalau nanti struktur berubah jadi komponen
 * bertingkat, lihat komentar di method itu).
 */
public class GeneratePayrollRun extends SvrProcess {

    private int p_PayrollPeriodID = 0;

    @Override
    protected void prepare() {
        for (ProcessInfoParameter para : getParameter()) {
            String name = para.getParameterName();
            if (name.equals("X_Payroll_Period_ID")) {
                p_PayrollPeriodID = para.getParameterAsInt();
            }
        }
        if (p_PayrollPeriodID <= 0) {
            throw new IllegalArgumentException("Payroll Period wajib diisi.");
        }
    }

    @Override
    protected String doIt() throws Exception {
        String trxName = get_TrxName();

        // ── 1. Load period + guard idempotency ──────────────────────────
        PeriodInfo period = loadPeriod(p_PayrollPeriodID, trxName);
        if ("CO".equals(period.docStatus)) {
            throw new IllegalStateException(
                "Periode " + period.periodName + " sudah diproses. " +
                "Void/reverse run yang ada dulu kalau perlu diproses ulang."
            );
        }

        // ── 2. Buat header X_Payroll_Run ─────────────────────────────────
        int runId = DB.getSQLValueEx(trxName,
            "SELECT nextval('X_Payroll_Run_seq')");
        int adClientId = Env.getAD_Client_ID(getCtx());
        int adOrgId = Env.getAD_Org_ID(getCtx());

        DB.executeUpdateEx(
            "INSERT INTO X_Payroll_Run " +
            "(X_Payroll_Run_ID, AD_Client_ID, AD_Org_ID, IsActive, " +
            " Created, CreatedBy, Updated, UpdatedBy, " +
            " X_Payroll_Period_ID, ProcessedDate, DocStatus) " +
            "VALUES (?, ?, ?, 'Y', now(), ?, now(), ?, ?, now(), 'DR')",
            new Object[]{ runId, adClientId, adOrgId, getAD_User_ID(), getAD_User_ID(), p_PayrollPeriodID },
            trxName
        );

        // ── 3. Preload rate/bracket — SEKALI per run, bukan per employee ──
        // Query rate di dalam loop employee akan sangat lambat untuk
        // jumlah karyawan besar; load sekali di luar loop.
        List<TaxBracket> terBrackets = MPayrollTaxRate.getActiveBracketsAsTaxBracket(
            "TER", period.dateFrom, trxName);
        List<TaxBracket> progressiveBrackets = period.isDecemberReconciliation
            ? MPayrollTaxRate.getActiveBracketsAsTaxBracket("PROGRESSIVE", period.dateFrom, trxName)
            : null;
        List<MPayrollBPJSRate> activeBpjsRates = MPayrollBPJSRate.getActiveRatesForDate(
            period.dateFrom, trxName);

        // ── 4. Loop tiap employee aktif ───────────────────────────────────
        List<Integer> employeeIds = getActiveEmployeeIds(trxName);
        int processedCount = 0;

        for (int employeeId : employeeIds) {
            EmployeeSnapshot emp = loadEmployeeSnapshot(employeeId, trxName);

            BigDecimal grossIncome = calculateGrossIncome(employeeId, period, trxName);

            // ── 4a. PPh21 ───────────────────────────────────────────────
            BigDecimal pph21;
            String terCategoryUsed = null;
            BigDecimal terRateUsed = null;

            MPayrollEmployeeTaxProfile taxProfile =
                MPayrollEmployeeTaxProfile.getActiveProfile(employeeId, period.dateFrom, trxName);
            String schemeType = (taxProfile != null) ? taxProfile.getSchemeType() : "TER";
            boolean hasNPWP = (taxProfile == null) || "Y".equals(taxProfile.getHasNPWP());
            BigDecimal npwpMultiplier = hasNPWP ? BigDecimal.ONE : new BigDecimal("1.2");

            if (period.isDecemberReconciliation) {
                // ── Rekonsiliasi tahunan — SELALU progresif, terlepas
                //    dari schemeType assignment employee ───────────────
                BigDecimal annualGrossIncome = getAnnualGrossIncome(employeeId, period, trxName);
                BigDecimal alreadyWithheldYTD = getAlreadyWithheldYTD(employeeId, period, trxName);
                String ptkpStatus = emp.ptkpStatus;
                BigDecimal ptkpAmount = MPayrollPTKPRate.lookupAnnualAmount(
                    ptkpStatus, period.dateFrom, trxName);

                pph21 = ProgressiveTaxCalculator.calculateDecemberAmount(
                    annualGrossIncome, ptkpAmount, alreadyWithheldYTD, progressiveBrackets);

            } else if ("PASAL26".equals(schemeType)) {
                List<TaxBracket> pasal26Brackets = MPayrollTaxRate.getActiveBracketsAsTaxBracket(
                    "PASAL26", period.dateFrom, trxName);
                if (pasal26Brackets.isEmpty()) {
                    throw new IllegalStateException(
                        "Employee " + employeeId + " berskema PASAL26 tapi tidak ada " +
                        "X_Payroll_TaxRate aktif untuk SchemeType='PASAL26'."
                    );
                }
                pph21 = grossIncome.multiply(pasal26Brackets.get(0).rate)
                    .setScale(0, RoundingMode.HALF_UP);

            } else {
                // ── Default: TER bulanan biasa ─────────────────────────
                String terCategory = (taxProfile != null && taxProfile.getTER_CategoryOverride() != null)
                    ? taxProfile.getTER_CategoryOverride()
                    : emp.terCategory;

                if (terCategory == null) {
                    throw new IllegalStateException(
                        "Employee " + employeeId + " tidak punya X_TER_Category di HR_Employee " +
                        "maupun override di X_Payroll_EmployeeTaxProfile. Tidak bisa hitung TER."
                    );
                }

                terRateUsed = TERCalculator.lookupRate(terCategory, grossIncome, terBrackets);
                terCategoryUsed = terCategory;
                pph21 = TERCalculator.calculate(terCategory, grossIncome, terBrackets, npwpMultiplier);
            }

            // ── 4b. BPJS — loop generik semua program aktif ────────────
            List<BpjsDetailResult> bpjsDetails = new ArrayList<>();
            BigDecimal totalBpjsEmployeeDeduction = BigDecimal.ZERO;

            for (MPayrollBPJSRate rate : activeBpjsRates) {
                boolean isEnrolled = MPayrollEmployeeProgram.isEmployeeEnrolled(
                    employeeId, rate.getProgramType(), period.dateFrom, trxName);
                if (!isEnrolled) continue;

                BigDecimal wageBase = WageCapUtil.applyCap(
                    grossIncome, rate.getWageCapLower(), rate.getWageCapUpper());
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
            }

            // ── 4c. Totals ──────────────────────────────────────────────
            BigDecimal totalDeduction = pph21.add(totalBpjsEmployeeDeduction);
            BigDecimal netIncome = grossIncome.subtract(totalDeduction);

            // ── 4d. Insert X_Payroll_RunLine ────────────────────────────
            int runLineId = insertRunLine(runId, employeeId, grossIncome,
                terCategoryUsed, terRateUsed, pph21, totalDeduction, netIncome,
                adClientId, adOrgId, trxName);

            // ── 4e. Insert X_Payroll_RunLineDetail per program BPJS ─────
            for (BpjsDetailResult detail : bpjsDetails) {
                insertRunLineDetail(runLineId, detail, adClientId, adOrgId, trxName);
            }

            processedCount++;
        }

        // ── 5. Mark run & period Complete ────────────────────────────────
        DB.executeUpdateEx(
            "UPDATE X_Payroll_Run SET DocStatus='CO' WHERE X_Payroll_Run_ID=?",
            new Object[]{ runId }, trxName);
        DB.executeUpdateEx(
            "UPDATE X_Payroll_Period SET DocStatus='CO' WHERE X_Payroll_Period_ID=?",
            new Object[]{ p_PayrollPeriodID }, trxName);

        // ── 6. GL Journal — belum diimplementasi, lihat catatan terpisah ──
        // generateGLJournal(runId, trxName);

        return "@OK@ - " + processedCount + " employee diproses pada Run #" + runId;
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
        try (java.sql.PreparedStatement pstmt = DB.prepareStatement(sql, trxName)) {
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
    // Helper — Employee
    // ═══════════════════════════════════════════════════════════════════

    private static class EmployeeSnapshot {
        String terCategory, ptkpStatus, npwp;
    }

    private List<Integer> getActiveEmployeeIds(String trxName) {
        List<Integer> ids = new ArrayList<>();
        String sql = "SELECT HR_Employee_ID FROM HR_Employee WHERE IsActive='Y'";
        try (java.sql.PreparedStatement pstmt = DB.prepareStatement(sql, trxName);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) ids.add(rs.getInt(1));
        } catch (Exception e) {
            throw new RuntimeException("Gagal load daftar employee aktif: " + e.getMessage(), e);
        }
        return ids;
    }

    private EmployeeSnapshot loadEmployeeSnapshot(int employeeId, String trxName) {
        String sql = "SELECT X_TER_Category, X_PTKPStatus, X_NPWP FROM HR_Employee WHERE HR_Employee_ID=?";
        EmployeeSnapshot snap = new EmployeeSnapshot();
        try (java.sql.PreparedStatement pstmt = DB.prepareStatement(sql, trxName)) {
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
     * ⚠️ TITIK YANG PERLU DIGANTI kalau struktur gaji jadi komponen
     * (Gaji Pokok + Tunjangan + Lembur, dst) alih-alih flat.
     *
     * Versi flat sekarang: baca kolom custom X_MonthlyGrossIncome di
     * HR_Employee (perlu ditambahkan lewat GUI Table and Column kalau
     * belum ada) — atau ganti sumbernya ke HR_Contract kalau gaji pokok
     * disimpan di sana.
     *
     * Versi komponen (kalau nanti dipilih): method ini akan JOIN ke
     * X_Payroll_RunLineDetail earning + X_Payroll_Component (IsTaxable
     * flag menentukan komponen mana yang masuk basis PPh21 vs tidak —
     * catatan: itu berarti GrossIncome untuk PPh21 bisa BEDA dari
     * GrossIncome untuk basis BPJS, kompleksitas tambahan yang perlu
     * dipikirkan ulang strukturnya kalau memang ke arah situ).
     */
    private BigDecimal calculateGrossIncome(int employeeId, PeriodInfo period, String trxName) {
        BigDecimal gross = DB.getSQLValueBDEx(trxName,
            "SELECT X_MonthlyGrossIncome FROM HR_Employee WHERE HR_Employee_ID=?", employeeId);
        if (gross == null) {
            throw new IllegalStateException(
                "Employee " + employeeId + " tidak punya X_MonthlyGrossIncome di HR_Employee."
            );
        }
        return gross;
    }

    private BigDecimal getAnnualGrossIncome(int employeeId, PeriodInfo period, String trxName) {
        // Jumlah GrossIncome dari SEMUA X_Payroll_RunLine tahun berjalan
        // (Jan-Nov yang sudah diproses) DITAMBAH gross bulan Desember
        // berjalan (period ini sendiri, belum ada RunLine-nya).
        BigDecimal ytdFromPreviousRuns = DB.getSQLValueBDEx(trxName,
            "SELECT COALESCE(SUM(rl.GrossIncome), 0) FROM X_Payroll_RunLine rl " +
            "JOIN X_Payroll_Run r ON r.X_Payroll_Run_ID = rl.X_Payroll_Run_ID " +
            "JOIN X_Payroll_Period p ON p.X_Payroll_Period_ID = r.X_Payroll_Period_ID " +
            "WHERE rl.HR_Employee_ID=? AND EXTRACT(YEAR FROM p.DateFrom) = EXTRACT(YEAR FROM ?::date) " +
            "AND r.DocStatus='CO'",
            employeeId, period.dateFrom);

        BigDecimal decemberGross = calculateGrossIncome(employeeId, period, trxName);
        return (ytdFromPreviousRuns != null ? ytdFromPreviousRuns : BigDecimal.ZERO).add(decemberGross);
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

    private int insertRunLine(int runId, int employeeId, BigDecimal grossIncome,
                               String terCategory, BigDecimal terRateApplied,
                               BigDecimal pph21, BigDecimal totalDeduction, BigDecimal netIncome,
                               int adClientId, int adOrgId, String trxName) {
        int runLineId = DB.getSQLValueEx(trxName, "SELECT nextval('X_Payroll_RunLine_seq')");
        DB.executeUpdateEx(
            "INSERT INTO X_Payroll_RunLine " +
            "(X_Payroll_RunLine_ID, AD_Client_ID, AD_Org_ID, IsActive, " +
            " Created, CreatedBy, Updated, UpdatedBy, " +
            " X_Payroll_Run_ID, HR_Employee_ID, GrossIncome, " +
            " TER_Category, TER_RateApplied, PPh21_Amount, TotalDeduction, NetIncome) " +
            "VALUES (?, ?, ?, 'Y', now(), ?, now(), ?, ?, ?, ?, ?, ?, ?, ?, ?)",
            new Object[]{
                runLineId, adClientId, adOrgId, getAD_User_ID(), getAD_User_ID(),
                runId, employeeId, grossIncome,
                terCategory, terRateApplied,
