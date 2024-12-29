package org.dao;

import org.dto.ExchangeRateDTO;
import org.exception.DataAccessException;
import org.exception.EntityAlreadyExistsException;
import org.exception.NotFoundException;
import org.exception.WrongArgumentsException;
import org.model.Currency;
import org.model.ExchangeRate;
import org.util.DataSourceFactory;

import javax.sql.DataSource;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExchangeRatesDAOImpl implements ExchangeRatesDAO {
    private static ExchangeRatesDAOImpl instance;

    private DataSourceFactory dataSourceFactory;
    private DataSource dataSource;


    private ExchangeRatesDAOImpl() {
        try {
            dataSourceFactory = DataSourceFactory.getInstance();
            dataSource = dataSourceFactory.getDataSource();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public static synchronized ExchangeRatesDAOImpl getInstance() {
        if (instance == null) {
            instance = new ExchangeRatesDAOImpl();
        }
        return instance;
    }

    @Override
    public void save(String base_currency_code, String target_currency_code, double rate) {
        String sql =
                "INSERT INTO ExchangeRates (BaseCurrencyId, TargetCurrencyId, Rate)" +
                " VALUES ((select ID from currencies where code = ?)," +
                "(select ID from currencies where code = ?)," +
                "?)";
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, base_currency_code);
            pstmt.setString(2, target_currency_code);
            pstmt.setDouble(3, rate);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage()
            );
            if (e.getMessage().contains("FOREIGN KEY constraint failed")) {
                throw new WrongArgumentsException("One or more currency code does not exist ");
            } else if (e.getMessage().contains("UNIQUE constraint failed")) {
                throw new EntityAlreadyExistsException("Rate for codes " + base_currency_code + target_currency_code+"  already exists ");
            }

        }
    }

    @Override
    public Optional<ExchangeRate> get(String baseCurrencyCode, String targetCurrencyCode) {
        String sql = """
                SELECT\s
                    er.ID AS exchange_rate_id,
                    bc.ID AS base_currency_id,
                    bc.Code AS base_currency_code,
                    bc.Fullname AS base_currency_FullName,
                    bc.Sign AS base_currency_sign,
                    tc.ID AS target_currency_id,
                    tc.Code AS target_currency_code,
                    tc.Fullname AS target_currency_FullName,
                    tc.Sign AS target_currency_sign,
                    er.Rate AS exchange_rate
                FROM ExchangeRates er
                JOIN currencies bc ON er.BaseCurrencyId = bc.ID
                JOIN currencies tc ON er.TargetCurrencyId = tc.ID
                WHERE bc.Code = ? AND tc.Code = ?;""";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, baseCurrencyCode);
            statement.setString(2, targetCurrencyCode);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapResultToExchangeRate(resultSet));
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error accessing database", e);
        }
    }


    @Override
    public List<ExchangeRateDTO> getAll() {
        List<ExchangeRateDTO> exchangeRates = new ArrayList<>();
        String sql = """
                SELECT\s
                    er.ID AS exchange_rate_id,
                    bc.ID, \s
                    bc.Code AS base_currency_code,
                    bc.Fullname AS base_currency_FullName,
                    bc.Sign AS base_currency_sign,
                    tc.ID,\s
                    tc.Code AS target_currency_code,
                    tc.Fullname AS target_currency_FullName,
                    tc.Sign AS target_currency_sign,
                    er.Rate AS exchange_rate
                FROM ExchangeRates er
                JOIN currencies bc ON er.BaseCurrencyId = bc.ID
                JOIN currencies tc ON er.TargetCurrencyId = tc.ID;""";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                exchangeRates.add(mapResultToExchangeRateDTO(resultSet));

            }
        } catch (SQLException e) {
            throw new DataAccessException("Error accessing database", e);
        }
        exchangeRates.forEach(System.out::println);
        return exchangeRates;
    }

    @Override
    public void update(String baseCurrencyCode, String targetCurrencyCode, double rate) {
        String sql =
                "UPDATE ExchangeRates " +
                "SET Rate = ? " +
                "WHERE BaseCurrencyId = (SELECT ID FROM currencies WHERE Code = ?) " +
                "AND TargetCurrencyId = (SELECT ID FROM currencies WHERE Code = ?)";

        try (   Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setDouble(1, rate);
            preparedStatement.setString(2, baseCurrencyCode);
            preparedStatement.setString(3, targetCurrencyCode);

            preparedStatement.executeUpdate();


        } catch (SQLException e) {
           throw new NotFoundException("No rate for " + baseCurrencyCode + " " + targetCurrencyCode);
        }
    }

    private ExchangeRateDTO mapResultToExchangeRateDTO(ResultSet resultSet) throws SQLException {//нарушает SRP
        int exchangeRateId = resultSet.getInt("exchange_rate_id");
        Currency baseCurrency = new Currency(
                resultSet.getInt("Id"),
                resultSet.getString("base_currency_code"),
                resultSet.getString("base_currency_FullName"),
                resultSet.getString("base_currency_sign"));

        Currency targetCurrency = new Currency(
                resultSet.getInt("ID"),
                resultSet.getString("target_currency_code"),
                resultSet.getString("target_currency_FullName"),
                resultSet.getString("target_currency_sign"));

        return new ExchangeRateDTO(exchangeRateId,baseCurrency, targetCurrency,
                resultSet.getDouble("exchange_rate"));
    }

    private ExchangeRate mapResultToExchangeRate(ResultSet resultSet) throws SQLException {
        return new ExchangeRate(
                resultSet.getInt("exchange_rate_id"),
                resultSet.getInt("base_currency_id"),
                resultSet.getInt("target_currency_id"),
                resultSet.getDouble("exchange_rate")
        );
    }


}
