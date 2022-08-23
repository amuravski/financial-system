package com.solvd.financialsystem.domain.bank;

import com.solvd.financialsystem.domain.FinancialActor;
import com.solvd.financialsystem.domain.LicenseExtendable;
import com.solvd.financialsystem.domain.exception.LicenseExpiredException;
import com.solvd.financialsystem.domain.exception.NoSubsidiaryBankException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public abstract class AbstractBank implements FinancialActor, LicenseExtendable {

    private static final int DEFAULT_BANK_LICENCE_PERIOD = 2;

    private String name;
    private BigDecimal assets;
    private BigDecimal liabilities;
    private LocalDateTime licencedUntil;
    private List<AbstractBank> subsidiaryBanks;
    private String bic;

    public AbstractBank(String name) {
        this.name = name;
    }

    public AbstractBank(String name, BigDecimal assets, BigDecimal liabilities) {
        this.name = name;
        this.assets = assets;
        this.liabilities = liabilities;
    }

    public void removeSubsidiary(AbstractBank subsidiary) {
        subsidiaryBanks.remove(subsidiary);
    }

    public String getBic() {
        return bic;
    }

    public void setBic(String bic) {
        this.bic = bic;
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

    public Optional<LocalDateTime> getLicencedUntil() {
        Optional<LocalDateTime> result = Optional.empty();
        if (licencedUntil != null) {
            result = Optional.of(licencedUntil);
        }
        return result;
    }

    public void setLicencedUntil(LocalDateTime licencedUntil) {
        this.licencedUntil = licencedUntil;
    }

    public Optional<List<AbstractBank>> getSubsidiaryBanks() {
        Optional<List<AbstractBank>> result = Optional.empty();
        if (subsidiaryBanks != null && subsidiaryBanks.size() > 0) {
            result = Optional.of(subsidiaryBanks);
        }
        return result;
    }

    public void setSubsidiaryBanks(List<AbstractBank> subsidiaryBanks) {
        this.subsidiaryBanks = subsidiaryBanks;
    }

    @Override
    public void extendLicence(int extendedForYears) {
        LocalDateTime LicencedUntilDate = getLicencedUntil()
                .orElseThrow(() -> new LicenseExpiredException(this.toString() + " has no licence."));
        LocalDateTime newLicencedUntilDate = LicencedUntilDate.plusYears(extendedForYears);
        setLicencedUntil(newLicencedUntilDate);
    }

    @Override
    public void extendLicence() {
        int extendedFor = getDefaultLicencePeriod();
        LocalDateTime LicencedUntilDate = getLicencedUntil()
                .orElseThrow(() -> new LicenseExpiredException(this.toString() + " has no licence."));
        LocalDateTime newLicencedUntilDate = LicencedUntilDate.plusYears(extendedFor);
        setLicencedUntil(newLicencedUntilDate);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractBank)) return false;
        AbstractBank that = (AbstractBank) o;
        return bic.equals(that.bic);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bic);
    }

    @Override
    public String toString() {
        return "AbstractBank{" +
                "name='" + name + '\'' +
                ", assets=" + assets +
                ", liabilities=" + liabilities +
                ", licencedUntil=" + licencedUntil +
                ", subsidiaryBanks=" + subsidiaryBanks +
                ", BIC='" + bic + '\'' +
                '}';
    }
}
