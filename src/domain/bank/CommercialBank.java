package domain.bank;

import domain.Regulatable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class CommercialBank extends AbstractBank implements Regulatable {

    private BigDecimal requiredReserves;

    public CommercialBank(String name, LocalDateTime licencedUntil, AbstractBank subsidiaryBank, BigDecimal requiredReserves) {
        super(name, licencedUntil, subsidiaryBank);
        this.requiredReserves = requiredReserves;
    }

    public CommercialBank(String name, BigDecimal assets, BigDecimal liabilities, LocalDateTime licencedUntil, BigDecimal requiredReserves) {
        super(name, assets, liabilities, licencedUntil);
        this.requiredReserves = requiredReserves;
    }

    public CommercialBank(String name, BigDecimal assets, BigDecimal liabilities) {
        super(name, assets, liabilities);
    }

    public CommercialBank(String name, LocalDateTime licencedUntil, BigDecimal requiredReserves) {
        super(name, licencedUntil);
        this.requiredReserves = requiredReserves;
    }

    public CommercialBank(String name, BigDecimal assets, BigDecimal liabilities, LocalDateTime licencedUntil, AbstractBank[] subsidiaryBanks, BigDecimal requiredReserves) {
        super(name, assets, liabilities, licencedUntil, subsidiaryBanks);
        this.requiredReserves = requiredReserves;
    }

    public CommercialBank(String name) {
        super(name);
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CommercialBank)) return false;
        if (!super.equals(o)) return false;
        CommercialBank other = (CommercialBank) o;
        return requiredReserves.equals(other.requiredReserves);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), requiredReserves);
    }

    @Override
    public String toString() {
        return "CommercialBank{" +
                "requiredReserves=" + requiredReserves + ", " + super.toString() +
                '}';
    }
}
