package domain.bank;

import java.math.BigDecimal;
import java.rmi.UnexpectedException;
import java.util.Objects;

public class MortgageBank extends CommercialBank {

    public static final int MORTGAGE_BANK_LICENCE_PERIOD = 5;

    private BigDecimal marginFractionRequirement;

    public MortgageBank(String name) {
        super(name);
    }

    public BigDecimal getMarginFractionRequirement() {
        return marginFractionRequirement;
    }

    public void setMarginFractionRequirement(BigDecimal marginFractionRequirement) throws UnexpectedException {
        if (marginFractionRequirement.compareTo(new BigDecimal("0.0")) > 0) {
            this.marginFractionRequirement = marginFractionRequirement;
        } else {
            throw new UnexpectedException("Margin fraction must be greater than 0");
        }
    }

    public BigDecimal getMarginRequirementForMortgage(BigDecimal estatePrice) {
        return estatePrice.multiply(marginFractionRequirement);
    }

    @Override
    public int getDefaultLicencePeriod() {
        return MORTGAGE_BANK_LICENCE_PERIOD;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MortgageBank)) return false;
        if (!super.equals(o)) return false;
        MortgageBank other = (MortgageBank) o;
        return marginFractionRequirement.equals(other.marginFractionRequirement);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), marginFractionRequirement);
    }

    @Override
    public String toString() {
        return "MortgageBank{" +
                "marginFractionRequirement=" + marginFractionRequirement + ", " + super.toString() +
                '}';
    }
}
