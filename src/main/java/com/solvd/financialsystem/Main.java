package com.solvd.financialsystem;

import com.solvd.financialsystem.domain.Conference;
import com.solvd.financialsystem.domain.FinancialSystem;
import com.solvd.financialsystem.domain.Individual;
import com.solvd.financialsystem.domain.bank.*;
import com.solvd.financialsystem.domain.company.AbstractCompany;
import com.solvd.financialsystem.domain.company.LLC;
import com.solvd.financialsystem.domain.exception.IllegalAmountOfMembersException;
import com.solvd.financialsystem.domain.exchange.AbstractExchange;
import com.solvd.financialsystem.domain.exchange.StockExchange;
import com.solvd.financialsystem.domain.fund.AbstractFund;
import com.solvd.financialsystem.domain.fund.MutualFund;
import com.solvd.financialsystem.utils.SortOrder;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.rmi.UnexpectedException;
import java.time.LocalDateTime;
import java.util.*;

import static com.solvd.financialsystem.utils.Utils.*;

public class Main {

    private static final Logger LOGGER = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        LocalDateTime defaultLicensedUntilDate = LocalDateTime.now().plusYears(5);
        CentralBank centralBank = new CentralBank(new BigDecimal("1.5"), new BigDecimal("2.5"), LocalDateTime.now());
        FinancialSystem financialSystem = new FinancialSystem(centralBank);
        List<AbstractBank> banks = new ArrayList<>();
        List<AbstractCompany> companies = new ArrayList<>();
        List<AbstractExchange> exchanges = new ArrayList<>();
        List<AbstractFund> funds = new ArrayList<>();
        AbstractCompany company = null;
        try {
            company = new LLC("OOO Minsk", new BigDecimal("520.0"), new BigDecimal("250.0"), 12);
        } catch (IllegalAmountOfMembersException e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            if (company != null) {
                LOGGER.info("Company " + company + " was successfully created.");
            }
        }
        AbstractBank bank = new CommercialBank("FastBank", new BigDecimal("555555.0"), new BigDecimal("43434.0"));
        bank.setLicencedUntil(LocalDateTime.now().plusYears(2));
        banks.add(bank);
        BigDecimal loanAmount = new BigDecimal("260.0");

        LOGGER.info("Is company " + company.getName() + " allowed to borrow " + loanAmount.toString() +
                " from a bank " + bank.getName() + ": " + applyForLoan(bank, company, loanAmount));

        companies.add(company);
        LOGGER.info("Current key rate: " + centralBank.getKeyRate());
        BigDecimal inflation = new BigDecimal("5.3");
        LOGGER.info("Current inflation: " + inflation.toString());
        centralBank.setCurrentInflation(inflation);
        LOGGER.info("Central bank meeting occurred.");
        centralBank.meet();
        LOGGER.info("Current key rate: " + centralBank.getKeyRate());

        AbstractExchange stockExchange = new StockExchange("NYSE", LocalDateTime.now().plusYears(5), new BigDecimal("0.02"));
        exchanges.add(stockExchange);
        LOGGER.info(stockExchange);

        AbstractFund mutualFund = new MutualFund("Vanguard", new BigDecimal("100000.0"), new BigDecimal("100000.0"));
        funds.add(mutualFund);
        LOGGER.info(mutualFund);

        InsuranceBank insuranceBank = new InsuranceBank("InsurAce");
        insuranceBank.setLicencedUntil(defaultLicensedUntilDate);
        banks.add(insuranceBank);
        InvestmentBank investmentBank = new InvestmentBank("Goldman");
        investmentBank.setLicencedUntil(defaultLicensedUntilDate);
        InvestmentBank biggerInvestmentBank = new InvestmentBank("Sachs");
        biggerInvestmentBank.setLicencedUntil(defaultLicensedUntilDate);
        biggerInvestmentBank.setSubsidiaryBanks(List.of(investmentBank));
        MortgageBank mortgageBank = new MortgageBank("Everyhome");
        mortgageBank.setLicencedUntil(defaultLicensedUntilDate);
        banks.add(investmentBank);
        banks.add(biggerInvestmentBank);
        banks.add(mortgageBank);
        LOGGER.info("Big investment bank: " + biggerInvestmentBank);
        LOGGER.info("It's subsidiary: " + investmentBank);
        LOGGER.info(mortgageBank);
        financialSystem.setBanks(banks);
        financialSystem.setCompanies(companies);
        financialSystem.setExchanges(exchanges);
        financialSystem.setFunds(funds);
        LOGGER.info("All the banks in financial system: " + financialSystem.getBanks() + "\n");

