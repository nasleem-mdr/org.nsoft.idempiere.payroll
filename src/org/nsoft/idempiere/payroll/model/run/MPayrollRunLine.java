package org.nsoft.idempiere.payroll.model.run;

import org.compiere.util.DB;
import org.compiere.util.Env;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Properties;

/**
 * Berisi seluruh query kumulatif lintas run/periode yang dibutuhkan
 * GeneratePayrollRun — dipindah ke sini (bukan inline SQL di process/)
 * supaya konsisten dengan pola MPayrollPTKPRate/MPayrollTaxRate: logic
 * akses data hidup di model/, process/ hanya mengorkestrasi urutan
 * pemanggilan dan kalkulasi (calc/).
 *
 * SEMUA query di sini WAJIB filter r.DocStatus='CO' — run yang di-void
 * ('VO') otomatis terkecuali tanpa perlu logic tambahan di caller.
 */
public class MPayrollRunLine extends X_X_Payroll_RunLine {

    public MPayrollRunLine(Properties ctx, int id, String trxName) {
        super(ctx, id, trxName);
    }
    public MPayrollRunLine(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }

    /**
     * Total TaxableGrossIncome dari semua run CO lain di periode yang
     * SAMA (mis. run BONUS yang sudah diproses sebelum run REGULAR ini).
     * Dipakai basis kumulatif TER.
     */
    public static BigDecimal getCumulativeTaxableGrossThisPeriod(int employeeId, int periodId, String trxName) {
        BigDecimal result = DB.getSQLValueBDEx(trxName,
            "SELECT COALESCE(SUM(rl.TaxableGrossIncome), 0) FROM X_Payroll_RunLine rl " +
            "JOIN X_Payroll_Run r ON r.X_Payroll_Run_ID = rl.X_Payroll_Run_ID " +
            "WHERE rl.HR_Employee_ID=? AND r.X_Payroll_Period_ID=? AND r.DocStatus='CO'",
            employeeId, periodId);
        return result != null ? result : BigDecimal.ZERO;
    }

    /**
     * Total PPh21 yang sudah dipotong dari run lain di periode yang sama
     * — dikurangkan dari pajak atas total gabungan untuk dapat potongan
     * run ini (lihat TERCalculator + GeneratePayrollRun untuk pemakaian).
     */
    public static BigDecimal getCumulativeWithheldThisPeriod(int employeeId, int periodId, String trxName) {
        BigDecimal result = DB.getSQLValueBDEx(trxName,
            "SELECT COALESCE(SUM(rl.PPh21_Amount), 0) FROM X_Payroll_RunLine rl " +
            "JOIN X_Payroll_Run r ON r.X_Payroll_Run_ID = rl.X_Payroll_Run_ID " +
            "WHERE rl.HR_Employee_ID=? AND r.X_Payroll_Period_ID=? AND r.DocStatus='CO'",
            employeeId, periodId);
        return result != null ? result : BigDecimal.ZERO;
    }

    /**
     * Total TaxableGrossIncome sepanjang TAHUN (semua periode CO yang
     * DateFrom-nya di tahun yang sama dengan referenceDate) — dipakai
     * rekonsiliasi Desember (ProgressiveTaxCalculator).
     */
    public static BigDecimal getAnnualTaxableGross(int employeeId, Timestamp referenceDate, String trxName) {
        BigDecimal result = DB.getSQLValueBDEx(trxName,
            "SELECT COALESCE(SUM(rl.TaxableGrossIncome), 0) FROM X_Payroll_RunLine rl " +
            "JOIN X_Payroll_Run r ON r.X_Payroll_Run_ID = rl.X_Payroll_Run_ID " +
            "JOIN X_Payroll_Period p ON p.X_Payroll_Period_ID = r.X_Payroll_Period_ID " +
            "WHERE rl.HR_Employee_ID=? AND EXTRACT(YEAR FROM p.DateFrom) = EXTRACT(YEAR FROM ?::date) " +
            "AND r.DocStatus='CO'",
            employeeId, referenceDate);
        return result != null ? result : BigDecimal.ZERO;
    }

    /**
     * Total PPh21 yang sudah dipotong sepanjang tahun berjalan (Jan-Nov,
     * atau run Desember lain sebelumnya) — pengurang liabilitas tahunan
     * di rekonsiliasi Desember.
     */
    public static BigDecimal getAlreadyWithheldYTD(int employeeId, Timestamp referenceDate, String trxName) {
        BigDecimal result = DB.getSQLValueBDEx(trxName,
            "SELECT COALESCE(SUM(rl.PPh21_Amount), 0) FROM X_Payroll_RunLine rl " +
            "JOIN X_Payroll_Run r ON r.X_Payroll_Run_ID = rl.X_Payroll_Run_ID " +
            "JOIN X_Payroll_Period p ON p.X_Payroll_Period_ID = r.X_Payroll_Period_ID " +
            "WHERE rl.HR_Employee_ID=? AND EXTRACT(YEAR FROM p.DateFrom) = EXTRACT(YEAR FROM ?::date) " +
            "AND r.DocStatus='CO'",
            employeeId, referenceDate);
        return result != null ? result : BigDecimal.ZERO;
    }

    public static MPayrollRunLine get(int runLineId, String trxName) {
        return new MPayrollRunLine(Env.getCtx(), runLineId, trxName);
    }
}
