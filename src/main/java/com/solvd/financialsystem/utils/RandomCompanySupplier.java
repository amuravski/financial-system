package com.solvd.financialsystem.utils;

import com.solvd.financialsystem.domain.company.AbstractCompany;
import com.solvd.financialsystem.domain.company.CJSC;
import com.solvd.financialsystem.domain.company.JSC;
import com.solvd.financialsystem.domain.company.LLC;

import java.math.BigDecimal;
import java.util.Random;
import java.util.function.Supplier;

public class RandomCompanySupplier implements Supplier<AbstractCompany> {

    @Override
    public AbstractCompany get() {
        AbstractCompany generatedCompany = null;
        Random rand = new Random();
        String randomName = String.valueOf(rand.nextInt() % 100000);
        switch (rand.nextInt(3)) {
            case 0:
                generatedCompany = new CJSC(randomName);
                break;
            case 1:
                generatedCompany = new JSC(randomName);
                break;
            case 2:
                generatedCompany = new LLC(randomName);
                break;
        }
        generatedCompany.setAssets(BigDecimal.valueOf(100 + rand.nextInt(100)));
        generatedCompany.setLiabilities(BigDecimal.valueOf(rand.nextInt(100)));
        generatedCompany.setType(rand.nextBoolean() ? AbstractCompany.Type.COMMERCIAL : AbstractCompany.Type.NONCOMMERCIAL);
        return generatedCompany;
    }
}
