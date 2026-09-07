-- ── 5. X_Payroll_RunLine — HEADER per employee per run, TETAP RAMPING ──
-- Cuma simpan ringkasan yang sifatnya SATU nilai per employee (bukan
-- per-program): gross income, kategori TER (atribut tunggal, bukan
-- multi-row seperti BPJS), dan total akhir untuk query cepat/laporan
-- tanpa perlu JOIN+SUM tiap kali nampilin daftar payroll run.
CREATE TABLE X_Payroll_RunLine (
    X_Payroll_RunLine_ID   NUMERIC(10)   NOT NULL,
    AD_Client_ID    NUMERIC(10)   NOT NULL,
    AD_Org_ID       NUMERIC(10)   NOT NULL,
    IsActive        CHAR(1)       DEFAULT 'Y' NOT NULL,
    Created         TIMESTAMP     DEFAULT now() NOT NULL,
    CreatedBy       NUMERIC(10)   NOT NULL,
    Updated         TIMESTAMP     DEFAULT now() NOT NULL,
    UpdatedBy       NUMERIC(10)   NOT NULL,
    X_Payroll_Run_ID   NUMERIC(10) NOT NULL,
    HR_Employee_ID  NUMERIC(10)   NOT NULL,
    GrossIncome     NUMERIC       DEFAULT 0 NOT NULL,
    TER_Category    CHAR(1)       NULL,             -- 'A'/'B'/'C' — atribut tunggal, wajar di header
    TER_RateApplied NUMERIC       NULL,
    PPh21_Amount    NUMERIC       DEFAULT 0 NOT NULL,  -- tetap di header — PPh21 selalu 1 nilai/employee
    TotalDeduction  NUMERIC       DEFAULT 0 NOT NULL,  -- kolom ringkasan, di-generate dari SUM detail saat insert
    NetIncome       NUMERIC       DEFAULT 0 NOT NULL,
    CONSTRAINT X_Payroll_RunLine_key PRIMARY KEY (X_Payroll_RunLine_ID),
    CONSTRAINT X_Payroll_RunLine_Run FOREIGN KEY (X_Payroll_Run_ID)
        REFERENCES X_Payroll_Run (X_Payroll_Run_ID),
    CONSTRAINT X_Payroll_RunLine_Employee FOREIGN KEY (HR_Employee_ID)
        REFERENCES HR_Employee (HR_Employee_ID)
);

-- ── 6. X_Payroll_RunLineDetail — 1 baris = 1 program per employee ──────
-- Generik: ProgramType SAMA persis dengan ProgramType di X_Payroll_BPJS_Rate,
-- jadi bisa langsung ditrace balik "rate mana yang dipakai saat itu" untuk
-- audit (PMK/BPJS rate berubah dari waktu ke waktu — detail ini adalah
-- snapshot histori, bukan referensi live yang bisa berubah diam-diam).
-- Tambah program baru (mis. JKP) = INSERT baris X_Payroll_BPJS_Rate baru,
-- TIDAK perlu ALTER TABLE atau redeploy plugin sama sekali.
CREATE TABLE X_Payroll_RunLineDetail (
    X_Payroll_RunLineDetail_ID NUMERIC(10)  NOT NULL,
    AD_Client_ID    NUMERIC(10)   NOT NULL,
    AD_Org_ID       NUMERIC(10)   NOT NULL,
    IsActive        CHAR(1)       DEFAULT 'Y' NOT NULL,
    Created         TIMESTAMP     DEFAULT now() NOT NULL,
    CreatedBy       NUMERIC(10)   NOT NULL,
    Updated         TIMESTAMP     DEFAULT now() NOT NULL,
    UpdatedBy       NUMERIC(10)   NOT NULL,
    X_Payroll_RunLine_ID    NUMERIC(10) NOT NULL,
    ProgramType     VARCHAR(20)   NOT NULL,          -- KESEHATAN/JHT/JP/JKK/JKM/JKP/... (bebas nambah)
    WageBase        NUMERIC       NOT NULL,           -- basis upah SETELAH capping — snapshot, bukan hitung ulang
    EmployeeRateApplied NUMERIC   DEFAULT 0 NOT NULL, -- snapshot rate saat itu (audit trail)
    EmployerRateApplied NUMERIC   DEFAULT 0 NOT NULL,
    EmployeeAmount  NUMERIC       DEFAULT 0 NOT NULL,
    EmployerAmount  NUMERIC       DEFAULT 0 NOT NULL,
    X_Payroll_BPJS_Rate_ID NUMERIC(10) NULL,          -- FK opsional ke rate yang dipakai, untuk trace penuh
    CONSTRAINT X_Payroll_RunLineDetail_key PRIMARY KEY (X_Payroll_RunLineDetail_ID),
    CONSTRAINT X_Payroll_RunLineDetail_RunLine FOREIGN KEY (X_Payroll_RunLine_ID)
        REFERENCES X_Payroll_RunLine (X_Payroll_RunLine_ID),
    CONSTRAINT X_Payroll_RunLineDetail_Rate FOREIGN KEY (X_Payroll_BPJS_Rate_ID)
        REFERENCES X_Payroll_BPJS_Rate (X_Payroll_BPJS_Rate_ID)
);

