package org.service;

import org.dto.ExchangeRateDTO;
import org.model.ExchangeRate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ExchangeRateService {
    double convert(double rate, double amount);
    Optional<ExchangeRate> getExchangeRate(String base_currency_code, String target_currency_code);
    List<ExchangeRateDTO> getAllExchangeRates();
    void update(String base_currency_code, String target_currency_code, double rate);
    void save(String base_currency_code, String target_currency_code, BigDecimal rate);


}
