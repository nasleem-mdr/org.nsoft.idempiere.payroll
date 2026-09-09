package org.nsoft.idempiere.payroll.model.run;

import org.compiere.util.Env;

import java.sql.ResultSet;
import java.util.Properties;

/**
 * Belum ada business logic tambahan di luar CRUD standar — breakdown
 * per program BPJS ini murni snapshot hasil kalkulasi GeneratePayrollRun.
 * Disiapkan sebagai titik ekstensi kalau nanti butuh, mis. helper query
 * "total iuran BPJS employer sebulan untuk keperluan laporan/GL".
 */
public class MPayrollRunLineDetail extends X_X_Payroll_RunLineDetail {

    public MPayrollRunLineDetail(Properties ctx, int id, String trxName) {
        super(ctx, id, trxName);
    }
    public MPayrollRunLineDetail(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }

    public static MPayrollRunLineDetail get(int id, String trxName) {
        return new MPayrollRunLineDetail(Env.getCtx(), id, trxName);
    }
}
