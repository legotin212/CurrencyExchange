package org.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;


@Getter
@ToString
public class ExchangeRate {
    @Setter
    private int id;
    private final int BaseCurrencyId;
    private final int TargetCurrencyId;
    @Setter
    private BigDecimal Rate;



    public ExchangeRate(int id, int baseCurrencyId, int targetCurrencyId, BigDecimal rate) {
        this.id = id;
        BaseCurrencyId = baseCurrencyId;
        TargetCurrencyId = targetCurrencyId;
        Rate = rate;
    }
}
