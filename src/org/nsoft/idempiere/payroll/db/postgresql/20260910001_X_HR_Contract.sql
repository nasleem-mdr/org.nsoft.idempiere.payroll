-- Breakdown komponen gaji TETAP per kontrak — sumber "template" yang
-- di-generate ulang ke X_Payroll_ComponentInput tiap kali periode baru
-- dibuka. Terpisah dari HR_Contract (tabel core) supaya tidak perlu
-- modifikasi struktur tabel bawaan iDempiere.
CREATE TABLE X_HR_ContractComponent (
    X_HR_ContractComponent_ID NUMERIC(10) NOT NULL,
    AD_Client_ID    NUMERIC(10)   NOT NULL,
    AD_Org_ID       NUMERIC(10)   NOT NULL,
    IsActive        CHAR(1)       DEFAULT 'Y' NOT NULL,
    Created         TIMESTAMP     DEFAULT now() NOT NULL,
    CreatedBy       NUMERIC(10)   NOT NULL,
    Updated         TIMESTAMP     DEFAULT now() NOT NULL,
    UpdatedBy       NUMERIC(10)   NOT NULL,
    HR_Contract_ID          NUMERIC(10) NOT NULL,
    X_Payroll_Component_ID  NUMERIC(10) NOT NULL,
    Amount          NUMERIC       DEFAULT 0 NOT NULL,
    CONSTRAINT X_HR_ContractComponent_key PRIMARY KEY (X_HR_ContractComponent_ID),
    CONSTRAINT X_HR_ContractComponent_Contract FOREIGN KEY (HR_Contract_ID)
        REFERENCES HR_Contract (HR_Contract_ID),
    CONSTRAINT X_HR_ContractComponent_Component FOREIGN KEY (X_Payroll_Component_ID)
        REFERENCES X_Payroll_Component (X_Payroll_Component_ID)
);
CREATE SEQUENCE X_HR_ContractComponent_seq START WITH 1000000;
