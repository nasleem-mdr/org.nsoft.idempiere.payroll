package org.nsoft.idempiere.payroll.calc;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * Kalkulasi PPh21 tarif progresif Pasal 17 UU HPP — WAJIB dipakai untuk
 * rekonsiliasi tahunan masa Desember (PMK 168/2023): TER hanya estimasi
 * pemotongan bulanan, kewajiban pajak sebenarnya dihitung progresif atas
 * setahun penuh.
 *
 * Hasil Desember = PPh21 terutang setahun − PPh21 yang SUDAH dipotong
 * Jan–Nov (+ run lain dalam bulan Desember itu sendiri kalau ada). Bisa
 * NEGATIF (lebih bayar) — caller wajib menangani sesuai kebijakan
 * perusahaan (restitusi/kompensasi masa berikutnya).
 *
 * PTKP amount diterima sebagai parameter (lookup dari X_Payroll_PTKP_Rate
 * dilakukan di process/ layer, bukan di sini).
 */
public class ProgressiveTaxCalculator {

    public static BigDecimal calculateDecemberAmount(BigDecimal annualGrossIncome,
                                                       BigDecimal ptkpAmount,
                                                       BigDecimal alreadyWithheldYTD,
                                                       List<TaxBracket> brackets) {
        BigDecimal annualPPh21 = calculateAnnualLiability(annualGrossIncome, ptkpAmount, brackets);
        return annualPPh21.subtract(alreadyWithheldYTD);
    }

    /**
     * Bracket berlapis — setiap lapisan kena rate-nya sendiri (BUKAN
     * flat rate dari bracket tertinggi yang dicapai).
     */
    public static BigDecimal calculateAnnualLiability(BigDecimal annualGrossIncome,
                                                        BigDecimal ptkpAmount,
                                                        List<TaxBracket> brackets) {
        BigDecimal pkp = annualGrossIncome.subtract(ptkpAmount);
        if (pkp.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal totalTax = BigDecimal.ZERO;
        for (TaxBracket b : brackets) {
            if (pkp.compareTo(b.incomeFrom) <= 0) continue;

            BigDecimal layerCeiling = (b.incomeTo != null) ? b.incomeTo : pkp;
            BigDecimal layerTop = pkp.min(layerCeiling);
            BigDecimal layerAmount = layerTop.subtract(b.incomeFrom);

            if (layerAmount.compareTo(BigDecimal.ZERO) > 0) {
                totalTax = totalTax.add(layerAmount.multiply(b.rate));
            }
        }
        return totalTax.setScale(0, RoundingMode.HALF_UP);
    }
}
