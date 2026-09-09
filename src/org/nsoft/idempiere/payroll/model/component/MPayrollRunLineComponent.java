package org.nsoft.idempiere.payroll.model.component;

import org.compiere.util.DB;
import org.compiere.util.Env;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.Properties;

/**
 * Snapshot breakdown komponen gaji per employee per run — immutable
 * setelah insert (hasil proses GeneratePayrollRun), tidak berubah
 * meski X_Payroll_ComponentInput sumbernya diedit belakangan.
 */
public class MPayrollRunLineComponent extends X_X_Payroll_RunLineComponent {

    public MPayrollRunLineComponent(Properties ctx, int id, String trxName) {
        super(ctx, id, trxName);
    }
    public MPayrollRunLineComponent(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }

    /**
     * Total komponen bertipe EARNING untuk satu run line — dipakai
     * verifikasi silang (mis. PDF slip) bahwa SUM breakdown = CashGrossIncome
     * yang tersimpan di header X_Payroll_RunLine.
     */
    public static BigDecimal sumEarningForRunLine(int runLineId, String trxName) {
        BigDecimal result = DB.getSQLValueBDEx(trxName,
            "SELECT COALESCE(SUM(rlc.Amount), 0) FROM X_Payroll_RunLineComponent rlc " +
            "JOIN X_Payroll_Component c ON c.X_Payroll_Component_ID = rlc.X_Payroll_Component_ID " +
            "WHERE rlc.X_Payroll_RunLine_ID=? AND c.ComponentType='EARNING'",
            runLineId);
        return result != null ? result : BigDecimal.ZERO;
    }

    public static MPayrollRunLineComponent get(int id, String trxName) {
        return new MPayrollRunLineComponent(Env.getCtx(), id, trxName);
    }
}
