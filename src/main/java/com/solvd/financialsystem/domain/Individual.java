package com.solvd.financialsystem.domain;

import java.math.BigDecimal;
import java.util.Objects;

public class Individual implements FinancialActor {

    private String name;
    private int currentOccupationTime;
    private BigDecimal monthlyIncome;
    private Type type;

    public enum Type {

        CHILD,
        PUPIL,
        STUDENT,
        ADULT(true),
        PENSIONER;

        private boolean isEconomicallyActive;

        public boolean isEconomicallyActive() {
            return isEconomicallyActive;
        }

        public void setEconomicallyActive(boolean economicallyActive) {
            isEconomicallyActive = economicallyActive;
        }

        Type(boolean isEconomicallyActive) {
            this.isEconomicallyActive = isEconomicallyActive;
        }

        Type() {
        }
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

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Individual that = (Individual) o;
        return name.equals(that.name) &&
                type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type);
    }

    @Override
    public String toString() {
        return "Individual{" +
                "name='" + name + '\'' +
                ", currentOccupationTime=" + currentOccupationTime +
                ", monthlyIncome=" + monthlyIncome +
                ", individualType=" + type +
                '}';
    }
}
