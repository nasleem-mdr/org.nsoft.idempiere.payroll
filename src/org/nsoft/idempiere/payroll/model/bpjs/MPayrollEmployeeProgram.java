package org.nsoft.idempiere.payroll.model.bpjs;

import org.compiere.util.DB;
import org.compiere.util.Env;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Properties;

/**
 * Model OPT-OUT: tanpa baris untuk kombinasi employee+program tertentu
 * = default IKUT program itu. Baris HANYA diisi untuk pengecualian.
 */
public class MPayrollEmployeeProgram extends X_X_Payroll_EmployeeProgram {

    public MPayrollEmployeeProgram(Properties ctx, int id, String trxName) {
        super(ctx, id, trxName);
    }
    public MPayrollEmployeeProgram(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }

    /**
     * Default TRUE (ikut program) kecuali ada baris eksplisit
     * IsEnrolled='N' yang valid untuk tanggal periode ini.
     */
    public static boolean isEmployeeEnrolled(int employeeId, String programType,
                                              Timestamp periodDate, String trxName) {
        String sql = "SELECT IsEnrolled FROM X_Payroll_EmployeeProgram " +
            "WHERE HR_Employee_ID=? AND ProgramType=? AND IsActive='Y' " +
            "AND ValidFrom<=? AND (ValidTo IS NULL OR ValidTo>=?) " +
            "ORDER BY ValidFrom DESC LIMIT 1";

        String result = DB.getSQLValueStringEx(trxName, sql,
            employeeId, programType, periodDate, periodDate);

        if (result == null) return true; // tidak ada baris = default ikut
        return "Y".equals(result);
    }

    public static MPayrollEmployeeProgram get(int id, String trxName) {
        return new MPayrollEmployeeProgram(Env.getCtx(), id, trxName);
    }
}
