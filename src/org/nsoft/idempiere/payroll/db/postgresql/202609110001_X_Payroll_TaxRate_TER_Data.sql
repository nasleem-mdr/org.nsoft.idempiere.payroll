-- ═══════════════════════════════════════════════════════════════════════
-- Insert data bracket TER (Tarif Efektif Rata-rata) — Kategori A, B, C
-- ke X_Payroll_TaxRate (SchemeType='TER')
--
-- SUMBER DATA: disalin persis dari daftar yang diberikan.
-- ⚠️ WAJIB diverifikasi terhadap lampiran resmi PMK 168/2023 sebelum
-- dipakai production — saya tidak memvalidasi kebenaran angka ini
-- sebagai otoritas pajak, cuma memindahkan struktur data ke SQL.
--
-- ⚠️ TAMBAHAN PENTING YANG TIDAK ADA DI DATA ASLI KAMU:
-- Bracket "0% untuk penghasilan di bawah ambang terendah" SAYA
-- TAMBAHKAN sendiri (IncomeFrom=0 sampai batas bawah bracket pertama
-- tiap kategori, Rate=0) — TANPA bracket ini, TERCalculator.lookupRate()
-- akan THROW EXCEPTION untuk karyawan berpenghasilan di bawah ambang
-- terendah (mis. kategori A: di bawah Rp5.400.001), karena tidak ada
-- baris yang match. WAJIB kamu verifikasi apakah nilai batas bawah ini
-- (5.400.000 / 6.200.000 / 6.600.000) sudah tepat sesuai lampiran resmi
-- PMK 168 — saya asumsikan "IncomeFrom pertama - 1" sebagai batas atas
-- bracket 0%, TIDAK dikonfirmasi ke sumber resmi.
--
-- ⚠️ BRACKET TERATAS: saya PERTAHANKAN IncomeTo eksplisit sesuai data
-- yang kamu berikan (bukan NULL/unbounded). Konsekuensinya: penghasilan
-- di ATAS batas teratas (>1.400.000.000 utk A, >1.405.000.000 utk B,
-- >1.419.000.000 utk C) akan membuat TERCalculator.lookupRate() THROW
-- EXCEPTION karena tidak ada bracket yang match. Kalau kamu mau ini
-- fail-safe untuk penghasilan sangat tinggi, ubah IncomeTo baris
-- terakhir tiap kategori jadi NULL secara manual — saya TIDAK melakukan
-- itu di sini karena berarti mengubah data dari yang kamu berikan tanpa
-- konfirmasi eksplisit.
--
-- Ganti :client_id, :org_id, :user_id, :valid_from di bawah sesuai
-- instance kamu sebelum eksekusi (psql \set, atau replace langsung).
-- ═══════════════════════════════════════════════════════════════════════

\set client_id 1000000
\set org_id 0
\set user_id 100
\set valid_from '''2024-01-01'''

-- ─── KATEGORI A ─────────────────────────────────────────────────────────

-- Bracket 0% (TAMBAHAN — lihat catatan di atas, verifikasi batas atasnya)
INSERT INTO X_Payroll_TaxRate
(X_Payroll_TaxRate_ID, AD_Client_ID, AD_Org_ID, IsActive, Created, CreatedBy, Updated, UpdatedBy,
 SchemeType, Category, IncomeFrom, IncomeTo, Rate, ValidFrom, ValidTo)
VALUES
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id,
 'TER', 'A', 0, 5400000, 0.0000, :valid_from, NULL);

INSERT INTO X_Payroll_TaxRate
(X_Payroll_TaxRate_ID, AD_Client_ID, AD_Org_ID, IsActive, Created, CreatedBy, Updated, UpdatedBy,
 SchemeType, Category, IncomeFrom, IncomeTo, Rate, ValidFrom, ValidTo)
