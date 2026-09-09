package org.nsoft.idempiere.payroll.model.run;

import org.compiere.util.DB;
import org.compiere.util.Env;

import java.sql.ResultSet;
import java.util.Properties;

public class MPayrollPeriod extends X_X_Payroll_Period {

    public MPayrollPeriod(Properties ctx, int id, String trxName) {
        super(ctx, id, trxName);
    }
    public MPayrollPeriod(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }

    public boolean isClosed() {
        return "CO".equals(getDocStatus());
    }

    public boolean isDecemberReconciliation() {
        return "Y".equals(get_ValueAsString("IsDecemberReconciliation"));
    }

    /**
     * Dipakai X_ClosePayrollPeriod (belum diimplementasi) — cek semua
     * X_Payroll_Run pada periode ini sudah CO atau VO, tidak ada yang
     * masih DR, sebelum periode boleh ditutup.
     */
    public static boolean hasAnyDraftRun(int periodId, String trxName) {
        int count = DB.getSQLValueEx(trxName,
            "SELECT COUNT(*) FROM X_Payroll_Run WHERE X_Payroll_Period_ID=? AND DocStatus='DR'",
            periodId);
        return count > 0;
    }

    public static MPayrollPeriod get(int periodId, String trxName) {
        return new MPayrollPeriod(Env.getCtx(), periodId, trxName);
    }
}
