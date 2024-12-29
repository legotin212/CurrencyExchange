package org.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.dto.ExchangeRateDTO;
import org.exception.WrongArgumentsException;
import org.model.ExchangeRate;
import org.service.ExchangeRateServiceImpl;
import org.service.ExchangeRateService;
import org.util.ExchangeValidator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@WebServlet("/exchangeRates")
public class ExchangeRates extends HttpServlet {
    private final ExchangeRateService exchangeRateService = ExchangeRateServiceImpl.getInstance();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ExchangeValidator exchangeValidator = new ExchangeValidator();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        List<ExchangeRateDTO> rates =  exchangeRateService.getAllExchangeRates();
        rates.forEach(System.out::println);

        objectMapper.writeValue(resp.getWriter(), rates);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String baseCurrencyCode = req.getParameter("baseCurrencyCode");
        String targetCurrencyCode = req.getParameter("targetCurrencyCode");
        double rate = Double.parseDouble(req.getParameter("rate"));
        Optional<String> validationResult = exchangeValidator.validate(baseCurrencyCode,targetCurrencyCode,rate);

        if(validationResult.isEmpty()){
            exchangeRateService.save(baseCurrencyCode, targetCurrencyCode, rate);
            Optional<ExchangeRate> result = exchangeRateService.getExchangeRate(baseCurrencyCode,targetCurrencyCode);
            objectMapper.writeValue(resp.getWriter(), result.get());
        }

        throw new WrongArgumentsException(validationResult.get());

    }
}
