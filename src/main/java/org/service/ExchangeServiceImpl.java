package org.service;

import org.dao.CurrencyDAO;
import org.dao.CurrencyDAOImpl;
import org.dao.ExchangeRatesDAO;
import org.dao.ExchangeRatesDAOImpl;
import org.dto.ExchangeRateDTO;
import org.dto.ExchangeResponseDTO;
import org.exception.WrongArgumentsException;
import org.model.Currency;
import org.model.ExchangeRate;
import java.util.Optional;

public class ExchangeServiceImpl implements ExchangeService {

    private final ExchangeRatesDAO exchangeRatesDAO;
    private final CurrencyDAO currencyDAO;
    private static ExchangeServiceImpl instance;



    public static synchronized ExchangeServiceImpl getInstance(){
        if (instance == null) {
            instance = new ExchangeServiceImpl(ExchangeRatesDAOImpl.getInstance(), CurrencyDAOImpl.getInstance())  ;
        }
        return instance;
    }

    private ExchangeServiceImpl(ExchangeRatesDAO exchangeRatesDAO, CurrencyDAO currencyDAO) {
        this.exchangeRatesDAO = exchangeRatesDAO;
        this.currencyDAO = currencyDAO;
    }

    @Override
    public ExchangeResponseDTO exchange(String baseCurrencyCode, String targetCurrencyCode, double amount) {

        Optional<ExchangeRate> rateDTO = exchangeRatesDAO.get(baseCurrencyCode, targetCurrencyCode);
        if(rateDTO.isPresent()){
            double rate = rateDTO.get().getRate();
            double convertedAmount = exchange(amount, rate);
            return createResponseDTO( baseCurrencyCode,  targetCurrencyCode,  amount, rate, convertedAmount);
        }

        Optional<ExchangeRate> reverseRateDTO = exchangeRatesDAO.get(targetCurrencyCode, baseCurrencyCode);
        if(reverseRateDTO.isPresent()){
            double rate = reverseRate(reverseRateDTO.get().getRate());
            double convertedAmount = exchange(amount, rate);
            return createResponseDTO( baseCurrencyCode,  targetCurrencyCode,  amount, rate, convertedAmount);
        }

        Optional<ExchangeRate> baseCurrencyToUSD = exchangeRatesDAO.get("USD", baseCurrencyCode );
        Optional<ExchangeRate> targetCurrencyToUSD = exchangeRatesDAO.get("USD", targetCurrencyCode);
        if(baseCurrencyToUSD.isPresent() && targetCurrencyToUSD.isPresent()){
            double rate = crossRateExchangeRate(baseCurrencyToUSD.get().getRate(), targetCurrencyToUSD.get().getRate());
            double convertedAmount = exchange(amount, rate);
            return createResponseDTO( baseCurrencyCode,  targetCurrencyCode,  amount, rate, convertedAmount);

        }

        throw new WrongArgumentsException("No available exchange rate found for " + baseCurrencyCode + " and " + targetCurrencyCode);
    }



    private ExchangeResponseDTO createResponseDTO(String baseCurrencyCode, String targetCurrencyCode, double amount, double rate,  double convertedAmount){
        Currency baseCurrency = currencyDAO.findByCode(baseCurrencyCode).get();
        Currency targetCurrency = currencyDAO.findByCode(targetCurrencyCode).get();

        return new ExchangeResponseDTO(baseCurrency,targetCurrency, rate, amount, convertedAmount);
    }

    private double crossRateExchangeRate(double baseCurrencyToUSDRate, double targetCurrencyToUSDRate) {
       return targetCurrencyToUSDRate/baseCurrencyToUSDRate;
    }

    private double exchange(double amount, double rate) {
        return amount * rate;
    }

    private double reverseRate(double rate){
        return 1/rate;
    }

}
