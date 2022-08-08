import domain.Conference;
import domain.FinancialSystem;
import domain.bank.*;
import domain.company.AbstractCompany;
import domain.company.LLC;
import domain.exception.IllegalAmountOfMembers;
import domain.exchange.AbstractExchange;
import domain.exchange.StockExchange;
import domain.fund.AbstractFund;
import domain.fund.MutualFund;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import java.math.BigDecimal;
import java.rmi.UnexpectedException;
import java.time.LocalDateTime;
import java.util.Arrays;

import static utils.Utils.*;

public class Main {

    static {
        System.setProperty("log4j.configurationFile", "log4j2.xml");
    }

    private static final Logger logger = (Logger) LogManager.getRootLogger();

    public static void main(String[] args) {
        AbstractCompany company = null;
        try {
            company = new LLC("OOO Minsk", new BigDecimal("520.0"), new BigDecimal("250.0"), 12);
        } catch (IllegalAmountOfMembers illegalAmountOfMembers) {
            logger.error(illegalAmountOfMembers);
        } finally {
            if (company != null) {
                logger.info("Company " + company + " was successfully created.");
            }
        }
        AbstractBank bank = new CommercialBank("FastBank", new BigDecimal("555555.0"), new BigDecimal("43434.0"));
        bank.setLicencedUntil(LocalDateTime.now().plusYears(2));
        CentralBank centralBank = new CentralBank(new BigDecimal("1.5"), new BigDecimal("2.5"), LocalDateTime.now());
        FinancialSystem financialSystem = new FinancialSystem(centralBank);
        financialSystem.addBank(bank);
        financialSystem.addCompany(company);
        BigDecimal loanAmount = new BigDecimal("260.0");

        logger.info("Is company " + company.getName() + " allowed to borrow " + loanAmount.toString() +
                " from a bank " + bank.getName() + ": " + applyForLoan(bank, company, loanAmount));
        logger.info("Current key rate: " + centralBank.getKeyRate());

        BigDecimal inflation = new BigDecimal("5.3");

        logger.info("Current inflation: " + inflation.toString());

        centralBank.setCurrentInflation(inflation);

        logger.info("Central bank meeting occurred.");

        centralBank.meet();

        logger.info("Current key rate: " + centralBank.getKeyRate());

        AbstractExchange stockExchange = new StockExchange("NYSE", LocalDateTime.now().plusYears(5), new BigDecimal("0.02"));
        financialSystem.addExchange(stockExchange);

        logger.info(stockExchange);

        AbstractFund mutualFund = new MutualFund("Vanguard", new BigDecimal("100000.0"), new BigDecimal("100000.0"));
        financialSystem.addFund(mutualFund);

        logger.info(mutualFund);

        InsuranceBank insuranceBank = new InsuranceBank("InsurAce", new BigDecimal("189423.0"), new BigDecimal("99123.0"), LocalDateTime.now().plusYears(5), new BigDecimal("0.03"));
        financialSystem.addBank(insuranceBank);
        InvestmentBank investmentBank = new InvestmentBank("Goldman", new BigDecimal("64545645.0"), new BigDecimal("99123.0"), LocalDateTime.now().plusYears(5), new BigDecimal("65498491.0"), new BigDecimal("0.025"));
        InvestmentBank biggerInvestmentBank = new InvestmentBank("Sachs", new BigDecimal("164545645"), new BigDecimal("199123.0"), LocalDateTime.now().plusYears(5), new BigDecimal("165498491.0"), new BigDecimal("0.025"));
        biggerInvestmentBank.addSubsidiary(investmentBank);
        MortgageBank mortgageBank = new MortgageBank("Everyhome", new BigDecimal("15654"), new BigDecimal("2222.0"), LocalDateTime.now().plusYears(5), new BigDecimal("0.05"), new BigDecimal("0.2"));
        financialSystem.addBank(investmentBank);
        financialSystem.addBank(biggerInvestmentBank);
        financialSystem.addBank(mortgageBank);

        logger.info("Big investment bank: " + biggerInvestmentBank);
        logger.info("It's subsidiary: " + investmentBank);
        logger.info(mortgageBank);
        logger.info("All the banks in financial system: " + Arrays.toString(financialSystem.getBanks()) + "\n");

        holdAllMeetings(financialSystem);
        extendAllLicences(financialSystem);
        updateAllRequiredReserves(financialSystem, BigDecimal.valueOf(0.05));
        publishReports(financialSystem);

        try (Conference conference = new Conference()) {
            conference.holdConference(0);
        } catch (UnexpectedException e) {
            logger.error(e);
        } finally {
            logger.info("holdConference() called.");
        }
    }
}
