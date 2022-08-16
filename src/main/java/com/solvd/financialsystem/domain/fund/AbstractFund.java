package com.solvd.financialsystem.domain.fund;

import com.solvd.financialsystem.domain.FinancialActor;

import java.math.BigDecimal;
import java.util.Objects;

public class AbstractFund implements FinancialActor {

    private String name;
    private BigDecimal assets;

    public AbstractFund(String name) {
        this.name = name;
    }

    public AbstractFund(String name, BigDecimal assets) {
        this.name = name;
        this.assets = assets;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getAssets() {
        return assets;
    }

    public void setAssets(BigDecimal assets) {
        this.assets = assets;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractFund)) return false;
        AbstractFund other = (AbstractFund) o;
        return name.equals(other.name) &&
                Objects.equals(assets, other.assets);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, assets);
    }

    @Override
    public String toString() {
        return "AbstractFund{" +
                "name='" + name + '\'' +
                ", assets=" + assets +
                '}';
    }
}
