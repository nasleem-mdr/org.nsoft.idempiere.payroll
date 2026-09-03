package org.nsoft.idempiere.payroll.process;

import org.compiere.process.SvrProcess;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.util.DB;
import org.compiere.util.Trx;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

/**
 * AD_Process: X_GeneratePayrollRun
 * Parameter: X_Payroll_Period_ID (mandatory)
 *
 * Satu transaksi DB untuk seluruh run — kalau satu employee gagal
 * dihitung, SELURUH run rollback. Ini kunci alasan kenapa server-side:
 * tidak ada "step 3 dari 10 employee sudah ke-commit lalu error di
 * employee ke-4" seperti kalau dipecah jadi banyak POST dari React.
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
        String trxName = get_TrxName(); // SvrProcess sudah otomatis dalam 1 Trx dari framework

        // ── Guard: period harus belum diproses (idempotent, cegah double-run) ──
        String periodStatus = DB.getSQLValueString(trxName,
            "SELECT DocStatus FROM X_Payroll_Period WHERE X_Payroll_Period_ID=?", p_PayrollPeriodID);
        if ("CO".equals(periodStatus)) {
            throw new IllegalStateException("Periode ini sudah diproses. Void/reverse dulu kalau perlu ulang.");
        }

        // ── Buat header X_Payroll_Run ──────────────────────────────────
        int runId = DB.getNextID(getCtx(), "X_Payroll_Run", trxName); // atau pakai PO.saveEx pattern
        // ... insert header (pakai MXPayrollRun generated PO class kalau sudah ada, atau raw SQL)

        // ── Loop tiap employee aktif ─────────────────────────────────
        List<Integer> employeeIds = getActiveEmployeeIds(trxName);
        int processedCount = 0;

        for (int employeeId : employeeIds) {
            BigDecimal grossIncome = calculateGrossIncome(employeeId, p_PayrollPeriodID, trxName);

            // ── PPh21 — TER atau progresif (Desember) ────────────────
            boolean isDecemberReconciliation = isDecemberPeriod(p_PayrollPeriodID, trxName);
            BigDecimal pph21;
            String terCategory = getEmployeeTERCategory(employeeId, trxName);

            if (isDecemberReconciliation) {
                pph21 = calculateAnnualReconciliation(employeeId, p_PayrollPeriodID, trxName);
            } else {
                BigDecimal terRate = lookupTERRate(terCategory, grossIncome, trxName);
                pph21 = grossIncome.multiply(terRate).setScale(0, java.math.RoundingMode.HALF_UP);
            }

            // ── BPJS ──────────────────────────────────────────────────
            BigDecimal[] bpjsKesehatan = calculateBPJS("KESEHATAN", grossIncome, trxName);
            BigDecimal[] bpjsJHT      = calculateBPJS("JHT", grossIncome, trxName);
            BigDecimal[] bpjsJP       = calculateBPJS("JP", grossIncome, trxName);
            BigDecimal[] bpjsJKK      = calculateBPJS("JKK", grossIncome, trxName); // employer only
            BigDecimal[] bpjsJKM      = calculateBPJS("JKM", grossIncome, trxName); // employer only

            BigDecimal totalDeduction = pph21
                .add(bpjsKesehatan[0]).add(bpjsJHT[0]).add(bpjsJP[0]);
            BigDecimal netIncome = grossIncome.subtract(totalDeduction);

            insertRunLine(runId, employeeId, grossIncome, terCategory, pph21,
                bpjsKesehatan, bpjsJHT, bpjsJP, bpjsJKK, bpjsJKM,
                totalDeduction, netIncome, trxName);

            processedCount++;
        }

        // ── Mark period Complete ─────────────────────────────────────
        DB.executeUpdateEx(
            "UPDATE X_Payroll_Period SET DocStatus='CO' WHERE X_Payroll_Period_ID=?",
            new Object[]{p_PayrollPeriodID}, trxName);

        // ── Generate GL Journal (opsional, lihat catatan di bawah) ───
        // generateGLJournal(runId, trxName);

        return "@OK@ - " + processedCount + " employee diproses";
    }

    // ... method-method private lainnya (lookupTERRate, calculateBPJS, dst)
    // — saya detailkan di bawah kalau kamu mau lanjut ke situ
              }
