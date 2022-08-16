package com.solvd.financialsystem;

import com.solvd.financialsystem.domain.Conference;
import com.solvd.financialsystem.domain.FinancialSystem;
import com.solvd.financialsystem.domain.bank.*;
import com.solvd.financialsystem.domain.company.AbstractCompany;
import com.solvd.financialsystem.domain.company.LLC;
import com.solvd.financialsystem.domain.exception.IllegalAmountOfMembersException;
import com.solvd.financialsystem.domain.exchange.AbstractExchange;
import com.solvd.financialsystem.domain.exchange.StockExchange;
import com.solvd.financialsystem.domain.fund.AbstractFund;
import com.solvd.financialsystem.domain.fund.MutualFund;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.rmi.UnexpectedException;
import java.time.LocalDateTime;
import java.util.*;

import static com.solvd.financialsystem.utils.Utils.*;

public class Main {

    static {
        System.setProperty("log4j.configurationFile", "log4j2.xml");
    }

    private static final Logger LOGGER = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        LocalDateTime defaultLicensedUntilDate = LocalDateTime.now().plusYears(5);
        CentralBank centralBank = new CentralBank(new BigDecimal("1.5"), new BigDecimal("2.5"), LocalDateTime.now());
        FinancialSystem financialSystem = new FinancialSystem(centralBank);
        financialSystem.setBanks(new ArrayList<>());
        financialSystem.setCompanies(new ArrayList<>());
        financialSystem.setExchanges(new ArrayList<>());
        financialSystem.setFunds(new ArrayList<>());
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
        company.setSharesPerHolder(new HashMap<>());
        AbstractBank bank = new CommercialBank("FastBank", new BigDecimal("555555.0"), new BigDecimal("43434.0"));
        bank.setLicencedUntil(LocalDateTime.now().plusYears(2));
        financialSystem.addActor(bank);
        BigDecimal loanAmount = new BigDecimal("260.0");

        LOGGER.info("Is company " + company.getName() + " allowed to borrow " + loanAmount.toString() +
                " from a bank " + bank.getName() + ": " + applyForLoan(bank, company, loanAmount));

        financialSystem.addActor(bank);
        financialSystem.addActor(company);
        LOGGER.info("Current key rate: " + centralBank.getKeyRate());
        BigDecimal inflation = new BigDecimal("5.3");
        LOGGER.info("Current inflation: " + inflation.toString());
        centralBank.setCurrentInflation(inflation);
        LOGGER.info("Central bank meeting occurred.");
        centralBank.meet();
        LOGGER.info("Current key rate: " + centralBank.getKeyRate());

        AbstractExchange stockExchange = new StockExchange("NYSE", LocalDateTime.now().plusYears(5), new BigDecimal("0.02"));
        financialSystem.addActor(stockExchange);
        LOGGER.info(stockExchange);

        AbstractFund mutualFund = new MutualFund("Vanguard", new BigDecimal("100000.0"), new BigDecimal("100000.0"));
        financialSystem.addActor(mutualFund);
        LOGGER.info(mutualFund);

        InsuranceBank insuranceBank = new InsuranceBank("InsurAce");
        insuranceBank.setLicencedUntil(defaultLicensedUntilDate);
        financialSystem.addActor(insuranceBank);
        InvestmentBank investmentBank = new InvestmentBank("Goldman");
        investmentBank.setLicencedUntil(defaultLicensedUntilDate);
        InvestmentBank biggerInvestmentBank = new InvestmentBank("Sachs");
        biggerInvestmentBank.setLicencedUntil(defaultLicensedUntilDate);
        biggerInvestmentBank.setSubsidiaryBanks(new ArrayList<>());
        biggerInvestmentBank.addSubsidiary(investmentBank);
        MortgageBank mortgageBank = new MortgageBank("Everyhome");
        mortgageBank.setLicencedUntil(defaultLicensedUntilDate);
        financialSystem.addActor(investmentBank);
        financialSystem.addActor(biggerInvestmentBank);
        financialSystem.addActor(mortgageBank);
        LOGGER.info("Big investment bank: " + biggerInvestmentBank);
        LOGGER.info("It's subsidiary: " + investmentBank);
        LOGGER.info(mortgageBank);
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

        Map<String, Integer> sharesPerHolders = company.getSharesPerHolder();
        List<String> names = List.of("Nojus Salas", "Shakeel Fitzgerald", "Abdur-Rahman Welsh", "Sama Davis", "Gertrude Harris", "Halimah Young", "Kieran Bouvet", "Bernadette Booker", "Kerys Hayes", "Evie-Rose Mckenna");
        Random rand = new Random();
        for (String name : names) {
            sharesPerHolders.put(name, rand.nextInt() % 100 + 105);
        }
        company.setSharesPerHolder(sharesPerHolders);
        LOGGER.info("Initialized shares per holders list: " + company.getSharesPerHolder());
        LOGGER.info("Sorted shares per holders list: " + sortShareholders(company));

        for (AbstractBank existingBank : financialSystem.getBanks()) {
            existingBank.setBic(String.valueOf(rand.nextInt() % 100000000));
        }
        Set<AbstractBank> banks = new HashSet<>(financialSystem.getBanks());
        LOGGER.info("Bank in set: " + banks.size());
        banks.addAll(financialSystem.getBanks());
        LOGGER.info("Bank in set: " + banks.size());
        CommercialBank bankWithTheSameBic = new CommercialBank("NovaBank");
        bankWithTheSameBic.setBic(financialSystem.getBanks().get(0).getBic());
        banks.add(bankWithTheSameBic);
        LOGGER.info("Bank in set: " + banks.size());

    }
}
