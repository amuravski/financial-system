import domain.Conference;
import domain.FinancialSystem;
import domain.bank.*;
import domain.company.AbstractCompany;
import domain.company.LLC;
import domain.exception.IllegalAmountOfMembersException;
import domain.exchange.AbstractExchange;
import domain.exchange.StockExchange;
import domain.fund.AbstractFund;
import domain.fund.MutualFund;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.rmi.UnexpectedException;
import java.time.LocalDateTime;
import java.util.Arrays;

import static utils.Utils.*;

public class Main {

    static {
        System.setProperty("log4j.configurationFile", "log4j2.xml");
    }

    private static final Logger LOGGER = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
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
        BigDecimal loanAmount = new BigDecimal("260.0");

        LOGGER.info("Is company " + company.getName() + " allowed to borrow " + loanAmount.toString() +
                " from a bank " + bank.getName() + ": " + applyForLoan(bank, company, loanAmount));
        CentralBank centralBank = new CentralBank(new BigDecimal("1.5"), new BigDecimal("2.5"), LocalDateTime.now());
        FinancialSystem financialSystem = new FinancialSystem(centralBank);
        financialSystem.addBank(bank);
        financialSystem.addCompany(company);
        LOGGER.info("Current key rate: " + centralBank.getKeyRate());
        BigDecimal inflation = new BigDecimal("5.3");
        LOGGER.info("Current inflation: " + inflation.toString());
        centralBank.setCurrentInflation(inflation);
        LOGGER.info("Central bank meeting occurred.");
        centralBank.meet();
        LOGGER.info("Current key rate: " + centralBank.getKeyRate());

        AbstractExchange stockExchange = new StockExchange("NYSE", LocalDateTime.now().plusYears(5), new BigDecimal("0.02"));
        financialSystem.addExchange(stockExchange);
        LOGGER.info(stockExchange);

        AbstractFund mutualFund = new MutualFund("Vanguard", new BigDecimal("100000.0"), new BigDecimal("100000.0"));
        financialSystem.addFund(mutualFund);
        LOGGER.info(mutualFund);

        InsuranceBank insuranceBank = new InsuranceBank("InsurAce", new BigDecimal("189423.0"), new BigDecimal("99123.0"), LocalDateTime.now().plusYears(5), new BigDecimal("0.03"));
        financialSystem.addBank(insuranceBank);
        InvestmentBank investmentBank = new InvestmentBank("Goldman", new BigDecimal("64545645.0"), new BigDecimal("99123.0"), LocalDateTime.now().plusYears(5), new BigDecimal("65498491.0"), new BigDecimal("0.025"));
        InvestmentBank biggerInvestmentBank = new InvestmentBank("Sachs", new BigDecimal("164545645"), new BigDecimal("199123.0"), LocalDateTime.now().plusYears(5), new BigDecimal("165498491.0"), new BigDecimal("0.025"));
        biggerInvestmentBank.addSubsidiary(investmentBank);
        MortgageBank mortgageBank = new MortgageBank("Everyhome", new BigDecimal("15654"), new BigDecimal("2222.0"), LocalDateTime.now().plusYears(5), new BigDecimal("0.05"), new BigDecimal("0.2"));
        financialSystem.addBank(investmentBank);
        financialSystem.addBank(biggerInvestmentBank);
        financialSystem.addBank(mortgageBank);
        LOGGER.info("Big investment bank: " + biggerInvestmentBank);
        LOGGER.info("It's subsidiary: " + investmentBank);
        LOGGER.info(mortgageBank);
        LOGGER.info("All the banks in financial system: " + Arrays.toString(financialSystem.getBanks()) + "\n");

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
    }
}