        holdAllMeetings(financialSystem);
        extendAllLicences(financialSystem);
        updateAllRequiredReserves(financialSystem, BigDecimal.valueOf(0.05));
        publishReports(financialSystem);

        try (Conference conference = new Conference()) {
            conference.holdConference(0);
        } catch (UnexpectedException e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            LOGGER.info("holdConference() called.");
        }

        Map<String, Integer> sharesPerHolders = new HashMap<>();
        List<String> names = List.of("Nojus Salas", "Shakeel Fitzgerald", "Abdur-Rahman Welsh", "Sama Davis", "Gertrude Harris", "Halimah Young", "Kieran Bouvet", "Bernadette Booker", "Kerys Hayes", "Evie-Rose Mckenna");
        Random rand = new Random();
        for (String name : names) {
            sharesPerHolders.put(name, rand.nextInt() % 100 + 105);
        }
        company.setSharesPerHolder(sharesPerHolders);
        LOGGER.info("Initialized shares per holders list: " + company.getSharesPerHolder());
        LOGGER.info("Sorted shares per holders list: " + sortMapByValue(company.getSharesPerHolder(), SortOrder.DESCENDING));

        for (AbstractBank existingBank : financialSystem.getBanks()) {
            existingBank.setBic(String.valueOf(rand.nextInt() % 100000000));
        }
        Set<AbstractBank> existingBanks = new HashSet<>(financialSystem.getBanks());
        LOGGER.info("Bank in set: " + existingBanks.size());
        existingBanks.addAll(financialSystem.getBanks());
        LOGGER.info("Bank in set: " + existingBanks.size());
        CommercialBank bankWithTheSameBic = new CommercialBank("NovaBank");
        bankWithTheSameBic.setBic(financialSystem.getBanks().get(0).getBic());
        existingBanks.add(bankWithTheSameBic);
        LOGGER.info("Bank in set: " + existingBanks.size());

        String fromFile = "https://www.gutenberg.org/cache/epub/5089/pg5089.txt";
        String fullText;
        File toFile = new File("book.txt");
        Map<String, Integer> wordsCount = new HashMap<>();
        try {
            FileUtils.copyURLToFile(new URL(fromFile), toFile, 10000, 10000);
            fullText = FileUtils.readFileToString(toFile, StandardCharsets.UTF_8).toLowerCase();
            for (String word : fullText.split("\\s|[\"#$%&()*+,\\-./:;<=>?@\\[\\\\\\]^_{|}~]")) {
                wordsCount.putIfAbsent(word, 0);
                wordsCount.put(word, wordsCount.get(word) + 1);
            }
            wordsCount.remove("");
            Map<String, Integer> sortedWordsCount = sortMapByValue(wordsCount, SortOrder.ASCENDING);
            LOGGER.info(sortedWordsCount);
            FileUtils.writeLines(new File("countedWords.txt"), sortedWordsCount.entrySet());
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
        for (AbstractCompany existingCompany : financialSystem.getCompanies()) {
            existingCompany.setType(rand.nextBoolean() ? AbstractCompany.Type.COMMERCIAL : AbstractCompany.Type.NONCOMMERCIAL);
        }
        reportCompanyTypes(financialSystem);
        List<Individual> individuals = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            Individual newIndividual = new Individual(String.valueOf(rand.nextInt() % 100000));
            newIndividual.setType(Individual.Type.values()[rand.nextInt(5)]);
            if (newIndividual.getType() != Individual.Type.ADULT) {
                newIndividual.getType().setEconomicallyActive(rand.nextInt(100) > 90);
            }
            individuals.add(newIndividual);
        }
        financialSystem.setIndividuals(individuals);
        reportIndividualTypes(financialSystem);
    }
}