VALUES
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'A', 5400001, 5650000, 0.0025, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'A', 5650001, 5950000, 0.0050, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'A', 5950001, 6300000, 0.0075, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'A', 6300001, 6750000, 0.0100, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'A', 6750001, 7500000, 0.0125, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'A', 7500001, 8550000, 0.0150, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'A', 8550001, 9650000, 0.0175, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'A', 9650001, 10050000, 0.0200, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'A', 10050001, 10350000, 0.0225, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'A', 10350001, 10700000, 0.0250, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'A', 10700001, 11050000, 0.0300, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'A', 11050001, 11600000, 0.0350, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'A', 11600001, 12500000, 0.0400, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'A', 12500001, 13750000, 0.0500, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'A', 13750001, 15100000, 0.0600, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'A', 15100001, 16950000, 0.0700, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'A', 16950001, 19750000, 0.0800, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'A', 19750001, 24150000, 0.0900, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'A', 24150001, 26450000, 0.1000, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'A', 26450001, 28000000, 0.1100, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'A', 28000001, 30050000, 0.1200, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'A', 30050001, 32400000, 0.1300, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'A', 32400001, 35400000, 0.1400, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'A', 35400001, 39100000, 0.1500, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'A', 39100001, 43850000, 0.1600, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'A', 43850001, 47800000, 0.1700, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'A', 47800001, 51400000, 0.1800, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'A', 51400001, 56300000, 0.1900, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'A', 56300001, 62200000, 0.2000, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'A', 62200001, 68600000, 0.2100, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'A', 68600001, 77500000, 0.2200, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'A', 77500001, 89000000, 0.2300, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'A', 89000001, 103000000, 0.2400, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'A', 103000001, 125000000, 0.2500, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'A', 125000001, 157000000, 0.2600, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'A', 157000001, 206000000, 0.2700, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'A', 206000001, 337000000, 0.2800, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'A', 337000001, 454000000, 0.2900, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'A', 454000001, 550000000, 0.3000, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'A', 550000001, 695000000, 0.3100, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'A', 695000001, 910000000, 0.3200, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'A', 910000001, 1400000000, 0.3300, :valid_from, NULL);


-- ─── KATEGORI B ─────────────────────────────────────────────────────────

-- Bracket 0% (TAMBAHAN — verifikasi batas atasnya)
INSERT INTO X_Payroll_TaxRate
(X_Payroll_TaxRate_ID, AD_Client_ID, AD_Org_ID, IsActive, Created, CreatedBy, Updated, UpdatedBy,
 SchemeType, Category, IncomeFrom, IncomeTo, Rate, ValidFrom, ValidTo)
VALUES
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id,
 'TER', 'B', 0, 6200000, 0.0000, :valid_from, NULL);

INSERT INTO X_Payroll_TaxRate
(X_Payroll_TaxRate_ID, AD_Client_ID, AD_Org_ID, IsActive, Created, CreatedBy, Updated, UpdatedBy,
 SchemeType, Category, IncomeFrom, IncomeTo, Rate, ValidFrom, ValidTo)
VALUES
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'B', 6200001, 6500000, 0.0025, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'B', 6500001, 6850000, 0.0050, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'B', 6850001, 7300000, 0.0075, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'B', 7300001, 9200000, 0.0100, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'B', 9200001, 10750000, 0.0150, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'B', 10750001, 11250000, 0.0200, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'B', 11250001, 11600000, 0.0250, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'B', 11600001, 12600000, 0.0300, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'B', 12600001, 13600000, 0.0400, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'B', 13600001, 14950000, 0.0500, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'B', 14950001, 16400000, 0.0600, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'B', 16400001, 18450000, 0.0700, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'B', 18450001, 21850000, 0.0800, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'B', 21850001, 26000000, 0.0900, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'B', 26000001, 27700000, 0.1000, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'B', 27700001, 29350000, 0.1100, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'B', 29350001, 31450000, 0.1200, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'B', 31450001, 33950000, 0.1300, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'B', 33950001, 37100000, 0.1400, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'B', 37100001, 41100000, 0.1500, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'B', 41100001, 45800000, 0.1600, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'B', 45800001, 49500000, 0.1700, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'B', 49500001, 53800000, 0.1800, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'B', 53800001, 58500000, 0.1900, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'B', 58500001, 64000000, 0.2000, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'B', 64000001, 71000000, 0.2100, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'B', 71000001, 80000000, 0.2200, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'B', 80000001, 93000000, 0.2300, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'B', 93000001, 109000000, 0.2400, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'B', 109000001, 129000000, 0.2500, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'B', 129000001, 163000000, 0.2600, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'B', 163000001, 211000000, 0.2700, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'B', 211000001, 374000000, 0.2800, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'B', 374000001, 459000000, 0.2900, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'B', 459000001, 555000000, 0.3000, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'B', 555000001, 704000000, 0.3100, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'B', 704000001, 957000000, 0.3200, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'B', 957000001, 1405000000, 0.3300, :valid_from, NULL);


