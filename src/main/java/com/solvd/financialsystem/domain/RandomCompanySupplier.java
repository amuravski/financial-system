package com.solvd.financialsystem.domain;

import com.solvd.financialsystem.domain.company.AbstractCompany;

@FunctionalInterface
public interface RandomCompanySupplier {

    AbstractCompany getRandomCompany();

}
