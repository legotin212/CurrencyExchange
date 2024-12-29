package org.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@ToString
public class ExchangeRate {
    @Setter
    private int id;
    private final int BaseCurrencyId;
    private final int TargetCurrencyId;
    @Setter
    private double Rate;



    public ExchangeRate(int id, int baseCurrencyId, int targetCurrencyId, double rate) {
        this.id = id;
        BaseCurrencyId = baseCurrencyId;
        TargetCurrencyId = targetCurrencyId;
        Rate = rate;
    }
}
