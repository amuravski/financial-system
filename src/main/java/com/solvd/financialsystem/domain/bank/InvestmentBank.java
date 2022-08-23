package com.solvd.financialsystem.domain.bank;

import com.solvd.financialsystem.domain.Reportable;
import com.solvd.financialsystem.domain.exception.LicenseExpiredException;
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
        this.setLicencedUntil(this.getLicencedUntil()
                .orElseThrow(() -> new LicenseExpiredException(this.toString() + " has no licence."))
                .plusYears(years));
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