CREATE SEQUENCE X_Payroll_RunLineDetail_seq START WITH 1000000;

-- src/db/postgresql/202608300001_X_Payroll_Tables.sql
-- ═══════════════════════════════════════════════════════════════════════
-- Payroll Module — TER (PMK 168/2023) & BPJS compatible
-- ═══════════════════════════════════════════════════════════════════════

-- ── 1. X_Payroll_TER_Rate ────────────────────────────────────────────
CREATE TABLE X_Payroll_TER_Rate (
    X_Payroll_TER_Rate_ID  NUMERIC(10)   NOT NULL,
    AD_Client_ID    NUMERIC(10)   NOT NULL,
    AD_Org_ID       NUMERIC(10)   NOT NULL,
    IsActive        CHAR(1)       DEFAULT 'Y' NOT NULL,
    Created         TIMESTAMP     DEFAULT now() NOT NULL,
    CreatedBy       NUMERIC(10)   NOT NULL,
    Updated         TIMESTAMP     DEFAULT now() NOT NULL,
    UpdatedBy       NUMERIC(10)   NOT NULL,
    TER_Category    CHAR(1)       NOT NULL,           -- 'A' / 'B' / 'C'
    IncomeFrom      NUMERIC       NOT NULL,
    IncomeTo        NUMERIC       NULL,                -- NULL = tidak terbatas (bracket teratas)
    Rate            NUMERIC       NOT NULL,             -- persentase, mis. 0.05 = 5%
    ValidFrom       DATE          NOT NULL,
    ValidTo         DATE          NULL,
    CONSTRAINT X_Payroll_TER_Rate_key PRIMARY KEY (X_Payroll_TER_Rate_ID)
);

-- ── 2. X_Payroll_BPJS_Rate ───────────────────────────────────────────
CREATE TABLE X_Payroll_BPJS_Rate (
    X_Payroll_BPJS_Rate_ID  NUMERIC(10)   NOT NULL,
    AD_Client_ID    NUMERIC(10)   NOT NULL,
    AD_Org_ID       NUMERIC(10)   NOT NULL,
    IsActive        CHAR(1)       DEFAULT 'Y' NOT NULL,
    Created         TIMESTAMP     DEFAULT now() NOT NULL,
    CreatedBy       NUMERIC(10)   NOT NULL,
    Updated         TIMESTAMP     DEFAULT now() NOT NULL,
    UpdatedBy       NUMERIC(10)   NOT NULL,
    ProgramType     VARCHAR(20)   NOT NULL,             -- KESEHATAN/JHT/JP/JKK/JKM
    EmployeeRate    NUMERIC       DEFAULT 0 NOT NULL,
    EmployerRate    NUMERIC       DEFAULT 0 NOT NULL,
    WageCapLower    NUMERIC       NULL,
    WageCapUpper    NUMERIC       NULL,
    ValidFrom       DATE          NOT NULL,
    ValidTo         DATE          NULL,
    CONSTRAINT X_Payroll_BPJS_Rate_key PRIMARY KEY (X_Payroll_BPJS_Rate_ID)
);

-- ── 3. X_Payroll_Period ──────────────────────────────────────────────
CREATE TABLE X_Payroll_Period (
    X_Payroll_Period_ID    NUMERIC(10)   NOT NULL,
    AD_Client_ID    NUMERIC(10)   NOT NULL,
    AD_Org_ID       NUMERIC(10)   NOT NULL,
    IsActive        CHAR(1)       DEFAULT 'Y' NOT NULL,
    Created         TIMESTAMP     DEFAULT now() NOT NULL,
    CreatedBy       NUMERIC(10)   NOT NULL,
    Updated         TIMESTAMP     DEFAULT now() NOT NULL,
    UpdatedBy       NUMERIC(10)   NOT NULL,
    PeriodName      VARCHAR(20)   NOT NULL,
    DateFrom        DATE          NOT NULL,
    DateTo          DATE          NOT NULL,
    IsDecemberReconciliation CHAR(1) DEFAULT 'N' NOT NULL,
    DocStatus       VARCHAR(2)    DEFAULT 'DR' NOT NULL,
    CONSTRAINT X_Payroll_Period_key PRIMARY KEY (X_Payroll_Period_ID)
);

-- ── 4. X_Payroll_Run ─────────────────────────────────────────────────
CREATE TABLE X_Payroll_Run (
    X_Payroll_Run_ID   NUMERIC(10)   NOT NULL,
    AD_Client_ID    NUMERIC(10)   NOT NULL,
    AD_Org_ID       NUMERIC(10)   NOT NULL,
    IsActive        CHAR(1)       DEFAULT 'Y' NOT NULL,
    Created         TIMESTAMP     DEFAULT now() NOT NULL,
    CreatedBy       NUMERIC(10)   NOT NULL,
    Updated         TIMESTAMP     DEFAULT now() NOT NULL,
    UpdatedBy       NUMERIC(10)   NOT NULL,
    X_Payroll_Period_ID NUMERIC(10) NOT NULL,
    ProcessedDate   TIMESTAMP     NULL,
    DocStatus       VARCHAR(2)    DEFAULT 'DR' NOT NULL,
    CONSTRAINT X_Payroll_Run_key PRIMARY KEY (X_Payroll_Run_ID),
    CONSTRAINT X_Payroll_Run_Period FOREIGN KEY (X_Payroll_Period_ID)
        REFERENCES X_Payroll_Period (X_Payroll_Period_ID)
);

