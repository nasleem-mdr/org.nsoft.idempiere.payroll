package org.nsoft.idempiere.payroll.validator;

import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.model.X_AD_Client;

/**
 * Mencegah edit/delete manual pada X_Payroll_Run(Line) yang sudah CO/VO
 * lewat window biasa — SATU-SATUNYA jalan sah untuk "membatalkan" hasil
 * run adalah lewat X_VoidPayrollRun (yang mengubah status via SQL
 * terkontrol dalam SvrProcess, bukan lewat window edit user).
 *
 * Tanpa validator ini, integritas seluruh query kumulatif (Bonus+Reguler,
 * rekonsiliasi Desember) rentan rusak oleh 1 klik "Save" yang tidak
 * disengaja di window generic iDempiere.
 */
public class PayrollLockValidator implements org.compiere.model.ModelValidator {

    private int adClientId;

    @Override
    public void initialize(org.compiere.model.ModelValidationEngine engine, X_AD_Client client) {
        this.adClientId = (client != null) ? client.getAD_Client_ID() : 0;
        engine.addModelChange("X_Payroll_Run", this);
        engine.addModelChange("X_Payroll_RunLine", this);
        engine.addModelChange("X_Payroll_RunLineDetail", this);
        engine.addModelChange("X_Payroll_RunLineComponent", this);
    }

    @Override
    public int getAD_Client_ID() {
        return adClientId;
    }

    @Override
    public String modelChange(PO po, int type) throws Exception {
        // ── Kasus 1: X_Payroll_Run sendiri diedit/dihapus ────────────────
        if (po instanceof org.nsoft.idempiere.payroll.model.run.X_X_Payroll_Run) {
            String docStatus = (String) po.get_Value("DocStatus");
            boolean isLocked = "CO".equals(docStatus) || "VO".equals(docStatus);

            if (isLocked && (type == TYPE_BEFORE_CHANGE || type == TYPE_BEFORE_DELETE)) {
                // Kecualikan perubahan status YANG SAH — dari GeneratePayrollRun
                // (DR→CO) atau X_VoidPayrollRun (CO→VO). Deteksi lewat cek
                // apakah HANYA kolom status/void yang berubah, bukan data lain.
                // Untuk kesederhanaan & keamanan maksimal, validator ini
                // BLOKIR SEMUA perubahan pada record yang statusnya SUDAH
                // CO/VO — proses SvrProcess kita pakai DB.executeUpdateEx()
                // langsung (bypass PO.save()), jadi tidak akan pernah kena
                // validator ini sama sekali. Blokir di sini murni menyasar
                // edit MANUAL lewat window/generic model REST.
                if (type == TYPE_BEFORE_DELETE) {
                    return "Payroll Run yang sudah " + docStatus + " tidak boleh dihapus. " +
                        "Gunakan proses Void Payroll Run untuk membatalkan.";
                }
                return "Payroll Run yang sudah " + docStatus + " tidak boleh diedit manual. " +
                    "Gunakan proses Void Payroll Run kalau perlu koreksi.";
            }
        }

        // ── Kasus 2: RunLine/RunLineDetail/RunLineComponent diedit/dihapus ──
        // Ketiganya child dari Run — cek status Run induknya.
        else {
            Integer runLineParentRunId = resolveParentRunId(po);
            if (runLineParentRunId != null && (type == TYPE_BEFORE_CHANGE || type == TYPE_BEFORE_DELETE)) {
                String parentStatus = org.compiere.util.DB.getSQLValueStringEx(po.get_TrxName(),
                    "SELECT DocStatus FROM X_Payroll_Run WHERE X_Payroll_Run_ID=?",
                    runLineParentRunId);
                if ("CO".equals(parentStatus) || "VO".equals(parentStatus)) {
                    return "Data ini milik Payroll Run yang sudah " + parentStatus +
                        " — tidak boleh diedit/dihapus manual. Gunakan proses Void Payroll Run.";
                }
            }
        }

        return null; // null = boleh lanjut
    }

    /**
     * Cari X_Payroll_Run_ID dari record child (langsung atau lewat RunLine).
     * Disederhanakan — sesuaikan kalau nama kolom FK di hasil generate
     * GUI kamu berbeda dari asumsi ini.
     */
    private Integer resolveParentRunId(PO po) {
        String tableName = po.get_TableName();
        try {
            if ("X_Payroll_RunLine".equals(tableName)) {
                Object val = po.get_Value("X_Payroll_Run_ID");
                return val != null ? ((Number) val).intValue() : null;
            }
            if ("X_Payroll_RunLineDetail".equals(tableName) ||
                "X_Payroll_RunLineComponent".equals(tableName)) {
                Object runLineIdVal = po.get_Value("X_Payroll_RunLine_ID");
                if (runLineIdVal == null) return null;
                return org.compiere.util.DB.getSQLValueEx(po.get_TrxName(),
                    "SELECT X_Payroll_Run_ID FROM X_Payroll_RunLine WHERE X_Payroll_RunLine_ID=?",
                    ((Number) runLineIdVal).intValue());
            }
        } catch (Exception e) {
            // Jangan biarkan kegagalan resolusi ini membuat validator
            // meledak dan memblokir SEMUA transaksi lain di sistem —
            // fail-open di sini (bukan fail-closed) karena ini validator
            // pencegahan tambahan, bukan satu-satunya lapis proteksi
            // (SvrProcess sendiri sudah pakai DB.executeUpdateEx bukan
            // PO.save(), jadi tidak akan pernah lewat sini).
            return null;
        }
        return null;
    }

    @Override
    public String docValidate(PO po, int timing) {
        return null; // tidak dipakai — payroll tidak pakai DocAction standar iDempiere
    }
}
