package org.nsoft.idempiere.payroll.process;

import org.compiere.process.SvrProcess;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.util.DB;
import org.compiere.util.Env;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * AD_Process: X_GenerateComponentInput
 * Parameter: X_Payroll_Period_ID (mandatory)
 *
 * PENTING: HR_Contract TIDAK punya HR_Employee_ID langsung — link-nya
 * lewat C_BPartner_ID. Employee ditemukan via
 * HR_Employee.C_BPartner_ID = HR_Contract.C_BPartner_ID.
 *
 * HR_Contract tidak punya flag "kontrak aktif tunggal" — bisa ada lebih
 * dari satu kontrak untuk C_BPartner_ID yang sama (mis. per-project via
 * C_Project_ID). Kalau ditemukan >1 kontrak valid untuk BPartner yang
 * sama pada periode ini, employee tsb DI-SKIP dengan warning (bukan
 * dijumlah atau asal pilih satu) — ambiguitas seperti ini harus
 * diselesaikan manual oleh HR sebelum generate, bukan ditebak sistem.
 */
public class X_GenerateComponentInput extends SvrProcess {

    private static final Logger log = Logger.getLogger(X_GenerateComponentInput.class.getName());

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

        // ── Ambil (HR_Employee_ID, HR_Contract_ID) untuk kontrak yang
        //    valid mencakup periode ini, JOIN via C_BPartner_ID ────────
        String contractSql =
            "SELECT e.HR_Employee_ID, c.HR_Contract_ID, c.ValidFrom " +
            "FROM HR_Contract c " +
            "JOIN HR_Employee e ON e.C_BPartner_ID = c.C_BPartner_ID " +
            "WHERE c.IsActive='Y' AND e.IsActive='Y' " +
            "AND e.StartDate <= ? AND (e.EndDate IS NULL OR e.EndDate >= ?) " +
            "AND c.ValidFrom <= ? AND (c.ValidTo IS NULL OR c.ValidTo >= ?) " +
            "ORDER BY e.HR_Employee_ID, c.ValidFrom DESC";

        // ── Kelompokkan hasil per employee, deteksi duplikat kontrak ───
        java.util.Map<Integer, java.util.List<Integer>> contractsByEmployee = new java.util.LinkedHashMap<>();

        try (PreparedStatement pstmt = DB.prepareStatement(contractSql, trxName)) {
            pstmt.setTimestamp(1, periodDateTo);
            pstmt.setTimestamp(2, periodDateFrom);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int employeeId = rs.getInt("HR_Employee_ID");
                    int contractId = rs.getInt("HR_Contract_ID");
                    contractsByEmployee.computeIfAbsent(employeeId, k -> new java.util.ArrayList<>()).add(contractId);
                }
            }
        }

        int generatedCount = 0;
        int skippedExisting = 0;
        int skippedAmbiguous = 0;

        for (var entry : contractsByEmployee.entrySet()) {
            int employeeId = entry.getKey();
            java.util.List<Integer> contractIds = entry.getValue();

            if (contractIds.size() > 1) {
                log.log(Level.WARNING,
                    "Employee {0} punya {1} kontrak aktif yang valid untuk periode ini " +
                    "(HR_Contract_ID: {2}) — DI-SKIP karena ambigu. Nonaktifkan kontrak " +
                    "lama secara eksplisit (IsActive=N atau ValidTo diisi) sebelum generate ulang.",
                    new Object[]{ employeeId, contractIds.size(), contractIds });
                skippedAmbiguous++;
                continue;
            }

            int contractId = contractIds.get(0);

            String componentSql =
                "SELECT X_Payroll_Component_ID, Amount FROM X_HR_ContractComponent " +
                "WHERE HR_Contract_ID=? AND IsActive='Y'";
            try (PreparedStatement cStmt = DB.prepareStatement(componentSql, trxName)) {
                cStmt.setInt(1, contractId);
                try (ResultSet cRs = cStmt.executeQuery()) {
                    while (cRs.next()) {
                        int componentId = cRs.getInt("X_Payroll_Component_ID");
                        BigDecimal amount = cRs.getBigDecimal("Amount");

                        int existing = DB.getSQLValueEx(trxName,
                            "SELECT COUNT(*) FROM X_Payroll_ComponentInput " +
                            "WHERE HR_Employee_ID=? AND X_Payroll_Period_ID=? AND X_Payroll_Component_ID=?",
                            employeeId, p_PayrollPeriodID, componentId);

                        if (existing > 0) {
                            skippedExisting++;
                            continue;
                        }

                        int inputId = DB.getSQLValueEx(trxName, "SELECT nextval('X_Payroll_ComponentInput_seq')");
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

        return "@OK@ - " + generatedCount + " ComponentInput ter-generate. " +
            skippedExisting + " di-skip (sudah ada). " +
            skippedAmbiguous + " employee di-skip (kontrak ganda/ambigu, cek log).";
    }
}
