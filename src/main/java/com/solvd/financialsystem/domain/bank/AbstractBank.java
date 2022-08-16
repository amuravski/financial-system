package com.solvd.financialsystem.domain.bank;

import com.solvd.financialsystem.domain.FinancialActor;
import com.solvd.financialsystem.domain.LicenseExtendable;
import com.solvd.financialsystem.domain.exception.LicenseExpiredException;
import com.solvd.financialsystem.domain.exception.NoSubsidiaryBankException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public abstract class AbstractBank implements FinancialActor, LicenseExtendable {

    private static final int DEFAULT_BANK_LICENCE_PERIOD = 2;

    private String name;
    private BigDecimal assets;
    private BigDecimal liabilities;
    private LocalDateTime licencedUntil;
    private List<AbstractBank> subsidiaryBanks;
    private String BIC;

    public AbstractBank(String name) {
        this.name = name;
    }

    public AbstractBank(String name, BigDecimal assets, BigDecimal liabilities) {
        this.name = name;
        this.assets = assets;
        this.liabilities = liabilities;
    }

    public void addSubsidiary(AbstractBank subsidiary) {
        subsidiaryBanks.add(subsidiary);
    }

    public void removeSubsidiary(AbstractBank subsidiary) {
        subsidiaryBanks.remove(subsidiary);
    }

    public String getBIC() {
        return BIC;
    }

    public void setBIC(String BIC) {
        this.BIC = BIC;
    }

    public int getDefaultLicencePeriod() {
        return DEFAULT_BANK_LICENCE_PERIOD;
    }

    public BigDecimal getAssets() {
        return assets;
    }

    public void setAssets(BigDecimal assets) {
        this.assets = assets;
    }

    public BigDecimal getLiabilities() {
        return liabilities;
    }

    public void setLiabilities(BigDecimal liabilities) {
        this.liabilities = liabilities;
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

    public List<AbstractBank> getSubsidiaryBanks() {
        if (subsidiaryBanks == null) {
            throw new NoSubsidiaryBankException(this.toString() + " has no subsidiaries.");
        } else if (subsidiaryBanks.size() == 0) {
            throw new NoSubsidiaryBankException(this.toString() + " has no subsidiaries.");
        }
        return subsidiaryBanks;
    }

    public void setSubsidiaryBanks(List<AbstractBank> subsidiaryBanks) {
        this.subsidiaryBanks = subsidiaryBanks;
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
        try {
            LocalDateTime LicencedUntilDate = getLicencedUntil();
            LocalDateTime newLicencedUntilDate = LicencedUntilDate.plusYears(extendedFor);
            setLicencedUntil(newLicencedUntilDate);
        } catch (NullPointerException e) {
            throw new LicenseExpiredException(this.toString() + " has no licence.", e.getCause());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractBank)) return false;
        AbstractBank that = (AbstractBank) o;
        return BIC.equals(that.BIC);
    }

    @Override
    public int hashCode() {
        return Objects.hash(BIC);
    }

    @Override
    public String toString() {
        return "AbstractBank{" +
                "name='" + name + '\'' +
                ", assets=" + assets +
                ", liabilities=" + liabilities +
                ", licencedUntil=" + licencedUntil +
                ", subsidiaryBanks=" + subsidiaryBanks +
                ", BIC='" + BIC + '\'' +
                '}';
    }
}
