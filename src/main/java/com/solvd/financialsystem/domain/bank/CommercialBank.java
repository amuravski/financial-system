package com.solvd.financialsystem.domain.bank;

import com.solvd.financialsystem.domain.Regulatable;

import java.math.BigDecimal;
import java.util.Objects;

public class CommercialBank extends AbstractBank implements Regulatable {

    private BigDecimal requiredReserves;

    public CommercialBank(String name, BigDecimal requiredReserves) {
        super(name);
        this.requiredReserves = requiredReserves;
    }

    public CommercialBank(String name) {
        super(name);
    }

    public CommercialBank(String name, BigDecimal assets, BigDecimal liabilities) {
        super(name, assets, liabilities);
    }

    public BigDecimal getRequiredReserves() {
        return requiredReserves;
    }

    public void setRequiredReserves(BigDecimal requiredReserves) {
        this.requiredReserves = requiredReserves;
    }

    @Override
    public void extendLicence(int years) {
        this.setLicencedUntil(this.getLicencedUntil().plusYears(years));
    }

    @Override
    public String toString() {
        return "CommercialBank{" +
                "requiredReserves=" + requiredReserves + ", " + super.toString() +
                '}';
    }
}
