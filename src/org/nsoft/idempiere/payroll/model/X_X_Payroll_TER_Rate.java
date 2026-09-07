// model/X_X_Payroll_TER_Rate.java  (base — JANGAN edit manual, hasil generate)
package org.nsoft.idempiere.payroll.model;

import org.compiere.model.PO;
import org.compiere.model.I_Persistent;
import java.sql.ResultSet;
import java.math.BigDecimal;
import java.util.Properties;

public class X_X_Payroll_TER_Rate extends PO implements I_Persistent {
    public X_X_Payroll_TER_Rate(Properties ctx, int X_Payroll_TER_Rate_ID, String trxName) {
        super(ctx, X_Payroll_TER_Rate_ID, trxName);
    }
    public X_X_Payroll_TER_Rate(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }

    public void setTER_Category(String TER_Category) { set_Value("TER_Category", TER_Category); }
    public String getTER_Category() { return (String) get_Value("TER_Category"); }

    public void setIncomeFrom(BigDecimal IncomeFrom) { set_Value("IncomeFrom", IncomeFrom); }
    public BigDecimal getIncomeFrom() { return (BigDecimal) get_Value("IncomeFrom"); }

    public void setIncomeTo(BigDecimal IncomeTo) { set_Value("IncomeTo", IncomeTo); }
    public BigDecimal getIncomeTo() { return (BigDecimal) get_Value("IncomeTo"); }

    public void setRate(BigDecimal Rate) { set_Value("Rate", Rate); }
    public BigDecimal getRate() { return (BigDecimal) get_Value("Rate"); }

    @Override
    public String getTableName() { return "X_Payroll_TER_Rate"; }
}
