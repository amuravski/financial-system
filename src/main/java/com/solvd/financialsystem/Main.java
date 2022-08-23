package com.solvd.financialsystem;

import com.solvd.financialsystem.domain.*;
import com.solvd.financialsystem.domain.bank.*;
import com.solvd.financialsystem.domain.company.AbstractCompany;
import com.solvd.financialsystem.domain.company.CJSC;
import com.solvd.financialsystem.domain.company.JSC;
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
import java.lang.reflect.*;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.rmi.UnexpectedException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
        names.forEach(name -> sharesPerHolders.put(name, rand.nextInt() % 100 + 105));
        company.setSharesPerHolder(sharesPerHolders);
        LOGGER.info("Initialized shares per holders list: " + company.getSharesPerHolder());
        LOGGER.info("Sorted shares per holders list: " + sortMapByValue(company.getSharesPerHolder(), SortOrder.DESCENDING));

        RandomBicSupplier randomBicSupplier = () -> String.valueOf(rand.nextInt() % 100000000);
        financialSystem.getBanks().forEach((existingBank) ->
                existingBank.setBic(randomBicSupplier.getRandomBic()));

        Set<AbstractBank> existingBanks = new HashSet<>(financialSystem.getBanks());
        LOGGER.info("Bank in set: " + existingBanks.size());
        existingBanks.addAll(financialSystem.getBanks());
        LOGGER.info("Bank in set: " + existingBanks.size());
        CommercialBank bankWithTheSameBic = new CommercialBank("NovaBank");
        bankWithTheSameBic.setBic(financialSystem.getBanks().get(0).getBic());
        existingBanks.add(bankWithTheSameBic);
        LOGGER.info("Bank in set: " + existingBanks.size());

        //countWordsInBook("https://www.gutenberg.org/cache/epub/5089/pg5089.txt");

        RandomCompanySupplier randomCompanySupplier = () -> {
            AbstractCompany generatedCompany = null;
            String randomName = String.valueOf(rand.nextInt() % 100000);
            switch (rand.nextInt(3)) {
                case 0:
                    generatedCompany = new CJSC(randomName);
                    break;
                case 1:
                    generatedCompany = new JSC(randomName);
                    break;
                case 2:
                    generatedCompany = new LLC(randomName);
                    break;
            }
            generatedCompany.setAssets(BigDecimal.valueOf(100 + rand.nextInt(100)));
            generatedCompany.setLiabilities(BigDecimal.valueOf(rand.nextInt(100)));
            return generatedCompany;
        };
        for (int i = 0; i < 20; i++) {
            financialSystem.addActor(randomCompanySupplier.getRandomCompany());
        }
        financialSystem.getCompanies()
                .forEach(existingCompany -> existingCompany.setType(rand.nextBoolean() ? AbstractCompany.Type.COMMERCIAL : AbstractCompany.Type.NONCOMMERCIAL));
        reportCompanyTypes(financialSystem);

        List<Individual> individuals = new ArrayList<>();
        RandomIndividualSupplier randomIndividualSupplier = () -> {
            Individual newIndividual = new Individual(String.valueOf(rand.nextInt() % 100000));
            newIndividual.setType(Individual.Type.values()[rand.nextInt(5)]);
            if (newIndividual.getType() != Individual.Type.ADULT) {
                newIndividual.getType().setEconomicallyActive(rand.nextInt(100) > 90);
            }
            return newIndividual;
        };
        for (int i = 0; i < 1000; i++) {
            individuals.add(randomIndividualSupplier.getRandomIndividual());
        }
        financialSystem.setIndividuals(individuals);
        reportIndividualTypes(financialSystem);

        try {
            String classname = "com.solvd.financialsystem.domain.company.JSC";
            Class<?> cl = Class.forName(classname);
            LOGGER.info(cl.getSimpleName() + " is a subclass of " + cl.getGenericSuperclass());
            LOGGER.info(cl.getSimpleName() + " implements" + Arrays.toString(cl.getGenericInterfaces()));
            LOGGER.info(cl.getSimpleName() + " has " + Modifier.toString(cl.getModifiers()) + " access modifier.");
            LOGGER.info("Methods: ");
            Arrays.stream(cl.getDeclaredMethods())
                    .forEach(method ->
                            LOGGER.info(Modifier.toString(cl.getModifiers()) + " " +
                                    method.getReturnType() + " " +
                                    method.getName() + "(" +
                                    Arrays.toString(
                                            method.getGenericParameterTypes()).replaceAll("[\\[\\]]", "") +
                                    ")"));
            Constructor<?> constructor = cl.getConstructor(String.class);
            AbstractCompany instance = (AbstractCompany) constructor.newInstance("Reflexive");
            Method setPublicShares = cl.getDeclaredMethod("setPublicShares", int.class);
            setPublicShares.invoke(instance, 22);
            Method setAssets = cl.getMethod("setAssets", BigDecimal.class);
            setAssets.invoke(instance, BigDecimal.valueOf(114));
            Method setLiabilities = cl.getMethod("setLiabilities", BigDecimal.class);
            setLiabilities.invoke(instance, BigDecimal.valueOf(100));

            Arrays.stream(cl.getDeclaredMethods()).forEach(method -> {
                if (method.getGenericParameterTypes().length == 0) {
                    try {
                        LOGGER.info("Calling " + method.getName());
                        if (!method.getReturnType().getName().equals("void")) {
                            LOGGER.info(method.invoke(instance));
                        }
                        method.invoke(instance);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        LOGGER.error(e.getMessage(), e);
                    }
                }
            });
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        Supplier<Integer> integerSupplier = rand::nextInt;
        Consumer<Number> printer = LOGGER::info;
        printer.accept(integerSupplier.get());
        BiConsumer<Integer, Integer> doublePrinter = (firstInt, secondInt) -> LOGGER.info(firstInt + " " + secondInt);
        doublePrinter.accept(integerSupplier.get(), integerSupplier.get());
        BiFunction<Integer, Integer, Double> divider = (dividend, divisor) -> (double) dividend / divisor;
        printer.accept(divider.apply(integerSupplier.get(), integerSupplier.get()));
        IntStream.
        //UnaryOperator squarer
    }
}
