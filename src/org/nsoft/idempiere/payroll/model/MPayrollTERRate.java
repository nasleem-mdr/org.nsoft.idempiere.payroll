// model/MPayrollTERRate.java  (custom — di sinilah kamu taruh business logic tambahan)
package org.nsoft.idempiere.payroll.model;

import java.sql.ResultSet;
import java.util.Properties;

public class MPayrollTERRate extends X_X_Payroll_TER_Rate {
    public MPayrollTERRate(Properties ctx, int id, String trxName) {
        super(ctx, id, trxName);
    }
    public MPayrollTERRate(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }
    // model/tax/MPayrollTaxRate.java — tambahkan:
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
}
