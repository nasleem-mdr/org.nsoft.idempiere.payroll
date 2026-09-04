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