-- ── 5. X_Payroll_RunLine ─────────────────────────────────────────────
CREATE TABLE X_Payroll_RunLine (
    X_Payroll_RunLine_ID   NUMERIC(10)   NOT NULL,
    AD_Client_ID    NUMERIC(10)   NOT NULL,
    AD_Org_ID       NUMERIC(10)   NOT NULL,
    IsActive        CHAR(1)       DEFAULT 'Y' NOT NULL,
    Created         TIMESTAMP     DEFAULT now() NOT NULL,
    CreatedBy       NUMERIC(10)   NOT NULL,
    Updated         TIMESTAMP     DEFAULT now() NOT NULL,
    UpdatedBy       NUMERIC(10)   NOT NULL,
    X_Payroll_Run_ID   NUMERIC(10) NOT NULL,
    HR_Employee_ID  NUMERIC(10)   NOT NULL,
    GrossIncome     NUMERIC       DEFAULT 0 NOT NULL,
    TER_Category    CHAR(1)       NULL,
    TER_RateApplied NUMERIC       NULL,
    PPh21_Amount    NUMERIC       DEFAULT 0 NOT NULL,
    BPJSKesehatan_Employee NUMERIC DEFAULT 0 NOT NULL,
    BPJSKesehatan_Employer NUMERIC DEFAULT 0 NOT NULL,
    BPJS_JHT_Employee      NUMERIC DEFAULT 0 NOT NULL,
    BPJS_JHT_Employer      NUMERIC DEFAULT 0 NOT NULL,
    BPJS_JP_Employee       NUMERIC DEFAULT 0 NOT NULL,
    BPJS_JP_Employer       NUMERIC DEFAULT 0 NOT NULL,
    BPJS_JKK_Employer      NUMERIC DEFAULT 0 NOT NULL,
    BPJS_JKM_Employer      NUMERIC DEFAULT 0 NOT NULL,
    TotalDeduction  NUMERIC       DEFAULT 0 NOT NULL,
    NetIncome       NUMERIC       DEFAULT 0 NOT NULL,
    CONSTRAINT X_Payroll_RunLine_key PRIMARY KEY (X_Payroll_RunLine_ID),
    CONSTRAINT X_Payroll_RunLine_Run FOREIGN KEY (X_Payroll_Run_ID)
        REFERENCES X_Payroll_Run (X_Payroll_Run_ID),
    CONSTRAINT X_Payroll_RunLine_Employee FOREIGN KEY (HR_Employee_ID)
        REFERENCES HR_Employee (HR_Employee_ID)
);

-- ═══════════════════════════════════════════════════════════════════════
-- AD Dictionary registration — WAJIB supaya generic model REST & PO
-- framework Java mengenali tabel ini. Pola: insert ke AD_Table lalu
-- AD_Column untuk tiap kolom, plus sequence.
-- Ini bagian paling verbose — di iDempiere REAL workflow, cara yang jauh
-- lebih cepat & tidak error-prone adalah:
--   1) Buat tabel ini via GUI "Table and Column" (isi nama tabel, lalu
--      klik "Create Columns From DB" setelah CREATE TABLE fisik di atas
--      dijalankan manual dulu di DB)
--   2) Export hasil AD_Table/AD_Column jadi 2Pack XML
--   3) 2Pack XML itu yang di-commit ke migration/plugin, BUKAN insert SQL
--      manual ke AD_Table (rawan salah AD_Reference_ID, salah sequence,
--      dsb yang GUI otomatis handle dengan benar)
-- ═══════════════════════════════════════════════════════════════════════
-- → Saran: jalankan CREATE TABLE fisik di atas dulu di DB, lalu ikuti
--   langkah GUI di atas. Saya TIDAK sertakan INSERT INTO AD_Table/AD_Column
--   manual di sini karena risiko human-error tinggi & sudah ada tooling
--   resmi iDempiere untuk itu (Synchronize Column dari GUI).

-- Sequence untuk tiap tabel (generic model REST butuh ini untuk auto-ID)
CREATE SEQUENCE X_Payroll_TER_Rate_seq START WITH 1000000;
CREATE SEQUENCE X_Payroll_BPJS_Rate_seq START WITH 1000000;
CREATE SEQUENCE X_Payroll_Period_seq START WITH 1000000;
CREATE SEQUENCE X_Payroll_Run_seq START WITH 1000000;
CREATE SEQUENCE X_Payroll_RunLine_seq START WITH 1000000;
