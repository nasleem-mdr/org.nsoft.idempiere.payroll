package org.nsoft.idempiere.payroll.calc;

import java.math.BigDecimal;

/**
 * Helper capping basis upah — dipakai BPJS (mis. BPJS Kesehatan capped
 * ~Rp12jt, JP capped sesuai batas tahunan yang di-update pemerintah) dan
 * berpotensi dipakai juga di skema pajak tertentu. Lower cap jarang dipakai
 * tapi disediakan untuk kasus seperti "basis minimal = UMR provinsi".
 *
 * Null pada lower/upper berarti tidak ada batas di sisi itu.
 */
public class WageCapUtil {

    public static BigDecimal applyCap(BigDecimal amount, BigDecimal lowerCap, BigDecimal upperCap) {
        BigDecimal result = amount;
        if (lowerCap != null && result.compareTo(lowerCap) < 0) {
            result = lowerCap;
        }
        if (upperCap != null && result.compareTo(upperCap) > 0) {
            result = upperCap;
        }
        return result;
    }
}
