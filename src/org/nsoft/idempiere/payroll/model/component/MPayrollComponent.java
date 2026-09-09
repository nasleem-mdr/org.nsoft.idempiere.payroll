package org.nsoft.idempiere.payroll.model.component;

import org.compiere.util.DB;
import org.compiere.util.Env;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Master komponen gaji (Gaji Pokok, Tunjangan Transport, dst).
 * IsTaxable  = komponen ini masuk basis PPh21 (TaxableGrossIncome)?
 * IsBPJSBase = komponen ini masuk basis perhitungan BPJS?
 * ComponentType = EARNING (menambah gaji) / DEDUCTION (mengurangi).
 */
public class MPayrollComponent extends X_X_Payroll_Component {

    public static final String COMPONENTTYPE_EARNING = "EARNING";
    public static final String COMPONENTTYPE_DEDUCTION = "DEDUCTION";

    public MPayrollComponent(Properties ctx, int id, String trxName) {
        super(ctx, id, trxName);
    }
    public MPayrollComponent(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }

    public boolean isTaxable() {
        return "Y".equals(get_ValueAsString("IsTaxable"));
    }

    public boolean isBPJSBase() {
        return "Y".equals(get_ValueAsString("IsBPJSBase"));
    }

    public boolean isEarning() {
        return COMPONENTTYPE_EARNING.equals(getComponentType());
    }

    /**
     * Load semua komponen aktif, diurutkan SeqNo — dipakai UI input HR
     * (dropdown pilih komponen saat isi X_Payroll_ComponentInput) dan
     * validasi di GeneratePayrollRun kalau perlu cross-check komponen
     * yang benar-benar terdaftar vs yang di-input.
     */
    public static List<MPayrollComponent> getActiveComponents(String trxName) {
        List<MPayrollComponent> list = new ArrayList<>();
        String sql = "SELECT X_Payroll_Component_ID FROM X_Payroll_Component " +
            "WHERE IsActive='Y' ORDER BY SeqNo, Name";
        try (PreparedStatement pstmt = DB.prepareStatement(sql, trxName);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                list.add(new MPayrollComponent(Env.getCtx(), rs.getInt(1), trxName));
            }
        } catch (Exception e) {
            throw new RuntimeException("Gagal load X_Payroll_Component aktif: " + e.getMessage(), e);
        }
        return list;
    }

    public static MPayrollComponent get(int id, String trxName) {
        return new MPayrollComponent(Env.getCtx(), id, trxName);
    }
}
