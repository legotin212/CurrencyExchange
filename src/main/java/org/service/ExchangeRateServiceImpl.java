package org.service;

import org.dao.ExchangeRatesDAOImpl;
import org.dao.ExchangeRatesDAO;
import org.dto.ExchangeRateDTO;
import org.model.ExchangeRate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class ExchangeRateServiceImpl implements ExchangeRateService {
    private final ExchangeRatesDAO exchangeRatesDAO;
    private static ExchangeRateServiceImpl instance;

    public static synchronized ExchangeRateServiceImpl getInstance(){
        if (instance == null) {
            instance = new ExchangeRateServiceImpl(ExchangeRatesDAOImpl.getInstance())  ;
        }
        return instance;
    }

    private ExchangeRateServiceImpl(ExchangeRatesDAO exchangeRatesDAO) {
        this.exchangeRatesDAO = exchangeRatesDAO;
    }

    @Override
    public double convert(double rate, double amount) {
        return rate * amount;
    }

    @Override
    public Optional<ExchangeRate> getExchangeRate(String base_currency_code, String target_currency_code) {
        return  exchangeRatesDAO.get(base_currency_code,target_currency_code);
    }

    @Override
    public List<ExchangeRateDTO> getAllExchangeRates() {
        return exchangeRatesDAO.getAll();
    }

    @Override
    public void update(String base_currency_code, String target_currency_code, double rate) {
        exchangeRatesDAO.update(base_currency_code,target_currency_code,rate);
    }

    @Override
    public void save(String base_currency_code, String target_currency_code, BigDecimal rate) {
        exchangeRatesDAO.save(base_currency_code,target_currency_code,rate);
    }

}
