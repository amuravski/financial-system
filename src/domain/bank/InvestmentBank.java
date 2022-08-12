package domain.bank;

import domain.Reportable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.util.Objects;

public class InvestmentBank extends AbstractBank implements Reportable {

    private static final Logger LOGGER = LogManager.getLogger(InvestmentBank.class);

    private BigDecimal aum;
    private BigDecimal tradingFee;

    public InvestmentBank(String name) {
        super(name);
    }

    public BigDecimal getAum() {
        return aum;
    }

    public void setAum(BigDecimal aum) {
        this.aum = aum;
    }

    public BigDecimal getTradingFee() {
        return tradingFee;
    }

    public void setTradingFee(BigDecimal tradingFee) {
        this.tradingFee = tradingFee;
    }

    @Override
    public void extendLicence(int years) {
        this.setLicencedUntil(this.getLicencedUntil().plusYears(years));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InvestmentBank)) return false;
        if (!super.equals(o)) return false;
        InvestmentBank other = (InvestmentBank) o;
        return Objects.equals(aum, other.aum) &&
                Objects.equals(tradingFee, other.tradingFee);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), aum, tradingFee);
    }

    @Override
    public String toString() {
        return "InvestmentBank{" +
                "aum=" + aum +
                ", tradingFee=" + tradingFee + ", " + super.toString() +
                '}';
    }

    @Override
    public void publishReport() {
        LOGGER.info(this.toString() + "\nReported AUM: " + aum);
    }
}
