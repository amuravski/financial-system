package utils;

import domain.*;
import domain.bank.AbstractBank;
import domain.company.AbstractCompany;
import domain.exchange.AbstractExchange;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import java.math.BigDecimal;

public class Utils {

    private static final Logger logger = (Logger) LogManager.getRootLogger();

    public static boolean applyForLoan(AbstractBank bank, AbstractCompany company, BigDecimal loanAmount) {
        BigDecimal bankAssets = bank.getAssets();
        BigDecimal bankLiabilities = bank.getLiabilities();
        BigDecimal companyAssets = company.getAssets();
        BigDecimal companyLiabilities = company.getLiabilities();
        BigDecimal bankBalance = bankAssets.subtract(bankLiabilities);
        BigDecimal companyBalance = companyAssets.subtract(companyLiabilities);
        return bankBalance.compareTo(BigDecimal.valueOf(0)) > 0 &&
                companyBalance.compareTo(loanAmount) >= 0 &&
                bankBalance.compareTo(loanAmount) >= 0;
    }

    public static FinancialActor[] addElement(FinancialActor[] array, FinancialActor elementToAdd) {
        int n;
        if (array == null) {
            n = 0;
        } else {
            n = array.length;
        }
        FinancialActor[] newArray = new FinancialActor[n + 1];
        for (int i = 0; i < n; i++)
            newArray[i] = array[i];
        newArray[n] = elementToAdd;
        return newArray;
    }

    public static FinancialActor[] removeElement(FinancialActor[] array, FinancialActor elementToRemove) {
        if (array.length - 1 <= 0) {
            return new FinancialActor[0];
        }
        FinancialActor[] newArray = new FinancialActor[array.length - 1];
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals(elementToRemove)) {
                continue;
            }
            newArray[i] = array[i];
        }
        return newArray;
    }

    public static void extendAllLicences(FinancialSystem financialSystem) {
        for (AbstractBank bank : financialSystem.getBanks()) {
            bank.extendLicence();
        }
        for (AbstractExchange exchange : financialSystem.getExchanges()) {
            exchange.extendLicence();
        }
    }

    public static void publishReports(FinancialSystem financialSystem) {
        for (AbstractBank bank : financialSystem.getBanks()) {
            if (bank instanceof Reportable) {
                Reportable reportingBank = (Reportable) bank;
                reportingBank.publishReport();
            }
        }
        for (AbstractCompany company : financialSystem.getCompanies()) {
            if (company instanceof Reportable) {
                Reportable reportingCompany = (Reportable) company;
                reportingCompany.publishReport();
            }
        }
    }

    public static void holdAllMeetings(FinancialSystem financialSystem) {
        financialSystem.getCentralBank().meet();
        for (AbstractBank bank : financialSystem.getBanks()) {
            if (bank instanceof Meetable) {
                Meetable reportingBank = (Meetable) bank;
                reportingBank.meet();
            }
        }
        for (AbstractCompany company : financialSystem.getCompanies()) {
            company.meet();
        }
    }

    public static void updateAllRequiredReserves(FinancialSystem financialSystem, BigDecimal requiredReserves) {
        for (AbstractBank bank : financialSystem.getBanks()) {
            if (bank instanceof Regulatable) {
                Regulatable regulatedBank = (Regulatable) bank;
                regulatedBank.setRequiredReserves(requiredReserves);
            }
        }
        for (AbstractExchange exchange : financialSystem.getExchanges()) {
            exchange.setRequiredReserves(requiredReserves);
        }
    }
}