-- ─── KATEGORI C ─────────────────────────────────────────────────────────

-- Bracket 0% (TAMBAHAN — verifikasi batas atasnya)
INSERT INTO X_Payroll_TaxRate
(X_Payroll_TaxRate_ID, AD_Client_ID, AD_Org_ID, IsActive, Created, CreatedBy, Updated, UpdatedBy,
 SchemeType, Category, IncomeFrom, IncomeTo, Rate, ValidFrom, ValidTo)
VALUES
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id,
 'TER', 'C', 0, 6600000, 0.0000, :valid_from, NULL);

INSERT INTO X_Payroll_TaxRate
(X_Payroll_TaxRate_ID, AD_Client_ID, AD_Org_ID, IsActive, Created, CreatedBy, Updated, UpdatedBy,
 SchemeType, Category, IncomeFrom, IncomeTo, Rate, ValidFrom, ValidTo)
VALUES
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'C', 6600001, 6950000, 0.0025, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'C', 6950001, 7350000, 0.0050, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'C', 7350001, 7800000, 0.0075, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'C', 7800001, 8850000, 0.0100, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'C', 8850001, 9800000, 0.0125, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'C', 9800001, 10950000, 0.0150, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'C', 10950001, 11200000, 0.0175, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'C', 11200001, 12050000, 0.0200, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'C', 12050001, 12950000, 0.0300, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'C', 12950001, 14150000, 0.0400, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'C', 14150001, 15550000, 0.0500, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'C', 15550001, 17050000, 0.0600, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'C', 17050001, 19500000, 0.0700, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'C', 19500001, 22700000, 0.0800, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'C', 22700001, 26600000, 0.0900, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'C', 26600001, 28100000, 0.1000, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'C', 28100001, 30100000, 0.1100, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'C', 30100001, 32600000, 0.1200, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'C', 32600001, 35400000, 0.1300, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'C', 35400001, 38900000, 0.1400, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'C', 38900001, 43000000, 0.1500, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'C', 43000001, 47400000, 0.1600, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'C', 47400001, 51200000, 0.1700, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'C', 51200001, 55800000, 0.1800, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'C', 55800001, 60400000, 0.1900, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'C', 60400001, 66700000, 0.2000, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'C', 66700001, 74500000, 0.2100, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'C', 74500001, 83200000, 0.2200, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'C', 83200001, 95600000, 0.2300, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'C', 95600001, 110000000, 0.2400, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'C', 110000001, 134000000, 0.2500, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'C', 134000001, 169000000, 0.2600, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'C', 169000001, 221000000, 0.2700, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'C', 221000001, 390000000, 0.2800, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'C', 390000001, 463000000, 0.2900, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'C', 463000001, 561000000, 0.3000, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'C', 561000001, 709000000, 0.3100, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'C', 709000001, 965000000, 0.3200, :valid_from, NULL),
(nextval('X_Payroll_TaxRate_seq'), :client_id, :org_id, 'Y', now(), :user_id, now(), :user_id, 'TER', 'C', 965000001, 1419000000, 0.3300, :valid_from, NULL);


-- ─── VERIFIKASI SETELAH INSERT ────────────────────────────────────────
-- Jalankan ini untuk cek jumlah baris per kategori & pastikan tidak ada
-- gap/overlap antar bracket (IncomeTo baris N harus = IncomeFrom baris
-- N+1 dikurangi 1, berurutan tanpa lompatan):
--
-- SELECT Category, COUNT(*) AS total_bracket,
--        MIN(IncomeFrom) AS batas_bawah, MAX(IncomeTo) AS batas_atas
-- FROM X_Payroll_TaxRate
-- WHERE SchemeType='TER' AND ValidFrom = :valid_from
-- GROUP BY Category ORDER BY Category;
