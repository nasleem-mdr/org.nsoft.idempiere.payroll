package org.nsoft.idempiere.payroll.calc;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * Kalkulasi PPh21 skema TER (Tarif Efektif Rata-rata, PMK 168/2023).
 * HANYA berlaku untuk masa Januari–November — masa Desember WAJIB pakai
 * ProgressiveTaxCalculator (rekonsiliasi tahunan Pasal 17), caller yang
 * bertanggung jawab memilih calculator yang benar berdasar periode.
 *
 * PENTING: nilai bracket/rate TIDAK di-hardcode di sini — selalu diterima
 * sebagai parameter `brackets` (hasil query X_Payroll_TaxRate WHERE
 * SchemeType='TER'). Validasi kebenaran angka bracket adalah tanggung
 * jawab tim finance/konsultan pajak, bukan kode ini.
 */
public class TERCalculator {

    /**
     * @param category         'A'/'B'/'C' — hasil resolusi dari HR_Employee
     *                         atau override di X_Payroll_EmployeeTaxProfile
     * @param grossMonthlyIncome penghasilan bruto bulan berjalan
     * @param brackets         bracket TER kategori `category` saja (caller
     *                         sudah filter, atau boleh kirim semua kategori
     *                         — method ini akan filter ulang by category)
     * @param npwpSurchargeMultiplier pengali tambahan untuk employee tanpa
     *                         NPWP (mis. new BigDecimal("1.2")), atau
     *                         BigDecimal.ONE kalau ber-NPWP / tidak berlaku
     */
    public static BigDecimal calculate(String category, BigDecimal grossMonthlyIncome,
                                        List<TaxBracket> brackets,
                                        BigDecimal npwpSurchargeMultiplier) {
        BigDecimal rate = lookupRate(category, grossMonthlyIncome, brackets);
        BigDecimal effectiveRate = rate.multiply(
            npwpSurchargeMultiplier != null ? npwpSurchargeMultiplier : BigDecimal.ONE);

        return grossMonthlyIncome.multiply(effectiveRate).setScale(0, RoundingMode.HALF_UP);
    }

    /**
     * Cuma cari rate-nya saja (tanpa dikalikan income) — berguna kalau
     * caller perlu tahu rate mentah untuk disimpan di X_Payroll_RunLine
     * .TER_RateApplied sebelum surcharge diterapkan.
     */
    public static BigDecimal lookupRate(String category, BigDecimal grossMonthlyIncome,
                                         List<TaxBracket> brackets) {
        for (TaxBracket b : brackets) {
            boolean categoryMatch = (b.category == null) || b.category.equals(category);
            if (categoryMatch && b.contains(grossMonthlyIncome)) {
                return b.rate;
            }
        }
        throw new IllegalStateException(
            "Tidak ada bracket TER kategori " + category +
            " yang cocok untuk penghasilan: " + grossMonthlyIncome +
            " — cek data X_Payroll_TaxRate, kemungkinan bracket teratas " +
            "(IncomeTo=NULL) belum di-set atau kategori salah."
        );
    }
}
