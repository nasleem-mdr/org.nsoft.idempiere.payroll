package org.nsoft.idempiere.payroll.calc;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class TERCalculator {

    public static class TERBracket {
        public BigDecimal incomeFrom, incomeTo, rate;
    }

    /** Pure function — gampang di-unit-test terpisah dari database. */
    public static BigDecimal calculate(BigDecimal grossMonthlyIncome, List<TERBracket> brackets) {
        for (TERBracket b : brackets) {
            boolean withinLower = grossMonthlyIncome.compareTo(b.incomeFrom) >= 0;
            boolean withinUpper = b.incomeTo == null || grossMonthlyIncome.compareTo(b.incomeTo) < 0;
            if (withinLower && withinUpper) {
                return grossMonthlyIncome.multiply(b.rate).setScale(0, RoundingMode.HALF_UP);
            }
        }
        throw new IllegalStateException("Tidak ada bracket TER yang cocok untuk income: " + grossMonthlyIncome);
    }
}
