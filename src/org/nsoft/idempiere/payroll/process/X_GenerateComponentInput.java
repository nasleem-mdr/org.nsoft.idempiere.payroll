package org.nsoft.idempiere.payroll.process;

import org.compiere.process.SvrProcess;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.util.DB;
import org.compiere.util.Env;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

/**
 * AD_Process: X_GenerateComponentInput
 * Parameter: X_Payroll_Period_ID (mandatory)
 *
 * Menyalin komponen gaji TETAP dari kontrak aktif tiap employee (via
 * X_HR_ContractComponent) menjadi X_Payroll_ComponentInput untuk periode
 * ini. TIDAK menyentuh komponen variabel (lembur, bonus manual) — itu
 * tetap diisi HR terpisah SETELAH proses ini jalan, lewat Window 2
 * ("Payroll Period Input") secara manual.
 *
 * IDEMPOTENT secara sengaja TIDAK — proses ini boleh dijalankan ulang
 * (misal kontrak direvisi setelah generate pertama), tapi akan SKIP
 * kombinasi employee+component yang sudah ada barisnya di periode ini
 * (tidak menimpa, supaya perubahan manual HR — misal override sementara
 * gaji pokok — tidak tertimpa diam-diam oleh re-run).
 */
public class X_GenerateComponentInput extends SvrProcess {

    private int p_PayrollPeriodID = 0;

    @Override
    protected void prepare() {
        for (ProcessInfoParameter para : getParameter()) {
            if (para.getParameterName().equals("X_Payroll_Period_ID")) {
                p_PayrollPeriodID = para.getParameterAsInt();
            }
        }
        if (p_PayrollPeriodID <= 0) {
            throw new IllegalArgumentException("Payroll Period wajib diisi.");
        }
    }

    @Override
    protected String doIt() throws Exception {
        String trxName = get_TrxName();
        int adClientId = Env.getAD_Client_ID(getCtx());
        int adOrgId = Env.getAD_Org_ID(getCtx());
        int adUserId = Env.getAD_User_ID(getCtx());

        Timestamp periodDateFrom = DB.getSQLValueTSEx(trxName,
            "SELECT DateFrom FROM X_Payroll_Period WHERE X_Payroll_Period_ID=?", p_PayrollPeriodID);
        Timestamp periodDateTo = DB.getSQLValueTSEx(trxName,
            "SELECT DateTo FROM X_Payroll_Period WHERE X_Payroll_Period_ID=?", p_PayrollPeriodID);

        // ── Ambil semua HR_Contract yang aktif & valid mencakup periode ini ──
        // Asumsi HR_Contract punya kolom StartDate/EndDate & HR_Employee_ID
        // — sesuaikan nama kolom kalau beda di instance kamu.
        String contractSql =
            "SELECT c.HR_Contract_ID, c.HR_Employee_ID FROM HR_Contract c " +
            "WHERE c.IsActive='Y' " +
            "AND c.StartDate <= ? " +
            "AND (c.EndDate IS NULL OR c.EndDate >= ?)";

        int generatedCount = 0;
        int skippedCount = 0;

        try (PreparedStatement pstmt = DB.prepareStatement(contractSql, trxName)) {
            pstmt.setTimestamp(1, periodDateTo);
            pstmt.setTimestamp(2, periodDateFrom);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int contractId = rs.getInt("HR_Contract_ID");
                    int employeeId = rs.getInt("HR_Employee_ID");

                    // ── Ambil breakdown komponen kontrak ini ────────────
                    String componentSql =
                        "SELECT X_Payroll_Component_ID, Amount FROM X_HR_ContractComponent " +
                        "WHERE HR_Contract_ID=? AND IsActive='Y'";
                    try (PreparedStatement cStmt = DB.prepareStatement(componentSql, trxName)) {
                        cStmt.setInt(1, contractId);
                        try (ResultSet cRs = cStmt.executeQuery()) {
                            while (cRs.next()) {
                                int componentId = cRs.getInt("X_Payroll_Component_ID");
                                java.math.BigDecimal amount = cRs.getBigDecimal("Amount");

                                // ── Skip kalau sudah ada baris untuk kombinasi
                                //    employee+component+period ini (idempotent
                                //    per-baris, bukan hapus-generate-ulang) ──
                                int existing = DB.getSQLValueEx(trxName,
                                    "SELECT COUNT(*) FROM X_Payroll_ComponentInput " +
                                    "WHERE HR_Employee_ID=? AND X_Payroll_Period_ID=? " +
                                    "AND X_Payroll_Component_ID=?",
                                    employeeId, p_PayrollPeriodID, componentId);

                                if (existing > 0) {
                                    skippedCount++;
                                    continue;
                                }

                                int inputId = DB.getSQLValueEx(trxName,
                                    "SELECT nextval('X_Payroll_ComponentInput_seq')");
                                DB.executeUpdateEx(
                                    "INSERT INTO X_Payroll_ComponentInput " +
                                    "(X_Payroll_ComponentInput_ID, AD_Client_ID, AD_Org_ID, IsActive, " +
                                    " Created, CreatedBy, Updated, UpdatedBy, " +
                                    " HR_Employee_ID, X_Payroll_Period_ID, X_Payroll_Component_ID, Amount) " +
                                    "VALUES (?, ?, ?, 'Y', now(), ?, now(), ?, ?, ?, ?, ?)",
                                    new Object[]{
                                        inputId, adClientId, adOrgId, adUserId, adUserId,
                                        employeeId, p_PayrollPeriodID, componentId, amount
                                    },
                                    trxName
                                );
                                generatedCount++;
                            }
                        }
                    }
                }
            }
        }

        return "@OK@ - " + generatedCount + " ComponentInput ter-generate dari kontrak, " +
            skippedCount + " di-skip (sudah ada / kemungkinan diedit manual sebelumnya).";
    }
}
