package utils;

import domain.FinancialSystem;
import domain.Meetable;
import domain.Regulatable;
import domain.Reportable;
import domain.bank.AbstractBank;
import domain.company.AbstractCompany;
import domain.exchange.AbstractExchange;

import java.math.BigDecimal;
import java.util.*;

public class Utils {

    public static <E extends AbstractBank, U extends AbstractCompany> boolean applyForLoan(E bank, U company, BigDecimal loanAmount) {
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

    public static Map<String, Integer> sortShareholders(AbstractCompany company) {
        List<Map.Entry<String, Integer>> list = new ArrayList<>(company.getSharesPerHolder().entrySet());
        list.sort(new Comparator<>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });
        Map<String, Integer> sortedSharesPerHolders = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entry : list) {
            sortedSharesPerHolders.put(entry.getKey(), entry.getValue());
        }
        return sortedSharesPerHolders;
    }
}
