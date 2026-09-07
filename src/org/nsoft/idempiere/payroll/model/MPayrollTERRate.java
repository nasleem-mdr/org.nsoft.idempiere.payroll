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
    // helper query statis, dipakai oleh TERCalculator/GeneratePayrollRun
}
