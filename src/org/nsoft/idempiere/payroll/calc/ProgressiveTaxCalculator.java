package org.nsoft.idempiere.payroll.calc;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * Kalkulasi PPh21 tarif progresif Pasal 17 UU HPP — dipakai untuk
 * REKONSILIASI TAHUNAN masa Desember. Ini WAJIB dilakukan di masa Desember
 * menurut PMK 168/2023: TER hanya alat estimasi pemotongan bulanan,
 * kewajiban pajak sebenarnya tetap dihitung progresif atas setahun penuh.
 *
 * Alur: PKP (Penghasilan Kena Pajak) tahunan = Penghasilan Bruto Tahunan
 * - PTKP → hitung PPh21 terutang setahun pakai bracket progresif → hasil
 * Desember = PPh21 terutang setahun - PPh21 yang SUDAH dipotong Jan-Nov
 * (via TER). Bisa NEGATIF (lebih bayar) — caller harus tangani kasus itu
 * sesuai kebijakan perusahaan (restitusi/kompensasi masa berikut).
 *
 * PTKP amount TIDAK dihitung di sini — diterima sebagai parameter, karena
 * penentuan PTKP butuh tabel referensi terpisah (status kawin/tanggungan
 * → nominal) yang belum kita desain. Lihat catatan di akhir jawaban ini.
 */
public class ProgressiveTaxCalculator {

    /**
     * @param annualGrossIncome     total penghasilan bruto Jan-Des (setahun penuh)
     * @param ptkpAmount            Penghasilan Tidak Kena Pajak tahunan employee ini
     * @param alreadyWithheldYTD    total PPh21 yang sudah dipotong Jan-Nov (via TER)
     * @param brackets              bracket progresif Pasal 17 (SchemeType='PROGRESSIVE')
     * @return                      PPh21 yang harus dipotong di masa Desember
     *                              (bisa negatif = lebih bayar, caller wajib handle)
     */
    public static BigDecimal calculateDecemberAmount(BigDecimal annualGrossIncome,
                                                       BigDecimal ptkpAmount,
                                                       BigDecimal alreadyWithheldYTD,
                                                       List<TaxBracket> brackets) {
        BigDecimal annualPPh21 = calculateAnnualLiability(annualGrossIncome, ptkpAmount, brackets);
        return annualPPh21.subtract(alreadyWithheldYTD);
    }

    /**
     * Hitung PPh21 terutang setahun penuh dari PKP, pakai bracket progresif
     * berlapis (setiap lapisan kena rate-nya sendiri, BUKAN flat rate dari
     * bracket tertinggi yang dicapai — ini beda mendasar dari cara TER
     * lookup bekerja).
     */
    public static BigDecimal calculateAnnualLiability(BigDecimal annualGrossIncome,
                                                        BigDecimal ptkpAmount,
                                                        List<TaxBracket> brackets) {
        BigDecimal pkp = annualGrossIncome.subtract(ptkpAmount);
        if (pkp.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO; // penghasilan di bawah PTKP, tidak kena pajak
        }

        BigDecimal totalTax = BigDecimal.ZERO;

        for (TaxBracket b : brackets) {
            if (pkp.compareTo(b.incomeFrom) <= 0) continue; // PKP belum masuk lapisan ini

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
