package com.solvd.financialsystem.domain.exchange;

import com.solvd.financialsystem.domain.FinancialActor;
import com.solvd.financialsystem.domain.LicenseExtendable;
import com.solvd.financialsystem.domain.Regulatable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class AbstractExchange implements FinancialActor, LicenseExtendable, Regulatable {

    private static final int DEFAULT_EXCHANGE_LICENCE_PERIOD = 3;

    private String name;
    private LocalDateTime licencedUntil;
    private BigDecimal requiredReserves;

    public AbstractExchange(String name, LocalDateTime licencedUntil, BigDecimal reservesRequired) {
        this.licencedUntil = licencedUntil;
        this.requiredReserves = reservesRequired;
    }

    public AbstractExchange(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getLicencedUntil() {
        return licencedUntil;
    }

    public void setLicencedUntil(LocalDateTime licencedUntil) {
        this.licencedUntil = licencedUntil;
    }

    public BigDecimal getRequiredReserves() {
        return requiredReserves;
    }

    public void setRequiredReserves(BigDecimal requiredReserves) {
        this.requiredReserves = requiredReserves;
    }

    public static int getDefaultLicencePeriod() {
        return DEFAULT_EXCHANGE_LICENCE_PERIOD;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractExchange)) return false;
        AbstractExchange other = (AbstractExchange) o;
        return licencedUntil.equals(other.licencedUntil) &&
                requiredReserves.equals(other.requiredReserves);
    }

    @Override
    public int hashCode() {
        return Objects.hash(licencedUntil, requiredReserves);
    }

    @Override
    public String toString() {
        return "AbstractExchange{" +
                "licencedUntil=" + licencedUntil +
                ", reservesRequired=" + requiredReserves +
                '}';
    }

    @Override
    public void extendLicence(int extendedForYears) {
        LocalDateTime LicencedUntilDate = getLicencedUntil();
        LocalDateTime newLicencedUntilDate = LicencedUntilDate.plusYears(extendedForYears);
        setLicencedUntil(newLicencedUntilDate);
    }

    @Override
    public void extendLicence() {
        int extendedFor = getDefaultLicencePeriod();
        LocalDateTime LicencedUntilDate = getLicencedUntil();
        LocalDateTime newLicencedUntilDate = LicencedUntilDate.plusYears(extendedFor);
        setLicencedUntil(newLicencedUntilDate);
    }
}
