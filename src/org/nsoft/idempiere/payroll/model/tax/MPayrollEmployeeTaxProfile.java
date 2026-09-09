package org.nsoft.idempiere.payroll.model.tax;

import org.compiere.util.DB;
import org.compiere.util.Env;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Properties;

/**
 * Default (tanpa baris) = SchemeType 'TER', HasNPWP 'Y', TER_Category
 * dari HR_Employee.X_TER_Category. Baris di sini HANYA untuk kasus
 * khusus: karyawan asing Pasal 26, tanpa NPWP, atau override kategori
 * TER manual.
 */
public class MPayrollEmployeeTaxProfile extends X_X_Payroll_EmployeeTaxProfile {

    public MPayrollEmployeeTaxProfile(Properties ctx, int id, String trxName) {
        super(ctx, id, trxName);
    }
    public MPayrollEmployeeTaxProfile(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }

    /**
     * @return profile aktif untuk employee pada tanggal tertentu, atau
     *         null kalau tidak ada override (caller pakai default TER).
     */
    public static MPayrollEmployeeTaxProfile getActiveProfile(int employeeId, Timestamp periodDate, String trxName) {
        String sql = "SELECT X_Payroll_EmployeeTaxProfile_ID FROM X_Payroll_EmployeeTaxProfile " +
            "WHERE HR_Employee_ID=? AND IsActive='Y' " +
            "AND ValidFrom<=? AND (ValidTo IS NULL OR ValidTo>=?) " +
            "ORDER BY ValidFrom DESC LIMIT 1";
        int id = DB.getSQLValueEx(trxName, sql, employeeId, periodDate, periodDate);
        if (id <= 0) return null;
        return new MPayrollEmployeeTaxProfile(Env.getCtx(), id, trxName);
    }

    public static MPayrollEmployeeTaxProfile get(int id, String trxName) {
        return new MPayrollEmployeeTaxProfile(Env.getCtx(), id, trxName);
    }
}
