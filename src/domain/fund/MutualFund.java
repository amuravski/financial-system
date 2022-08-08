package domain.fund;

import java.math.BigDecimal;
import java.util.Objects;

public class MutualFund extends AbstractFund {

    BigDecimal NAV;

    public MutualFund(String name, BigDecimal assets, BigDecimal NAV) {
        super(name, assets);
        this.NAV = NAV;
    }

    public MutualFund(String name, BigDecimal NAV) {
        super(name);
        this.NAV = NAV;
    }

    public MutualFund(String name) {
        super(name);
    }

    public BigDecimal getNAV() {
        return NAV;
    }

    public void setNAV(BigDecimal NAV) {
        this.NAV = NAV;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MutualFund)) return false;
        if (!super.equals(o)) return false;
        MutualFund other = (MutualFund) o;
        return Objects.equals(NAV, other.NAV);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), NAV);
    }

    @Override
    public String toString() {
        return "MutualFund{" +
                "NAV=" + NAV + ", " + super.toString() +
                '}';
    }
}
