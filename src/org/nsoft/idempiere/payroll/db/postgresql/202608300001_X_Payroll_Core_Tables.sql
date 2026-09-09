-- ═══════════════════════════════════════════════════════════════════════
-- BAGIAN 2 — PERIODE & RUN (header transaksi)
-- ═══════════════════════════════════════════════════════════════════════

-- ── 2.1 X_Payroll_Period ──────────────────────────────────────────────
-- DocStatus di sini adalah status PERIODE (ditutup manual lewat proses
-- "Tutup Periode" setelah semua run selesai), BUKAN otomatis CO setelah
-- 1 run — karena satu periode boleh punya banyak run (REGULAR + BONUS).
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
CREATE SEQUENCE X_Payroll_Period_seq START WITH 1000000;

-- ── 2.2 X_Payroll_Run ─────────────────────────────────────────────────
-- RunType membedakan jenis slip dalam satu periode (satu Period bisa
-- punya banyak Run: REGULAR + BONUS + THR, diproses terpisah, masing2
-- menghasilkan slip sendiri, tapi PPh21 dihitung KUMULATIF lintas run
-- dalam periode yang sama — lihat X_Payroll_RunLine di bawah).
-- DocStatus: DR → CO → (opsional) VO.
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
    RunType         VARCHAR(20)   DEFAULT 'REGULAR' NOT NULL,  -- REGULAR/BONUS/THR/OFF_CYCLE
    ProcessedDate   TIMESTAMP     NULL,
    DocStatus       VARCHAR(2)    DEFAULT 'DR' NOT NULL,       -- DR/CO/VO
    VoidedDate      TIMESTAMP     NULL,
    VoidedBy        NUMERIC(10)   NULL,
    VoidReason      VARCHAR(255)  NULL,
    CONSTRAINT X_Payroll_Run_key PRIMARY KEY (X_Payroll_Run_ID),
    CONSTRAINT X_Payroll_Run_Period FOREIGN KEY (X_Payroll_Period_ID)
        REFERENCES X_Payroll_Period (X_Payroll_Period_ID)
);
CREATE SEQUENCE X_Payroll_Run_seq START WITH 1000000;


-- ═══════════════════════════════════════════════════════════════════════
-- BAGIAN 3 — OVERRIDE / ENROLLMENT PER EMPLOYEE
-- Bergantung ke HR_Employee (tabel core iDempiere).
-- Model OPT-OUT: tanpa baris di sini = perilaku default (ikut semua
-- program BPJS aktif, skema pajak TER standar). Baris HANYA diisi untuk
-- KASUS KHUSUS/PENGECUALIAN — supaya data yang perlu di-maintain sekecil
-- mungkin dan default selalu condong ke posisi paling aman secara hukum
-- (wajib ikut, bukan sebaliknya).
-- ═══════════════════════════════════════════════════════════════════════

-- ── 3.1 X_Payroll_EmployeeProgram — pengecualian enrollment BPJS ─────
CREATE TABLE X_Payroll_EmployeeProgram (
    X_Payroll_EmployeeProgram_ID  NUMERIC(10)  NOT NULL,
    AD_Client_ID    NUMERIC(10)   NOT NULL,
    AD_Org_ID       NUMERIC(10)   NOT NULL,
    IsActive        CHAR(1)       DEFAULT 'Y' NOT NULL,
    Created         TIMESTAMP     DEFAULT now() NOT NULL,
    CreatedBy       NUMERIC(10)   NOT NULL,
    Updated         TIMESTAMP     DEFAULT now() NOT NULL,
    UpdatedBy       NUMERIC(10)   NOT NULL,
    HR_Employee_ID  NUMERIC(10)   NOT NULL,
    ProgramType     VARCHAR(20)   NOT NULL,
    IsEnrolled      CHAR(1)       DEFAULT 'Y' NOT NULL,   -- 'N' = dikecualikan dari program ini
    ValidFrom       DATE          NOT NULL,
    ValidTo         DATE          NULL,
    Reason          VARCHAR(255)  NULL,                   -- WAJIB diisi kalau IsEnrolled='N'
    CONSTRAINT X_Payroll_EmployeeProgram_key PRIMARY KEY (X_Payroll_EmployeeProgram_ID),
    CONSTRAINT X_Payroll_EmployeeProgram_Employee FOREIGN KEY (HR_Employee_ID)
        REFERENCES HR_Employee (HR_Employee_ID)
);
CREATE SEQUENCE X_Payroll_EmployeeProgram_seq START WITH 1000000;

