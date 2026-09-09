package org.nsoft.idempiere.payroll.model.tax;

import org.compiere.util.DB;
import org.compiere.util.Env;
import org.nsoft.idempiere.payroll.calc.TaxBracket;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class MPayrollTaxRate extends X_X_Payroll_TaxRate {

    public MPayrollTaxRate(Properties ctx, int id, String trxName) {
        super(ctx, id, trxName);
    }
    public MPayrollTaxRate(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }

    /**
     * Load bracket aktif untuk SchemeType tertentu ('TER'/'PROGRESSIVE'/
     * 'PASAL26'/...), dikonversi langsung ke DTO TaxBracket di calc/
     * package — supaya calc/ tetap bebas dependency DB.
     */
    public static List<TaxBracket> getActiveBracketsAsTaxBracket(String schemeType, Timestamp date, String trxName) {
        List<TaxBracket> list = new ArrayList<>();
        String sql = "SELECT Category, IncomeFrom, IncomeTo, Rate FROM X_Payroll_TaxRate " +
            "WHERE SchemeType=? AND IsActive='Y' AND ValidFrom<=? AND (ValidTo IS NULL OR ValidTo>=?) " +
            "ORDER BY Category, IncomeFrom";
        try (PreparedStatement pstmt = DB.prepareStatement(sql, trxName)) {
            pstmt.setString(1, schemeType);
            pstmt.setTimestamp(2, date);
            pstmt.setTimestamp(3, date);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new TaxBracket(
                        rs.getString("Category"),
                        rs.getBigDecimal("IncomeFrom"),
                        rs.getBigDecimal("IncomeTo"),
                        rs.getBigDecimal("Rate")
                    ));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Gagal load X_Payroll_TaxRate (" + schemeType + "): " + e.getMessage(), e);
        }
        return list;
    }

    public static MPayrollTaxRate get(int id, String trxName) {
        return new MPayrollTaxRate(Env.getCtx(), id, trxName);
    }
}
