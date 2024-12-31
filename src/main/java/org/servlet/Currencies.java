package org.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.dto.CurrencyDTO;
import org.exception.WrongArgumentsException;
import org.model.Currency;
import org.service.CurrencyService;
import org.service.CurrencyServiceImpl;
import org.util.validator.CurrencyValidator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@WebServlet(urlPatterns = ("/currencies"))

public class Currencies extends HttpServlet {
    private final CurrencyService currencyService  = CurrencyServiceImpl.getInstance();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final CurrencyValidator currencyValidator = new CurrencyValidator();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Currency> result = currencyService.getAllCurrency();
        objectMapper.writeValue(resp.getWriter(), result);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {

        String name = request.getParameter("name");
        String code = request.getParameter("code");
        String sign = request.getParameter("sign");

        String validationResult = currencyValidator.validate(name, code, sign);

        if (validationResult != null) {
          throw new WrongArgumentsException(validationResult);
        }

        currencyService.saveCurrency(new CurrencyDTO(code, name, sign));
        Optional<Currency> result = currencyService.getCurrencyByCode(code);

        if (result.isPresent()) {
            resp.setStatus(HttpServletResponse.SC_OK);
            objectMapper.writeValue(resp.getWriter(), result);
        }
    }
}
