package org.nsoft.idempiere.payroll.model.contract;

import org.compiere.util.DB;
import org.compiere.util.Env;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Breakdown komponen gaji TETAP per kontrak (Gaji Pokok, Tunjangan
 * Transport, dst) — sumber "template" yang disalin ke
 * X_Payroll_ComponentInput oleh X_GenerateComponentInput setiap kali
 * periode baru diproses. HR mengisi ini SEKALI saat kontrak dibuat/
 * direvisi, bukan tiap bulan.
 *
 * ⚠️ CEK NAMA CLASS SEBELUM COMPILE: sesuai pola yang sudah ketahuan
 * di X_Payroll_Bpjs_Rate (bukan X_Payroll_BPJS_Rate), kemungkinan besar
 * hasil generate GUI untuk tabel ini adalah X_X_Hr_ContractComponent
 * (huruf "Hr" bukan "HR") — BUKAN X_X_HR_ContractComponent seperti yang
 * dipakai di baris "extends" bawah ini. Buka file X_X_*.java hasil
 * generate, cocokkan nama class-nya persis, baru sesuaikan baris
 * "extends" kalau perlu.
 */
public class MHRContractComponent extends X_X_HR_ContractComponent {

    public MHRContractComponent(Properties ctx, int id, String trxName) {
        super(ctx, id, trxName);
    }
    public MHRContractComponent(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }

    /**
     * DTO ringan hasil JOIN ke X_Payroll_Component — dipakai
     * X_GenerateComponentInput supaya tidak perlu query 2 tahap
     * (component lalu cek flag Taxable/BPJSBase terpisah).
     */
    public static class ResolvedContractComponent {
        public final int componentId;
        public final BigDecimal amount;

        public ResolvedContractComponent(int componentId, BigDecimal amount) {
            this.componentId = componentId;
            this.amount = amount;
        }
    }

    /**
     * Load semua komponen aktif untuk satu kontrak — dipanggil
     * X_GenerateComponentInput per kontrak yang lolos filter tanggal.
     */
    public static List<ResolvedContractComponent> getComponentsForContract(int contractId, String trxName) {
        List<ResolvedContractComponent> list = new ArrayList<>();
        String sql = "SELECT X_Payroll_Component_ID, Amount FROM X_HR_ContractComponent " +
            "WHERE HR_Contract_ID=? AND IsActive='Y'";
        try (PreparedStatement pstmt = DB.prepareStatement(sql, trxName)) {
            pstmt.setInt(1, contractId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new ResolvedContractComponent(
                        rs.getInt("X_Payroll_Component_ID"),
                        rs.getBigDecimal("Amount")
                    ));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(
                "Gagal load X_HR_ContractComponent untuk kontrak " + contractId + ": " + e.getMessage(), e);
        }
        return list;
    }

    /**
     * Cek apakah kontrak ini punya breakdown komponen sama sekali —
     * berguna untuk validasi/laporan "kontrak tanpa breakdown gaji"
     * sebelum generate, supaya HR sadar ada kontrak yang belum lengkap
     * datanya, bukan diam-diam menghasilkan 0 baris ComponentInput.
     */
    public static boolean hasAnyComponent(int contractId, String trxName) {
        int count = DB.getSQLValueEx(trxName,
            "SELECT COUNT(*) FROM X_HR_ContractComponent WHERE HR_Contract_ID=? AND IsActive='Y'",
            contractId);
        return count > 0;
    }

    public static MHRContractComponent get(int id, String trxName) {
        return new MHRContractComponent(Env.getCtx(), id, trxName);
    }
}
