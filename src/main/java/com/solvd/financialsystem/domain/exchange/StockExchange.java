package com.solvd.financialsystem.domain.exchange;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class StockExchange extends AbstractExchange {

    public StockExchange(String name, LocalDateTime licencedUntil, BigDecimal reservesRequired) {
        super(name, licencedUntil, reservesRequired);
    }

    public StockExchange(String name) {
        super(name);
    }
}
