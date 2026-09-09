package org.nsoft.idempiere.payroll.calc;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * Kalkulasi PPh21 skema TER (PMK 168/2023). HANYA berlaku masa
 * Januari–November — masa Desember WAJIB pakai ProgressiveTaxCalculator
 * (rekonsiliasi tahunan Pasal 17). Caller (GeneratePayrollRun) yang
 * bertanggung jawab memilih calculator yang benar berdasar periode.
 *
 * Nilai bracket/rate TIDAK di-hardcode — selalu parameter `brackets`
 * (hasil query X_Payroll_TaxRate WHERE SchemeType='TER'). Validasi
 * kebenaran angka bracket adalah tanggung jawab tim finance/konsultan
 * pajak, bukan kode ini.
 */
public class TERCalculator {

    /**
     * @param category                'A'/'B'/'C'
     * @param grossMonthlyIncome       basis TER (TaxableGrossIncome kumulatif)
     * @param brackets                 bracket TER (boleh berisi semua kategori,
     *                                 method ini filter ulang by category)
     * @param npwpSurchargeMultiplier  BigDecimal.ONE kalau ber-NPWP, atau
     *                                 pengali tambahan (mis. 1.2) kalau tidak
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
     * Cuma cari rate mentah (tanpa dikalikan income/surcharge) — dipakai
     * caller yang perlu simpan TER_RateApplied sebelum surcharge, atau
     * yang mengelola perkalian sendiri (lihat GeneratePayrollRun untuk
     * kasus kumulatif multi-run).
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
