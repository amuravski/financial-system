package domain.bank;

import domain.FinancialActor;
import domain.LicenceExtendable;
import domain.exception.LicenseExpiredException;
import domain.exception.NoSubsidiaryBankException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;

import static utils.Utils.addElement;
import static utils.Utils.removeElement;

public abstract class AbstractBank implements FinancialActor, LicenceExtendable {

    private static final int DEFAULT_BANK_LICENCE_PERIOD = 2;

    private String name;
    private BigDecimal assets;
    private BigDecimal liabilities;
    private LocalDateTime licencedUntil;
    private AbstractBank[] subsidiaryBanks;
    private String BIC;

    public AbstractBank(String name, BigDecimal assets, BigDecimal liabilities, LocalDateTime licencedUntil, AbstractBank[] subsidiaryBanks, String BIC) {
        this.name = name;
        this.assets = assets;
        this.liabilities = liabilities;
        this.licencedUntil = licencedUntil;
        this.subsidiaryBanks = subsidiaryBanks;
        this.BIC = BIC;
    }

    public AbstractBank(String name, BigDecimal assets, BigDecimal liabilities, LocalDateTime licencedUntil, AbstractBank[] subsidiaryBanks) {
        this.name = name;
        this.assets = assets;
        this.liabilities = liabilities;
        this.licencedUntil = licencedUntil;
        this.subsidiaryBanks = subsidiaryBanks;
    }

    public AbstractBank(String name, BigDecimal assets, BigDecimal liabilities, LocalDateTime licencedUntil) {
        this.name = name;
        this.assets = assets;
        this.liabilities = liabilities;
        this.licencedUntil = licencedUntil;
    }

    public AbstractBank(String name, BigDecimal assets, BigDecimal liabilities) {
        this.name = name;
        this.assets = assets;
        this.liabilities = liabilities;
    }

    public AbstractBank(String name, LocalDateTime licencedUntil, AbstractBank subsidiaryBank) {
        this.name = name;
        this.licencedUntil = licencedUntil;
        addSubsidiary(subsidiaryBank);
    }

    public AbstractBank(String name, LocalDateTime licencedUntil) {
        this.name = name;
        this.licencedUntil = licencedUntil;
    }

    public AbstractBank(String name) {
        this.name = name;
    }

    public void addSubsidiary(AbstractBank subsidiary) {
        FinancialActor[] newBanksArray = addElement(subsidiaryBanks, subsidiary);
        subsidiaryBanks = Arrays.copyOf(newBanksArray, newBanksArray.length, AbstractBank[].class);
    }

    public void removeSubsidiary(AbstractBank subsidiary) {
        this.subsidiaryBanks = (AbstractBank[]) removeElement(subsidiaryBanks, subsidiary);
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

    public AbstractBank[] getSubsidiaryBanks() {
        if (subsidiaryBanks == null) {
            throw new NoSubsidiaryBankException(this.toString() + " has no subsidiaries.");
        } else if (subsidiaryBanks.length == 0) {
            throw new NoSubsidiaryBankException(this.toString() + " has no subsidiaries.");
        }
        return subsidiaryBanks;
    }

    public void setSubsidiaryBanks(AbstractBank[] subsidiaryBanks) {
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
                ", subsidiaryBanks=" + Arrays.toString(subsidiaryBanks) +
                ", BIC='" + BIC + '\'' +
                '}';
    }
}
