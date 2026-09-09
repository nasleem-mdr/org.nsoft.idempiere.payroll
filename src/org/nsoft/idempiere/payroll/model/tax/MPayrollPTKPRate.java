package org.nsoft.idempiere.payroll.model.tax;

import org.compiere.util.DB;
import org.compiere.util.Env;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Properties;

public class MPayrollPTKPRate extends X_X_Payroll_PTKP_Rate {

    public MPayrollPTKPRate(Properties ctx, int id, String trxName) {
        super(ctx, id, trxName);
    }
    public MPayrollPTKPRate(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }

    /**
     * @throws IllegalStateException kalau status tidak ditemukan —
     *         sengaja fail-fast, payroll run TIDAK BOLEH lanjut dengan
     *         asumsi PTKP=0 (itu meng-under-report PPh21 employee).
     */
    public static BigDecimal lookupAnnualAmount(String ptkpStatus, Timestamp periodDate, String trxName) {
        String sql = "SELECT AnnualAmount FROM X_Payroll_PTKP_Rate " +
            "WHERE PTKPStatus=? AND IsActive='Y' " +
            "AND ValidFrom<=? AND (ValidTo IS NULL OR ValidTo>=?) " +
            "ORDER BY ValidFrom DESC LIMIT 1";

        BigDecimal result = DB.getSQLValueBDEx(trxName, sql, ptkpStatus, periodDate, periodDate);

        if (result == null) {
            throw new IllegalStateException(
                "Tidak ditemukan rate PTKP aktif untuk status '" + ptkpStatus +
                "' pada tanggal " + periodDate + " — cek data X_Payroll_PTKP_Rate. " +
                "Payroll run dihentikan (bukan default ke 0) untuk mencegah under-report PPh21."
            );
        }
        return result;
    }

    public static MPayrollPTKPRate get(int id, String trxName) {
        return new MPayrollPTKPRate(Env.getCtx(), id, trxName);
    }
}
