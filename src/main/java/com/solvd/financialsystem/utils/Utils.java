package com.solvd.financialsystem.utils;

import com.solvd.financialsystem.domain.*;
import com.solvd.financialsystem.domain.bank.AbstractBank;
import com.solvd.financialsystem.domain.company.AbstractCompany;
import com.solvd.financialsystem.domain.company.CompanyType;
import com.solvd.financialsystem.domain.exchange.AbstractExchange;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.util.*;

public class Utils {

    private static final Logger LOGGER = LogManager.getLogger(Utils.class);

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

    public static <K, V extends Comparable<? super V>> Map<K, V> sortMapByValue(Map<K, V> map, SortOrder order) {
        List<Map.Entry<K, V>> listToSort = new ArrayList<>(map.entrySet());
        if (order == SortOrder.ASCENDING) {
            listToSort.sort(Map.Entry.comparingByValue());
        } else {
            listToSort.sort(new Comparator<Map.Entry<K, V>>() {
                @Override
                public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                    return o2.getValue().compareTo(o1.getValue());
                }
            });
        }
        Map<K, V> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : listToSort) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }

    public static void reportCompanyTypes(FinancialSystem financialSystem) {
        int commercialCompaniesCount = 0;
        int nonCommercialCompaniesCount = 0;
        CompanyType companyType;
        for (AbstractCompany company : financialSystem.getCompanies()) {
            companyType = company.getCompanyType();
            if (companyType.equals(CompanyType.COMMERCIAL))
                commercialCompaniesCount++;
            else if (companyType.equals(CompanyType.NONCOMMERCIAL)) {
                nonCommercialCompaniesCount++;
            }
        }
        LOGGER.info("There are " + commercialCompaniesCount +
                " commercial and " + nonCommercialCompaniesCount +
                " non-commercial companies in the economy.");
    }

    public static void reportIndividualTypes(FinancialSystem financialSystem) {
        int childCounter = 0;
        int pupilCounter = 0;
        int studentCounter = 0;
        int adultCounter = 0;
        int pensionerCounter = 0;
        for (Individual individual : financialSystem.getIndividuals()) {
            switch (individual.getIndividualType()) {
                case CHILD:
                    childCounter++;
                    break;
                case PUPIL:
                    pupilCounter++;
                    break;
                case STUDENT:
                    studentCounter++;
                    break;
                case ADULT:
                    adultCounter++;
                    break;
                case PENSIONER:
                    pensionerCounter++;
                    break;
            }
        }
        LOGGER.info("There are " +
                childCounter + " children " +
                pupilCounter + " pupils " +
                studentCounter + " students " +
                adultCounter + " adults and " +
                pensionerCounter + " pensioners in the economy.");
    }
}
