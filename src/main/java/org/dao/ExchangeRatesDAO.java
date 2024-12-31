package org.dao;

import org.dto.ExchangeRateDTO;
import org.model.ExchangeRate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ExchangeRatesDAO {
     void save(String base_currency_code, String target_currency_code, BigDecimal rate);
     Optional<ExchangeRate> get(String base_currency_code, String target_currency_code);
     List<ExchangeRateDTO> getAll();
     void update(String base_currency_code, String target_currency_code, double rate);
}
