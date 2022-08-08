package domain.bank;

import domain.Reportable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class InvestmentBank extends AbstractBank implements Reportable {

    private static final Logger logger = (Logger) LogManager.getRootLogger();

    private BigDecimal aum;
    private BigDecimal tradingFee;

    public InvestmentBank(String name, LocalDateTime licencedUntil, AbstractBank subsidiaryBank, BigDecimal aum, BigDecimal tradingFee) {
        super(name, licencedUntil, subsidiaryBank);
        this.aum = aum;
        this.tradingFee = tradingFee;
    }

    public InvestmentBank(String name, LocalDateTime licencedUntil) {
        super(name, licencedUntil);
    }

    public InvestmentBank(String name) {
        super(name);
    }

    public InvestmentBank(String name, BigDecimal assets, BigDecimal liabilities, LocalDateTime licencedUntil, AbstractBank[] subsidiaryBanks, BigDecimal aum, BigDecimal tradingFee) {
        super(name, assets, liabilities, licencedUntil, subsidiaryBanks);
        this.aum = aum;
        this.tradingFee = tradingFee;
    }

    public InvestmentBank(String name, BigDecimal assets, BigDecimal liabilities, LocalDateTime licencedUntil, BigDecimal aum, BigDecimal tradingFee) {
        super(name, assets, liabilities, licencedUntil);
        this.aum = aum;
        this.tradingFee = tradingFee;
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
        logger.info(this.toString() + "\nReported AUM: " + aum);
    }
}
