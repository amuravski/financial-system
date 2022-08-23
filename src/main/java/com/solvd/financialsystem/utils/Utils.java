package com.solvd.financialsystem.utils;

import com.solvd.financialsystem.domain.*;
import com.solvd.financialsystem.domain.bank.AbstractBank;
import com.solvd.financialsystem.domain.company.AbstractCompany;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        Stream.of(financialSystem.getBanks(), financialSystem.getExchanges())
                .flatMap(Collection::stream)
                .forEach(LicenseExtendable::extendLicence);
    }

    public static void publishReports(FinancialSystem financialSystem) {
        Stream.of(financialSystem.getBanks(), financialSystem.getCompanies())
                .flatMap(Collection::stream)
                .filter(financialActor -> financialActor instanceof Reportable)
                .forEach(reportableEntity -> ((Reportable) reportableEntity).publishReport());
    }

    public static void holdAllMeetings(FinancialSystem financialSystem) {
        Stream.of(financialSystem.getBanks(), financialSystem.getCompanies(), List.of(financialSystem.getCentralBank()))
                .flatMap(Collection::stream)
                .filter(financialActor -> financialActor instanceof Meetable)
                .forEach(meetableEntity -> ((Meetable) meetableEntity).meet());
    }

    public static void updateAllRequiredReserves(FinancialSystem financialSystem, BigDecimal requiredReserves) {
        Stream.of(financialSystem.getBanks(), financialSystem.getExchanges())
                .flatMap(Collection::stream)
                .filter(financialActor -> financialActor instanceof Regulatable)
                .forEach(regulatableEntity -> ((Regulatable) regulatableEntity).setRequiredReserves(requiredReserves));
    }

    public static <K, V extends Comparable<? super V>> Map<K, V> sortMapByValue(Map<K, V> map, SortOrder order) {
        return map.entrySet().stream()
                .sorted(order.getComparator())
                .collect(Collectors
                        .toMap(Map.Entry::getKey, Map.Entry::getValue, (u, v) -> u, LinkedHashMap::new));
    }

    public static void reportCompanyTypes(FinancialSystem financialSystem) {
        Map<AbstractCompany.Type, Long> companiesCountedByType = financialSystem.getCompanies().stream()
                .collect(Collectors.groupingBy(AbstractCompany::getType, Collectors.counting()));
        LOGGER.info("There are " + companiesCountedByType.get(AbstractCompany.Type.COMMERCIAL) +
                " commercial and " + companiesCountedByType.get(AbstractCompany.Type.NONCOMMERCIAL) +
                " non-commercial companies in the economy.");
    }

    public static void reportIndividualTypes(FinancialSystem financialSystem) {
        Map<Individual.Type, Long> individualsCountedByType = financialSystem.getIndividuals().stream()
                .collect(Collectors.groupingBy(Individual::getType, Collectors.counting()));
        long economicallyActiveIndividuals = financialSystem.getIndividuals().stream()
                .filter(individual -> individual.getType().isEconomicallyActive()).count();
        LOGGER.info("There are " +
                individualsCountedByType.get(Individual.Type.CHILD) + " children " +
                individualsCountedByType.get(Individual.Type.PUPIL) + " pupils " +
                individualsCountedByType.get(Individual.Type.STUDENT) + " students " +
                individualsCountedByType.get(Individual.Type.ADULT) + " adults and " +
                individualsCountedByType.get(Individual.Type.PENSIONER) + " pensioners in the economy.\n" +
                economicallyActiveIndividuals + " of them are economically active.");
    }

    public static void countWordsInBook(String link) {
        try {
            String fullText;
            File toFile = new File("book.txt");
            FileUtils.copyURLToFile(new URL(link), toFile, 10000, 10000);
            fullText = FileUtils.readFileToString(toFile, StandardCharsets.UTF_8).toLowerCase();
            Map<String, Long> wordsCount = Arrays.stream(fullText.split("\\s|[\"#$!%&()*+,\\-./:;<=>?@\\[\\\\\\]^_{|}~]"))
                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
            wordsCount.remove("");
            Map<String, Long> sortedWordsCount = sortMapByValue(wordsCount, SortOrder.ASCENDING);
            LOGGER.info(sortedWordsCount);
            FileUtils.writeLines(new File("countedWords.txt"), sortedWordsCount.entrySet());
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}