-- ── 3.2 X_Payroll_EmployeeTaxProfile — override skema pajak ─────────
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
    HasNPWP         CHAR(1)       DEFAULT 'Y' NOT NULL,     -- 'N' → surcharge diterapkan
    TER_CategoryOverride CHAR(1)  NULL,                     -- NULL = pakai HR_Employee.X_TER_Category
    ValidFrom       DATE          NOT NULL,
    ValidTo         DATE          NULL,
    Reason          VARCHAR(255)  NULL,                     -- WAJIB diisi kalau bukan default TER
    CONSTRAINT X_Payroll_EmployeeTaxProfile_key PRIMARY KEY (X_Payroll_EmployeeTaxProfile_ID),
    CONSTRAINT X_Payroll_EmployeeTaxProfile_Employee FOREIGN KEY (HR_Employee_ID)
        REFERENCES HR_Employee (HR_Employee_ID)
);
CREATE SEQUENCE X_Payroll_EmployeeTaxProfile_seq START WITH 1000000;

-- ── 3.3 X_Payroll_ComponentInput — input HR sebelum run diproses ────
-- Sumber data Gaji Pokok/Tunjangan per employee per periode, diisi HR
-- (manual atau default dari HR_Contract) SEBELUM tombol "Proses Payroll"
-- ditekan. Di-snapshot menjadi X_Payroll_RunLineComponent saat
-- GeneratePayrollRun dieksekusi — perubahan di sini SETELAH run
-- Complete TIDAK mempengaruhi run yang sudah jadi (snapshot immutable).
CREATE TABLE X_Payroll_ComponentInput (
    X_Payroll_ComponentInput_ID NUMERIC(10) NOT NULL,
    AD_Client_ID    NUMERIC(10)   NOT NULL,
    AD_Org_ID       NUMERIC(10)   NOT NULL,
    IsActive        CHAR(1)       DEFAULT 'Y' NOT NULL,
    Created         TIMESTAMP     DEFAULT now() NOT NULL,
    CreatedBy       NUMERIC(10)   NOT NULL,
    Updated         TIMESTAMP     DEFAULT now() NOT NULL,
    UpdatedBy       NUMERIC(10)   NOT NULL,
    HR_Employee_ID          NUMERIC(10) NOT NULL,
    X_Payroll_Period_ID     NUMERIC(10) NOT NULL,
    X_Payroll_Component_ID  NUMERIC(10) NOT NULL,
    Amount          NUMERIC       DEFAULT 0 NOT NULL,
    CONSTRAINT X_Payroll_ComponentInput_key PRIMARY KEY (X_Payroll_ComponentInput_ID),
    CONSTRAINT X_Payroll_ComponentInput_Employee FOREIGN KEY (HR_Employee_ID)
        REFERENCES HR_Employee (HR_Employee_ID),
    CONSTRAINT X_Payroll_ComponentInput_Period FOREIGN KEY (X_Payroll_Period_ID)
        REFERENCES X_Payroll_Period (X_Payroll_Period_ID),
    CONSTRAINT X_Payroll_ComponentInput_Component FOREIGN KEY (X_Payroll_Component_ID)
        REFERENCES X_Payroll_Component (X_Payroll_Component_ID)
);
CREATE SEQUENCE X_Payroll_ComponentInput_seq START WITH 1000000;


-- ═══════════════════════════════════════════════════════════════════════
-- BAGIAN 4 — HASIL PROSES (output GeneratePayrollRun, immutable/snapshot)
-- ═══════════════════════════════════════════════════════════════════════

