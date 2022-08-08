package domain.bank;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class InsuranceBank extends CommercialBank {

    public InsuranceBank(BigDecimal assets, BigDecimal liabilities, String name, LocalDateTime licencedUntil, AbstractBank subsidiaryBank, BigDecimal requiredReserves) {
        super(name, licencedUntil, subsidiaryBank, requiredReserves);
    }

    public InsuranceBank(String name, BigDecimal assets, BigDecimal liabilities, LocalDateTime licencedUntil, BigDecimal requiredReserves) {
        super(name, licencedUntil, requiredReserves);
    }

    public InsuranceBank(String name, BigDecimal assets, BigDecimal liabilities, LocalDateTime licencedUntil, AbstractBank[] subsidiaryBanks, BigDecimal requiredReserves) {
        super(name, assets, liabilities, licencedUntil, subsidiaryBanks, requiredReserves);
    }

    public InsuranceBank(String name) {
        super(name);
    }

    public BigDecimal evaluateInsuranceCase(BigDecimal annualizedChance, BigDecimal averagePayout, BigDecimal annualInsurancePrice) {
        return annualInsurancePrice.subtract(annualInsurancePrice.multiply(averagePayout));
    }

    @Override
    public String toString() {
        return "InsuranceBank{" + super.toString() + "}";
    }
}
