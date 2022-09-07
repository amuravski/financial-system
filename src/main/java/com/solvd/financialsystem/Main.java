package com.solvd.financialsystem;

import com.solvd.financialsystem.domain.*;
import com.solvd.financialsystem.domain.bank.*;
import com.solvd.financialsystem.domain.company.AbstractCompany;
import com.solvd.financialsystem.domain.company.LLC;
import com.solvd.financialsystem.domain.connections.ConnectionPool;
import com.solvd.financialsystem.domain.exception.IllegalAmountOfMembersException;
import com.solvd.financialsystem.domain.exchange.AbstractExchange;
import com.solvd.financialsystem.domain.exchange.StockExchange;
import com.solvd.financialsystem.domain.fund.AbstractFund;
import com.solvd.financialsystem.domain.fund.MutualFund;
import com.solvd.financialsystem.utils.RandomCompanySupplier;
import com.solvd.financialsystem.utils.SortOrder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.rmi.UnexpectedException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

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
        company.setType(AbstractCompany.Type.COMMERCIAL);
        LOGGER.info("Initialized shares per holders list: " + company.getSharesPerHolder());
        LOGGER.info("Sorted shares per holders list: " + sortMapByValue(company.getSharesPerHolder(), SortOrder.DESCENDING));

        financialSystem.getBanks().forEach((existingBank) ->
                existingBank.setBic(String.valueOf(rand.nextInt() % 100000000)));
        Set<AbstractBank> existingBanks = new HashSet<>(financialSystem.getBanks());
        LOGGER.info("Bank in set: " + existingBanks.size());
        existingBanks.addAll(financialSystem.getBanks());
        LOGGER.info("Bank in set: " + existingBanks.size());
        CommercialBank bankWithTheSameBic = new CommercialBank("NovaBank");
        bankWithTheSameBic.setBic(financialSystem.getBanks().get(0).getBic());
        existingBanks.add(bankWithTheSameBic);
        LOGGER.info("Bank in set: " + existingBanks.size());

        //countWordsInBook("https://www.gutenberg.org/cache/epub/5089/pg5089.txt");
        RandomCompanySupplier randomCompanySupplier = new RandomCompanySupplier();
        Stream.iterate(0, i -> i < 20, i -> ++i)
                .forEach(x -> financialSystem.addActor(randomCompanySupplier.get()));
        reportCompanyTypes(financialSystem);

        List<Individual> individuals = IntStream.range(0, 1000)
                .boxed()
                .map(x -> {
                    Individual newIndividual = new Individual(String.valueOf(rand.nextInt() % 100000));
                    newIndividual.setType(Individual.Type.values()[rand.nextInt(5)]);
                    if (newIndividual.getType() != Individual.Type.ADULT) {
                        newIndividual.getType().setEconomicallyActive(rand.nextInt(100) > 90);
                    }
                    return newIndividual;
                })
                .collect(Collectors.toCollection(ArrayList::new));
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

        Supplier<Integer> supplier = rand::nextInt;
        Consumer<Number> printer = LOGGER::info;
        printer.accept(supplier.get());
        BiConsumer<Integer, Integer> doublePrinter = (firstInt, secondInt) -> LOGGER.info(firstInt + " " + secondInt);
        doublePrinter.accept(supplier.get(), supplier.get());
        BiFunction<Integer, Integer, Integer> divider = (a, b) -> a / b;
        printer.accept(divider.apply(supplier.get(), supplier.get()));
        UnaryOperator<Integer> squarer = (x) -> x * x;
        printer.accept(divider.andThen(squarer).apply(10, 2));
        BinaryOperator<Integer> pow = (x, y) -> (int) Math.pow(x, y);
        printer.accept(pow.apply(-2, 4));
        Predicate<Integer> isPositive = (x) -> Integer.signum(x) == 1;
        LOGGER.info(isPositive.and(x -> x > 3).test(pow.apply(-2, 2)));

        Optional<AbstractCompany> minskCompany = financialSystem.getCompanies().stream().filter(existingCompany -> existingCompany.getName().contains("Minsk")).findFirst();
        LOGGER.info("Minsk company " + (minskCompany.isPresent() ? minskCompany.get() : "not found."));
        LOGGER.info("Sum of 20: " + IntStream.range(0, 20).reduce(Integer::sum).orElse(0));
        Optional<Integer> bigNumber = IntStream.range(0, 20).boxed().map(x -> x * rand.nextInt(5)).filter(x -> x > 50).max(Integer::compareTo);
        LOGGER.info(bigNumber.map(number -> "Big number: " + number).orElse(" no big numbers found."));

        IDoAfter meet = (actor) -> ((Meetable) actor).meet();
        IDoAfter extendLicense = (actor) -> ((LicenseExtendable) actor).extendLicence();

        financialSystem.getCompanies().forEach(existingCompany -> showAndDoAfter(existingCompany, meet));
        financialSystem.getBanks().forEach(existingBank -> showAndDoAfter(existingBank, extendLicense));

        int connectionPoolSize = 10;
        ConnectionPool connectionPool = ConnectionPool.getInstance(connectionPoolSize);
        Runnable poolRunner = () -> {
            try {
                Thread.sleep(0, rand.nextInt(1));
            } catch (InterruptedException e) {
                LOGGER.error(e.getMessage(), e);
            }
            ConnectionPool.getInstance().getConnection()
                    .orElseThrow(() -> new RuntimeException("Unable to get connection"))
                    .create();
        };
        new Thread(() -> IntStream.range(0, 5).boxed()
                .forEach((x) -> new Thread(poolRunner).run()))
                .start();
        IntStream.range(0, 5).boxed().forEach((x) -> new Thread(() -> ConnectionPool.getInstance()
                .getConnection()
                .orElseThrow(() -> new RuntimeException("Unable to get connection"))
                .update()).start());

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
        }
        connectionPool.releaseAll();
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        new Thread(() -> {
        });
        IntStream.range(0, 10).boxed()
                .map(x -> executorService.submit(() -> {
                    ConnectionPool.getInstance().getConnection()
                            .orElseThrow(() -> new RuntimeException("Unable to get connection"))
                            .delete();
                }))
                .peek((x) -> {
                    try {
                        Thread.sleep(0, rand.nextInt(5));
                    } catch (InterruptedException e) {
                        LOGGER.error(e.getMessage(), e);
                    }
                })
                .forEach(future -> LOGGER.info(future + " " + (future.isDone() ? " done" : " not done yet")));
        connectionPool.releaseAll();
        IntStream.range(0, 10).boxed()
                .map(i -> CompletableFuture.supplyAsync(() -> {
                    ConnectionPool.getInstance()
                            .getConnection()
                            .orElseThrow(() -> new RuntimeException("Unable to get connection"))
                            .read();
                    return i;
                }, executorService)
                        .thenAccept(future -> LOGGER.info(future + " completable future done")))
                .forEach(future -> future.complete(null));
        executorService.shutdown();
    }
}
