-- src/db/postgresql/202608300003_X_Payroll_Tax_Tables.sql

-- ── X_Payroll_TaxRate — generalisasi dari X_Payroll_TER_Rate ──────────
-- SchemeType menentukan bagaimana baris ini dibaca:
--   'TER'         → Category diisi (A/B/C), IncomeFrom/IncomeTo = bracket TER bulanan
--   'PROGRESSIVE' → Category NULL, IncomeFrom/IncomeTo = bracket tahunan Pasal 17
--                    (dipakai untuk rekonsiliasi Desember)
--   'PASAL26'     → Category NULL, IncomeFrom=0/IncomeTo=NULL, Rate = tarif flat
-- Nambah SchemeType baru = tidak sentuh kode Java, cukup insert baris +
-- pastikan ada Calculator yang menangani SchemeType itu (lihat catatan
-- di GeneratePayrollRun di bawah — masih butuh 1x kode per SchemeType
-- baru, TIDAK bisa 100% zero-code seperti BPJS karena rumus tiap skema
-- pajak beda struktur, bukan cuma beda rate/cap).
CREATE TABLE X_Payroll_TaxRate (
    X_Payroll_TaxRate_ID   NUMERIC(10)   NOT NULL,
    AD_Client_ID    NUMERIC(10)   NOT NULL,
    AD_Org_ID       NUMERIC(10)   NOT NULL,
    IsActive        CHAR(1)       DEFAULT 'Y' NOT NULL,
    Created         TIMESTAMP     DEFAULT now() NOT NULL,
    CreatedBy       NUMERIC(10)   NOT NULL,
    Updated         TIMESTAMP     DEFAULT now() NOT NULL,
    UpdatedBy       NUMERIC(10)   NOT NULL,
    SchemeType      VARCHAR(20)   NOT NULL,          -- TER / PROGRESSIVE / PASAL26 / ...
    Category        CHAR(1)       NULL,               -- A/B/C — hanya relevan untuk SchemeType='TER'
    IncomeFrom      NUMERIC       NOT NULL,
    IncomeTo        NUMERIC       NULL,
    Rate            NUMERIC       NOT NULL,
    ValidFrom       DATE          NOT NULL,
    ValidTo         DATE          NULL,
    CONSTRAINT X_Payroll_TaxRate_key PRIMARY KEY (X_Payroll_TaxRate_ID)
);
CREATE SEQUENCE X_Payroll_TaxRate_seq START WITH 1000000;

-- ── X_Payroll_EmployeeTaxProfile — override skema pajak per employee ──
-- Default (tanpa baris di sini) = SchemeType 'TER', HasNPWP 'Y',
-- TER_Category diambil dari HR_Employee.X_TER_Category (kolom custom
-- yang sudah kita tambahkan sebelumnya). Baris di tabel ini HANYA perlu
-- diisi untuk KASUS KHUSUS — karyawan asing Pasal 26, karyawan tanpa
-- NPWP, atau override kategori TER manual.
CREATE TABLE X_Payroll_EmployeeTaxProfile (
    X_Payroll_EmployeeTaxProfile_ID NUMERIC(10) NOT NULL,
    AD_Client_ID    NUMERIC(10)   NOT NULL,
    AD_Org_ID       NUMERIC(10)   NOT NULL,
    IsActive        CHAR(1)       DEFAULT 'Y' NOT NULL,
    Created         TIMESTAMP     DEFAULT now() NOT NULL,
    CreatedBy       NUMERIC(10)   NOT NULL,
    Updated         TIMESTAMP     DEFAULT now() NOT NULL,
    UpdatedBy       NUMERIC(10)   NOT NULL,
    HR_Employee_ID  NUMERIC(10)   NOT NULL,
    SchemeType      VARCHAR(20)   DEFAULT 'TER' NOT NULL,
    HasNPWP         CHAR(1)       DEFAULT 'Y' NOT NULL,  -- 'N' → surcharge diterapkan
    TER_CategoryOverride CHAR(1)  NULL,                  -- NULL = pakai HR_Employee.X_TER_Category
    ValidFrom       DATE          NOT NULL,
    ValidTo         DATE          NULL,
    Reason          VARCHAR(255)  NULL,                  -- WAJIB diisi kalau bukan default TER — audit trail
    CONSTRAINT X_Payroll_EmployeeTaxProfile_key PRIMARY KEY (X_Payroll_EmployeeTaxProfile_ID),
    CONSTRAINT X_Payroll_EmployeeTaxProfile_Employee FOREIGN KEY (HR_Employee_ID)
        REFERENCES HR_Employee (HR_Employee_ID)
);
CREATE SEQUENCE X_Payroll_EmployeeTaxProfile_seq START WITH 1000000;
