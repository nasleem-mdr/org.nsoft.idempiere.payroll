package org.nsoft.idempiere.payroll.model.bpjs;

import org.compiere.util.DB;
import org.compiere.util.Env;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class MPayrollBPJSRate extends X_X_Payroll_BPJS_Rate {

    public MPayrollBPJSRate(Properties ctx, int id, String trxName) {
        super(ctx, id, trxName);
    }
    public MPayrollBPJSRate(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }

    public boolean isEmployerContributionTaxable() {
        return "Y".equals(get_ValueAsString("IsEmployerContributionTaxable"));
    }

    /**
     * Load semua rate BPJS aktif & valid untuk tanggal tertentu — dipanggil
     * SEKALI di luar loop employee oleh GeneratePayrollRun (bukan per
     * employee) untuk performa.
     */
    public static List<MPayrollBPJSRate> getActiveRatesForDate(Timestamp date, String trxName) {
        List<MPayrollBPJSRate> list = new ArrayList<>();
        String sql = "SELECT X_Payroll_BPJS_Rate_ID FROM X_Payroll_BPJS_Rate " +
            "WHERE IsActive='Y' AND ValidFrom<=? AND (ValidTo IS NULL OR ValidTo>=?)";
        try (PreparedStatement pstmt = DB.prepareStatement(sql, trxName)) {
            pstmt.setTimestamp(1, date);
            pstmt.setTimestamp(2, date);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new MPayrollBPJSRate(Env.getCtx(), rs.getInt(1), trxName));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Gagal load X_Payroll_BPJS_Rate aktif: " + e.getMessage(), e);
        }
        return list;
    }

    public static MPayrollBPJSRate get(int id, String trxName) {
        return new MPayrollBPJSRate(Env.getCtx(), id, trxName);
    }
}
