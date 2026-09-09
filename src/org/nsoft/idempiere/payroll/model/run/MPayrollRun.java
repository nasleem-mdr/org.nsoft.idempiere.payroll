package org.nsoft.idempiere.payroll.model.run;

import org.compiere.util.DB;
import org.compiere.util.Env;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Properties;

public class MPayrollRun extends X_X_Payroll_Run {

    public MPayrollRun(Properties ctx, int id, String trxName) {
        super(ctx, id, trxName);
    }
    public MPayrollRun(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }

    public boolean isCompleted() {
        return "CO".equals(getDocStatus());
    }

    public boolean isVoided() {
        return "VO".equals(getDocStatus());
    }

    /**
     * Cek apakah sudah ada run dengan RunType yang sama untuk periode ini
     * yang masih DR atau sudah CO — dipakai guard duplikasi di
     * GeneratePayrollRun sebelum membuat run baru.
     * @return DocStatus run yang ditemukan, atau null kalau belum ada.
     */
    public static String findExistingRunStatus(int periodId, String runType, String trxName) {
        return DB.getSQLValueStringEx(trxName,
            "SELECT DocStatus FROM X_Payroll_Run WHERE X_Payroll_Period_ID=? AND RunType=? " +
            "AND DocStatus IN ('DR','CO') ORDER BY Created DESC LIMIT 1",
            periodId, runType);
    }

    /**
     * Cari run LAIN (bukan runId ini sendiri) di periode yang sama yang
     * diproses SESUDAH run ini dan masih berstatus CO. Dipakai guard LIFO
     * di X_VoidPayrollRun — kalau ada hasil, run ini TIDAK boleh di-void
     * dulu karena run yang lebih baru kemungkinan sudah menghitung pajak
     * kumulatif berdasarkan angka dari run ini.
     * @return deskripsi run yang lebih baru ("ID (RunType)"), atau null
     *         kalau aman untuk di-void.
     */
    public static String findLaterCompletedRun(int periodId, int excludeRunId,
                                                 Timestamp afterProcessedDate, String trxName) {
        return DB.getSQLValueStringEx(trxName,
            "SELECT X_Payroll_Run_ID || ' (' || RunType || ')' FROM X_Payroll_Run " +
            "WHERE X_Payroll_Period_ID=? AND DocStatus='CO' " +
            "AND ProcessedDate > ? AND X_Payroll_Run_ID != ? " +
            "ORDER BY ProcessedDate ASC LIMIT 1",
            periodId, afterProcessedDate, excludeRunId);
    }

    public void markCompleted(String trxName) {
        DB.executeUpdateEx(
            "UPDATE X_Payroll_Run SET DocStatus='CO' WHERE X_Payroll_Run_ID=?",
            new Object[]{ get_ID() }, trxName);
    }

    public void markVoided(int voidedByUserId, String reason, String trxName) {
        DB.executeUpdateEx(
            "UPDATE X_Payroll_Run SET DocStatus='VO', VoidedDate=now(), " +
            "VoidedBy=?, VoidReason=? WHERE X_Payroll_Run_ID=?",
            new Object[]{ voidedByUserId, reason, get_ID() }, trxName);
    }

    public static MPayrollRun get(int runId, String trxName) {
        return new MPayrollRun(Env.getCtx(), runId, trxName);
    }
}
