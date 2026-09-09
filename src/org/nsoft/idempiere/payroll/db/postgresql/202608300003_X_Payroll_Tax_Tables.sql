═══════════════════════════════════════════════════════════════════════
-- BAGIAN 1 — REFERENSI TARIF (tidak bergantung tabel lain)
-- Semua rate/bracket di sini WAJIB bertanggal-valid (ValidFrom/ValidTo)
-- karena PMK dan rate BPJS berubah dari waktu ke waktu — update rate =
-- insert baris baru, BUKAN redeploy plugin atau UPDATE baris lama.
-- ═══════════════════════════════════════════════════════════════════════

-- ── 1.1 X_Payroll_TaxRate — rate pajak generik, menggantikan X_Payroll_TER_Rate ──
-- SchemeType menentukan cara baca baris ini:
--   'TER'         → Category diisi (A/B/C), IncomeFrom/IncomeTo = bracket bulanan
--   'PROGRESSIVE' → Category NULL, IncomeFrom/IncomeTo = bracket TAHUNAN Pasal 17
--                    (dipakai KHUSUS rekonsiliasi Desember, wajib per PMK 168)
--   'PASAL26'     → Category NULL, IncomeFrom=0/IncomeTo=NULL, Rate = tarif flat
-- Nambah SchemeType baru = insert baris baru + tambah handling di
-- GeneratePayrollRun.java (masih butuh sedikit kode per SchemeType baru,
-- beda dengan BPJS yang 100% zero-code karena rumus tiap skema pajak
-- beda struktur, bukan cuma beda rate/cap).
CREATE TABLE X_Payroll_TaxRate (
    X_Payroll_TaxRate_ID   NUMERIC(10)   NOT NULL,
    AD_Client_ID    NUMERIC(10)   NOT NULL,
    AD_Org_ID       NUMERIC(10)   NOT NULL,
    IsActive        CHAR(1)       DEFAULT 'Y' NOT NULL,
    Created         TIMESTAMP     DEFAULT now() NOT NULL,
    CreatedBy       NUMERIC(10)   NOT NULL,
    Updated         TIMESTAMP     DEFAULT now() NOT NULL,
    UpdatedBy       NUMERIC(10)   NOT NULL,
    SchemeType      VARCHAR(20)   NOT NULL,
    Category        CHAR(1)       NULL,
    IncomeFrom      NUMERIC       NOT NULL,
    IncomeTo        NUMERIC       NULL,
    Rate            NUMERIC       NOT NULL,
    ValidFrom       DATE          NOT NULL,
    ValidTo         DATE          NULL,
    CONSTRAINT X_Payroll_TaxRate_key PRIMARY KEY (X_Payroll_TaxRate_ID)
);
CREATE SEQUENCE X_Payroll_TaxRate_seq START WITH 1000000;

-- ── 1.2 X_Payroll_PTKP_Rate — lookup PTKP tahunan by status ──────────
-- Lookup 1-ke-1 by kode status (TK/0, K/0, K/1, dst), bukan range income
-- seperti TER/Progresif. Dipakai khusus rekonsiliasi Desember.
CREATE TABLE X_Payroll_PTKP_Rate (
    X_Payroll_PTKP_Rate_ID  NUMERIC(10)  NOT NULL,
    AD_Client_ID    NUMERIC(10)   NOT NULL,
    AD_Org_ID       NUMERIC(10)   NOT NULL,
    IsActive        CHAR(1)       DEFAULT 'Y' NOT NULL,
    Created         TIMESTAMP     DEFAULT now() NOT NULL,
    CreatedBy       NUMERIC(10)   NOT NULL,
    Updated         TIMESTAMP     DEFAULT now() NOT NULL,
    UpdatedBy       NUMERIC(10)   NOT NULL,
    PTKPStatus      VARCHAR(10)   NOT NULL,
    AnnualAmount    NUMERIC       NOT NULL,
    ValidFrom       DATE          NOT NULL,
    ValidTo         DATE          NULL,
    CONSTRAINT X_Payroll_PTKP_Rate_key PRIMARY KEY (X_Payroll_PTKP_Rate_ID)
);
CREATE SEQUENCE X_Payroll_PTKP_Rate_seq START WITH 1000000;

-- ── 1.3 X_Payroll_BPJS_Rate — rate & cap BPJS per program ────────────
-- IsEmployerContributionTaxable: 'Y' untuk Kesehatan/JKM/JKK (iuran
-- perusahaan = natura, menambah basis PPh21 karyawan), 'N' untuk JHT/JP
-- (iuran perusahaan BUKAN objek pajak).
CREATE TABLE X_Payroll_BPJS_Rate (
    X_Payroll_BPJS_Rate_ID  NUMERIC(10)   NOT NULL,
    AD_Client_ID    NUMERIC(10)   NOT NULL,
    AD_Org_ID       NUMERIC(10)   NOT NULL,
    IsActive        CHAR(1)       DEFAULT 'Y' NOT NULL,
    Created         TIMESTAMP     DEFAULT now() NOT NULL,
    CreatedBy       NUMERIC(10)   NOT NULL,
    Updated         TIMESTAMP     DEFAULT now() NOT NULL,
    UpdatedBy       NUMERIC(10)   NOT NULL,
    ProgramType     VARCHAR(20)   NOT NULL,
    EmployeeRate    NUMERIC       DEFAULT 0 NOT NULL,
    EmployerRate    NUMERIC       DEFAULT 0 NOT NULL,
    WageCapLower    NUMERIC       NULL,
    WageCapUpper    NUMERIC       NULL,
    IsEmployerContributionTaxable CHAR(1) DEFAULT 'N' NOT NULL,
    ValidFrom       DATE          NOT NULL,
    ValidTo         DATE          NULL,
    CONSTRAINT X_Payroll_BPJS_Rate_key PRIMARY KEY (X_Payroll_BPJS_Rate_ID)
);
CREATE SEQUENCE X_Payroll_BPJS_Rate_seq START WITH 1000000;

-- ── 1.4 X_Payroll_Component — master komponen gaji ───────────────────
-- IsTaxable  = masuk basis PPh21 (TaxableGrossIncome)?
-- IsBPJSBase = masuk basis perhitungan BPJS?
-- ComponentType: EARNING (Gaji Pokok, Tunjangan) / DEDUCTION (potongan lain)
CREATE TABLE X_Payroll_Component (
    X_Payroll_Component_ID NUMERIC(10)  NOT NULL,
    AD_Client_ID    NUMERIC(10)   NOT NULL,
    AD_Org_ID       NUMERIC(10)   NOT NULL,
    IsActive        CHAR(1)       DEFAULT 'Y' NOT NULL,
    Created         TIMESTAMP     DEFAULT now() NOT NULL,
    CreatedBy       NUMERIC(10)   NOT NULL,
    Updated         TIMESTAMP     DEFAULT now() NOT NULL,
    UpdatedBy       NUMERIC(10)   NOT NULL,
    Name            VARCHAR(60)   NOT NULL,
    ComponentType   VARCHAR(10)   NOT NULL,
    IsTaxable       CHAR(1)       DEFAULT 'Y' NOT NULL,
    IsBPJSBase      CHAR(1)       DEFAULT 'Y' NOT NULL,
    SeqNo           NUMERIC       DEFAULT 10,
    CONSTRAINT X_Payroll_Component_key PRIMARY KEY (X_Payroll_Component_ID)
);
CREATE SEQUENCE X_Payroll_Component_seq START WITH 1000000;

