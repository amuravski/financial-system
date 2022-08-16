package com.solvd.financialsystem.domain;

import java.math.BigDecimal;
import java.util.Objects;

public class Individual implements FinancialActor {

    private String name;
    private int currentOccupationTime;
    private BigDecimal monthlyIncome;

    public Individual(String name, int currentOccupationTime, BigDecimal monthlyIncome) {
        this.name = name;
        this.currentOccupationTime = currentOccupationTime;
        this.monthlyIncome = monthlyIncome;
    }

    public Individual(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCurrentOccupationTime() {
        return currentOccupationTime;
    }

    public void setCurrentOccupationTime(int currentOccupationTime) {
        this.currentOccupationTime = currentOccupationTime;
    }

    public BigDecimal getMonthlyIncome() {
        return monthlyIncome;
    }

    public void setMonthlyIncome(BigDecimal monthlyIncome) {
        this.monthlyIncome = monthlyIncome;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Individual)) return false;
        Individual other = (Individual) o;
        return currentOccupationTime == other.currentOccupationTime &&
                name.equals(other.name) &&
                Objects.equals(monthlyIncome, other.monthlyIncome);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, currentOccupationTime, monthlyIncome);
    }

    @Override
    public String toString() {
        return "Individual{" +
                "name='" + name + '\'' +
                ", currentOccupationTime=" + currentOccupationTime +
                ", monthlyIncome=" + monthlyIncome +
                '}';
    }
}
