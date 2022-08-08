package domain;

import domain.bank.AbstractBank;
import domain.bank.CentralBank;
import domain.company.AbstractCompany;
import domain.exchange.AbstractExchange;
import domain.fund.AbstractFund;

import java.util.Arrays;

import static utils.Utils.addElement;

public class FinancialSystem {

    private final CentralBank centralBank;
    private AbstractBank[] banks;
    private AbstractCompany[] companies;
    private AbstractExchange[] exchanges;
    private AbstractFund[] funds;
    private Individual[] individuals;

    public FinancialSystem(CentralBank centralBank) {
        this.centralBank = centralBank;
    }


    public CentralBank getCentralBank() {
        return centralBank;
    }

    public AbstractBank[] getBanks() {
        return banks;
    }

    public void setBanks(AbstractBank[] banks) {
        this.banks = banks;
    }

    public AbstractCompany[] getCompanies() {
        return companies;
    }

    public void setCompanies(AbstractCompany[] companies) {
        this.companies = companies;
    }

    public AbstractExchange[] getExchanges() {
        return exchanges;
    }

    public void setExchanges(AbstractExchange[] exchanges) {
        this.exchanges = exchanges;
    }

    public AbstractFund[] getFunds() {
        return funds;
    }

    public void setFunds(AbstractFund[] funds) {
        this.funds = funds;
    }

    public Individual[] getIndividuals() {
        return individuals;
    }

    public void setIndividuals(Individual[] individuals) {
        this.individuals = individuals;
    }

    public void addBank(AbstractBank bankToAdd) {
        FinancialActor[] newBanksArray = addElement(banks, bankToAdd);
        banks = Arrays.copyOf(newBanksArray, newBanksArray.length, AbstractBank[].class);
    }

    public void addCompany(AbstractCompany companyToAdd) {
        FinancialActor[] newCompaniesArray = addElement(companies, companyToAdd);
        companies = Arrays.copyOf(newCompaniesArray, newCompaniesArray.length, AbstractCompany[].class);
    }

    public void addIndividual(Individual individualToAdd) {
        FinancialActor[] newIndividualsArray = addElement(individuals, individualToAdd);
        individuals = Arrays.copyOf(newIndividualsArray, newIndividualsArray.length, Individual[].class);
    }

    public void addExchange(AbstractExchange exchangeToAdd) {
        FinancialActor[] newExchangesArray = addElement(exchanges, exchangeToAdd);
        exchanges = Arrays.copyOf(newExchangesArray, newExchangesArray.length, AbstractExchange[].class);
    }

    public void addFund(AbstractFund fundToAdd) {
        FinancialActor[] newFundsArray = addElement(funds, fundToAdd);
        funds = Arrays.copyOf(newFundsArray, newFundsArray.length, AbstractFund[].class);
    }
}