-- ── 4.1 X_Payroll_RunLine — ringkasan hasil per employee per run ─────
-- Tetap ramping (atribut tunggal per employee), breakdown detail ada di
-- RunLineDetail (BPJS) dan RunLineComponent (earning/komponen gaji).
--
-- PENTING — beda makna 3 kolom income:
--   CashGrossIncome     = SUM komponen EARNING yang BENERAN ditransfer
--                          tunai ke employee (Gaji Pokok + Tunjangan).
--                          Dipakai untuk hitung NetIncome (take-home).
--   TaxableGrossIncome  = CashGrossIncome + iuran BPJS employer yang
--                          IsEmployerContributionTaxable='Y' (natura
--                          kena pajak). Dipakai KHUSUS untuk lookup
--                          bracket TER/Progresif — BUKAN untuk NetIncome.
--   BPJSBaseIncome      = SUM komponen yang IsBPJSBase='Y'. Basis untuk
--                          hitung iuran BPJS (sebelum capping per program).
--
-- CumulativeGrossBeforeThisRun & PPh21WithheldPreviouslyThisPeriod:
-- snapshot transparansi untuk kasus Bonus+Reguler dalam periode sama —
-- ditampilkan di slip supaya karyawan bisa lihat kenapa potongan run ini
-- sebesar itu (total gabungan dikurangi yang sudah dipotong sebelumnya).
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
    CashGrossIncome     NUMERIC   DEFAULT 0 NOT NULL,
    TaxableGrossIncome  NUMERIC   DEFAULT 0 NOT NULL,
    BPJSBaseIncome      NUMERIC   DEFAULT 0 NOT NULL,
    TER_Category    CHAR(1)       NULL,
    TER_RateApplied NUMERIC       NULL,
    PPh21_Amount    NUMERIC       DEFAULT 0 NOT NULL,
    CumulativeGrossBeforeThisRun        NUMERIC DEFAULT 0 NOT NULL,
    PPh21WithheldPreviouslyThisPeriod   NUMERIC DEFAULT 0 NOT NULL,
    TotalDeduction  NUMERIC       DEFAULT 0 NOT NULL,
    NetIncome       NUMERIC       DEFAULT 0 NOT NULL,
    CONSTRAINT X_Payroll_RunLine_key PRIMARY KEY (X_Payroll_RunLine_ID),
    CONSTRAINT X_Payroll_RunLine_Run FOREIGN KEY (X_Payroll_Run_ID)
        REFERENCES X_Payroll_Run (X_Payroll_Run_ID),
    CONSTRAINT X_Payroll_RunLine_Employee FOREIGN KEY (HR_Employee_ID)
        REFERENCES HR_Employee (HR_Employee_ID)
);
CREATE SEQUENCE X_Payroll_RunLine_seq START WITH 1000000;

-- ── 4.2 X_Payroll_RunLineDetail — breakdown per program BPJS ─────────
-- 1 baris = 1 program (KESEHATAN/JHT/JP/JKK/JKM/...) per employee per
-- run. Generik — ProgramType dinamis, nambah program baru TIDAK perlu
-- ALTER TABLE. Snapshot rate yang dipakai saat itu untuk audit trail
-- penuh (rate live di X_Payroll_BPJS_Rate bisa berubah di kemudian hari,
-- baris ini tidak ikut berubah).
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
    ProgramType     VARCHAR(20)   NOT NULL,
    WageBase        NUMERIC       NOT NULL,
    EmployeeRateApplied NUMERIC   DEFAULT 0 NOT NULL,
    EmployerRateApplied NUMERIC   DEFAULT 0 NOT NULL,
    EmployeeAmount  NUMERIC       DEFAULT 0 NOT NULL,
    EmployerAmount  NUMERIC       DEFAULT 0 NOT NULL,
    X_Payroll_BPJS_Rate_ID NUMERIC(10) NULL,
    CONSTRAINT X_Payroll_RunLineDetail_key PRIMARY KEY (X_Payroll_RunLineDetail_ID),
    CONSTRAINT X_Payroll_RunLineDetail_RunLine FOREIGN KEY (X_Payroll_RunLine_ID)
        REFERENCES X_Payroll_RunLine (X_Payroll_RunLine_ID),
    CONSTRAINT X_Payroll_RunLineDetail_Rate FOREIGN KEY (X_Payroll_BPJS_Rate_ID)
        REFERENCES X_Payroll_BPJS_Rate (X_Payroll_BPJS_Rate_ID)
);
CREATE SEQUENCE X_Payroll_RunLineDetail_seq START WITH 1000000;

