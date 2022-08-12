package domain;

import domain.bank.AbstractBank;
import domain.bank.CentralBank;
import domain.company.AbstractCompany;
import domain.exchange.AbstractExchange;
import domain.fund.AbstractFund;

import java.util.ArrayList;
import java.util.List;

public class FinancialSystem {

    private final CentralBank centralBank;
    private List<AbstractBank> banks;
    private List<AbstractCompany> companies;
    private List<AbstractExchange> exchanges;
    private List<AbstractFund> funds;
    private List<Individual> individuals;

    public FinancialSystem(CentralBank centralBank) {
        this.centralBank = centralBank;
        banks = new ArrayList<>();
        companies = new ArrayList<>();
        exchanges = new ArrayList<>();
        funds = new ArrayList<>();
        individuals = new ArrayList<>();
    }

    public CentralBank getCentralBank() {
        return centralBank;
    }

    public List<AbstractBank> getBanks() {
        return banks;
    }

    public void setBanks(List<AbstractBank> banks) {
        this.banks = banks;
    }

    public List<AbstractCompany> getCompanies() {
        return companies;
    }

    public void setCompanies(List<AbstractCompany> companies) {
        this.companies = companies;
    }

    public List<AbstractExchange> getExchanges() {
        return exchanges;
    }

    public void setExchanges(List<AbstractExchange> exchanges) {
        this.exchanges = exchanges;
    }

    public List<AbstractFund> getFunds() {
        return funds;
    }

    public void setFunds(List<AbstractFund> funds) {
        this.funds = funds;
    }

    public List<Individual> getIndividuals() {
        return individuals;
    }

    public void setIndividuals(List<Individual> individuals) {
        this.individuals = individuals;
    }

    public <E extends FinancialActor> void addActor(E actorToAdd) {
        if (actorToAdd instanceof AbstractBank) {
            banks.add((AbstractBank) actorToAdd);
        } else if (actorToAdd instanceof AbstractCompany) {
            companies.add((AbstractCompany) actorToAdd);
        } else if (actorToAdd instanceof AbstractExchange) {
            exchanges.add((AbstractExchange) actorToAdd);
        } else if (actorToAdd instanceof AbstractFund) {
            funds.add((AbstractFund) actorToAdd);
        } else if (actorToAdd instanceof Individual) {
            individuals.add((Individual) actorToAdd);
        }
    }

    public <E extends FinancialActor> void removeActor(E actorToAdd) {
        if (actorToAdd instanceof AbstractBank) {
            banks.remove(actorToAdd);
        } else if (actorToAdd instanceof AbstractCompany) {
            companies.remove(actorToAdd);
        } else if (actorToAdd instanceof AbstractExchange) {
            exchanges.remove(actorToAdd);
        } else if (actorToAdd instanceof AbstractFund) {
            funds.remove(actorToAdd);
        } else if (actorToAdd instanceof Individual) {
            individuals.remove(actorToAdd);
        }
    }
}
