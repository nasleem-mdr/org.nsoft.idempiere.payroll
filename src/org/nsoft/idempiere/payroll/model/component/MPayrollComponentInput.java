package org.nsoft.idempiere.payroll.model.component;

import org.compiere.util.DB;
import org.compiere.util.Env;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Input HR sebelum run diproses — sumber data Gaji Pokok/Tunjangan per
 * employee per periode. Di-snapshot menjadi X_Payroll_RunLineComponent
 * saat GeneratePayrollRun dieksekusi; perubahan di sini SETELAH run
 * Complete tidak mempengaruhi run yang sudah jadi.
 */
public class MPayrollComponentInput extends X_X_Payroll_ComponentInput {

    /** Hasil JOIN ke X_Payroll_Component — dipakai langsung oleh
     *  GeneratePayrollRun tanpa perlu query terpisah per baris. */
    public static class ResolvedInput {
        public final int componentId;
        public final String componentType;
        public final boolean isTaxable;
        public final boolean isBpjsBase;
        public final BigDecimal amount;

        public ResolvedInput(int componentId, String componentType,
                              boolean isTaxable, boolean isBpjsBase, BigDecimal amount) {
            this.componentId = componentId;
            this.componentType = componentType;
            this.isTaxable = isTaxable;
            this.isBpjsBase = isBpjsBase;
            this.amount = amount;
        }
    }

    public MPayrollComponentInput(Properties ctx, int id, String trxName) {
        super(ctx, id, trxName);
    }
    public MPayrollComponentInput(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }

    /**
     * Load input komponen employee untuk periode tertentu, sudah di-JOIN
     * dengan flag IsTaxable/IsBPJSBase/ComponentType dari master
     * X_Payroll_Component — dipanggil GeneratePayrollRun per employee
     * di dalam loop.
     */
    public static List<ResolvedInput> getResolvedInputs(int employeeId, int periodId, String trxName) {
        List<ResolvedInput> rows = new ArrayList<>();
        String sql =
            "SELECT ci.X_Payroll_Component_ID, ci.Amount, " +
            "       c.ComponentType, c.IsTaxable, c.IsBPJSBase " +
            "FROM X_Payroll_ComponentInput ci " +
            "JOIN X_Payroll_Component c ON c.X_Payroll_Component_ID = ci.X_Payroll_Component_ID " +
            "WHERE ci.HR_Employee_ID=? AND ci.X_Payroll_Period_ID=? AND ci.IsActive='Y'";
        try (PreparedStatement pstmt = DB.prepareStatement(sql, trxName)) {
            pstmt.setInt(1, employeeId);
            pstmt.setInt(2, periodId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    rows.add(new ResolvedInput(
                        rs.getInt("X_Payroll_Component_ID"),
                        rs.getString("ComponentType"),
                        "Y".equals(rs.getString("IsTaxable")),
                        "Y".equals(rs.getString("IsBPJSBase")),
                        rs.getBigDecimal("Amount")
                    ));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(
                "Gagal load ComponentInput employee " + employeeId + " periode " + periodId +
                ": " + e.getMessage(), e);
        }
        return rows;
    }

    /**
     * Cek apakah employee punya minimal 1 input komponen untuk periode
     * ini — dipakai GeneratePayrollRun untuk skip (dengan warning log)
     * employee yang belum di-setup gajinya, alih-alih throw error yang
     * menghentikan seluruh run untuk semua employee lain.
     */
    public static boolean hasAnyInput(int employeeId, int periodId, String trxName) {
        int count = DB.getSQLValueEx(trxName,
            "SELECT COUNT(*) FROM X_Payroll_ComponentInput " +
            "WHERE HR_Employee_ID=? AND X_Payroll_Period_ID=? AND IsActive='Y'",
            employeeId, periodId);
        return count > 0;
    }

    public static MPayrollComponentInput get(int id, String trxName) {
        return new MPayrollComponentInput(Env.getCtx(), id, trxName);
    }
}
