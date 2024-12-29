package org.dao;

import org.exception.DataAccessException;
import org.exception.EntityAlreadyExistsException;
import org.model.Currency;
import org.util.DataSourceFactory;
import org.dto.CurrencyDTO;
import javax.sql.DataSource;
import java.net.URISyntaxException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CurrencyDAOImpl implements CurrencyDAO {

    private  static CurrencyDAOImpl instance;
    private final DataSourceFactory dataSourceFactory;
    private final DataSource dataSource;

    private CurrencyDAOImpl() {
        try {
            dataSourceFactory = DataSourceFactory.getInstance();
            dataSource = dataSourceFactory.getDataSource();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public static synchronized CurrencyDAOImpl getInstance() {
        if (instance == null) {
            instance = new CurrencyDAOImpl();
        }
        return instance;
    }

    @Override
    public Optional<Currency> findByCode(String currencyCode) {
        String sql = "SELECT * FROM Currencies WHERE code = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, currencyCode);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultToCurrency(rs));
            }
            return Optional.empty();

            } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
     }

    @Override
    public void save(CurrencyDTO currencyDTO) {

        String sql = "INSERT INTO Currencies (code, fullname, sign) VALUES (?, ?, ?)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, currencyDTO.currencyCode());
            preparedStatement.setString(2, currencyDTO.fullName());
            preparedStatement.setString(3, currencyDTO.sign());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new EntityAlreadyExistsException("Currency with code " + currencyDTO.currencyCode() + " already exists");
        }
    }

    @Override
    public List<Currency> findAll() {
        List<Currency> currencies = new ArrayList<>();
        Statement statement;
        ResultSet resultSet;

        String query = "SELECT * FROM Currencies";

        try (Connection connection = dataSource.getConnection()) {

            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                Currency cur = mapResultToCurrency(resultSet);
                currencies.add(cur);
            }
            return currencies;

        } catch (SQLException e) {
            throw new DataAccessException("Error accessing database", e);

        }
    }

    private  Currency mapResultToCurrency(ResultSet resultSet) throws SQLException {
        return new Currency(
                resultSet.getInt(1),
                resultSet.getString(2),
                resultSet.getString(3),
                resultSet.getString(4));
    }

}













