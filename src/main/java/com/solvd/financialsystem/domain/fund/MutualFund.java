package com.solvd.financialsystem.domain.fund;

import java.math.BigDecimal;
import java.util.Objects;

public class MutualFund extends AbstractFund {

    private BigDecimal nav;

    public MutualFund(String name, BigDecimal assets, BigDecimal nav) {
        super(name, assets);
        this.nav = nav;
    }

    public MutualFund(String name, BigDecimal nav) {
        super(name);
        this.nav = nav;
    }

    public MutualFund(String name) {
        super(name);
    }

    public BigDecimal getNav() {
        return nav;
    }

    public void setNav(BigDecimal nav) {
        this.nav = nav;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MutualFund)) return false;
        if (!super.equals(o)) return false;
        MutualFund other = (MutualFund) o;
        return Objects.equals(nav, other.nav);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), nav);
    }

    @Override
    public String toString() {
        return "MutualFund{" +
                "NAV=" + nav + ", " + super.toString() +
                '}';
    }
}
