package org.service;

import org.dto.CurrencyDTO;
import org.model.Currency;

import java.util.List;
import java.util.Optional;

public interface CurrencyService {
    List<Currency> getAllCurrency();
    Optional<Currency> getCurrencyByCode(String currencyCode);
    void saveCurrency(CurrencyDTO currencyDTO);


}
