package com.solvd.financialsystem.domain.company;

import com.solvd.financialsystem.domain.FinancialActor;
import com.solvd.financialsystem.domain.Meetable;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;

public abstract class AbstractCompany implements FinancialActor, Meetable {

    private String name;
    private BigDecimal assets;
    private BigDecimal liabilities;
    private Map<String, Integer> sharesPerHolder;
    private Type type;

    public enum Type {

        COMMERCIAL,
        NONCOMMERCIAL

    }

    public AbstractCompany(String name, BigDecimal assets, BigDecimal liabilities) {
        this.name = name;
        this.assets = assets;
        this.liabilities = liabilities;
    }

    public AbstractCompany(String name) {
        this.name = name;
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

    public BigDecimal getLiabilities() {
        return liabilities;
    }

    public void setLiabilities(BigDecimal liabilities) {
        this.liabilities = liabilities;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Map<String, Integer> getSharesPerHolder() {
        return sharesPerHolder;
    }

    public void setSharesPerHolder(Map<String, Integer> sharesPerHolder) {
        this.sharesPerHolder = sharesPerHolder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractCompany that = (AbstractCompany) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(assets, that.assets) &&
                Objects.equals(liabilities, that.liabilities) &&
                Objects.equals(sharesPerHolder, that.sharesPerHolder) &&
                type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, assets, liabilities, sharesPerHolder, type);
    }

    @Override
    public String toString() {
        return "AbstractCompany{" +
                "name='" + name + '\'' +
                ", assets=" + assets +
                ", liabilities=" + liabilities +
                ", sharesPerHolder=" + sharesPerHolder +
                ", companyType=" + type +
                '}';
    }

    public abstract void meet();
}
