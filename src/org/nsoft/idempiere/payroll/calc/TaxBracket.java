package org.nsoft.idempiere.payroll.calc;

import java.math.BigDecimal;

/**
 * DTO generik untuk satu baris bracket tarif — dipakai TER, Progresif,
 * maupun Pasal 26. Diisi oleh caller (biasanya process/ layer) dari hasil
 * query X_Payroll_TaxRate, supaya calc/ package ini TIDAK tahu apa-apa
 * soal iDempiere PO/DB — murni angka masuk, angka keluar.
 */
public class TaxBracket {
    public final String category;      // 'A'/'B'/'C' untuk TER, null untuk skema lain
    public final BigDecimal incomeFrom;
    public final BigDecimal incomeTo;   // null = tidak terbatas (bracket teratas)
    public final BigDecimal rate;       // desimal, mis. 0.05 = 5%

    public TaxBracket(String category, BigDecimal incomeFrom, BigDecimal incomeTo, BigDecimal rate) {
        this.category = category;
        this.incomeFrom = incomeFrom;
        this.incomeTo = incomeTo;
        this.rate = rate;
    }

    public boolean contains(BigDecimal income) {
        boolean withinLower = income.compareTo(incomeFrom) >= 0;
        boolean withinUpper = (incomeTo == null) || income.compareTo(incomeTo) < 0;
        return withinLower && withinUpper;
    }
}
