package com.solvd.financialsystem.domain.bank;

import java.math.BigDecimal;

public class InsuranceBank extends CommercialBank {

    public InsuranceBank(String name, BigDecimal requiredReserves) {
        super(name, requiredReserves);
    }

    public InsuranceBank(String name) {
        super(name);
    }

    public BigDecimal evaluateInsuranceCase(BigDecimal annualizedChance, BigDecimal averagePayout, BigDecimal annualInsurancePrice) {
        return annualInsurancePrice.subtract(annualInsurancePrice.multiply(averagePayout));
    }

    @Override
    public String toString() {
        return "InsuranceBank{" + super.toString() + "}";
    }
}