-- ── 4.3 X_Payroll_RunLineComponent — breakdown per komponen gaji ────
-- 1 baris = 1 komponen (Gaji Pokok/Tunjangan Transport/dst) per employee
-- per run. Snapshot dari X_Payroll_ComponentInput pada saat run
-- diproses — immutable setelah itu.
CREATE TABLE X_Payroll_RunLineComponent (
    X_Payroll_RunLineComponent_ID NUMERIC(10) NOT NULL,
    AD_Client_ID    NUMERIC(10)   NOT NULL,
    AD_Org_ID       NUMERIC(10)   NOT NULL,
    IsActive        CHAR(1)       DEFAULT 'Y' NOT NULL,
    Created         TIMESTAMP     DEFAULT now() NOT NULL,
    CreatedBy       NUMERIC(10)   NOT NULL,
    Updated         TIMESTAMP     DEFAULT now() NOT NULL,
    UpdatedBy       NUMERIC(10)   NOT NULL,
    X_Payroll_RunLine_ID    NUMERIC(10) NOT NULL,
    X_Payroll_Component_ID  NUMERIC(10) NOT NULL,
    Amount          NUMERIC       DEFAULT 0 NOT NULL,
    CONSTRAINT X_Payroll_RunLineComponent_key PRIMARY KEY (X_Payroll_RunLineComponent_ID),
    CONSTRAINT X_Payroll_RunLineComponent_RunLine FOREIGN KEY (X_Payroll_RunLine_ID)
        REFERENCES X_Payroll_RunLine (X_Payroll_RunLine_ID),
    CONSTRAINT X_Payroll_RunLineComponent_Component FOREIGN KEY (X_Payroll_Component_ID)
        REFERENCES X_Payroll_Component (X_Payroll_Component_ID)
);
CREATE SEQUENCE X_Payroll_RunLineComponent_seq START WITH 1000000;


-- ═══════════════════════════════════════════════════════════════════════
-- BAGIAN 5 — KOLOM TAMBAHAN DI HR_EMPLOYEE (tabel core iDempiere)
--
-- JANGAN jalankan sebagai ALTER TABLE manual di sini. Tambahkan lewat
-- GUI System Admin → Application Dictionary → Table and Column →
-- HR_Employee, supaya AD_Column ter-generate dengan benar (Reference,
-- Element, dst) dan generic model REST langsung mengenalinya.
--
-- Daftar kolom yang perlu ditambahkan:
--   X_PTKPStatus              String, Reference List (TK/0, TK/1, ...,
--                              K/0, K/1, K/2, K/3)
--   X_TER_Category            String, Reference List (A, B, C)
--   X_NPWP                    String
--   X_BPJSKesehatan_No        String
--   X_BPJSKetenagakerjaan_No  String
--
-- CATATAN: X_MonthlyGrossIncome yang sempat disebut di diskusi awal
-- (untuk asumsi struktur gaji FLAT) SUDAH TIDAK RELEVAN sejak keputusan
-- struktur gaji KOMPONEN — gross income sekarang dihitung dari
-- SUM(X_Payroll_ComponentInput.Amount) per periode, bukan satu kolom
-- tetap di HR_Employee. JANGAN tambahkan kolom itu.
-- ═══════════════════════════════════════════════════════════════════════
