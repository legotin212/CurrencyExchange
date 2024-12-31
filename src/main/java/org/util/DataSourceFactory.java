package org.util;

import org.sqlite.SQLiteDataSource;

import javax.sql.DataSource;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

public class DataSourceFactory {
    private static DataSourceFactory instance = null;
    private DataSourceFactory() {
    }

    public static DataSourceFactory getInstance() {
        if(instance==null){
            instance = new DataSourceFactory();
        }
        return instance;
    }

    public DataSource getDataSource() throws URISyntaxException {
        SQLiteDataSource sqLiteDataSource = new SQLiteDataSource();
        sqLiteDataSource.setUrl("jdbc:sqlite::resource:CurrencyExchange.db");
        return sqLiteDataSource;
}}