package com.qanda;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    private String databaseAddress;

    public Database(String databaseAddress) throws ClassNotFoundException {
        this.databaseAddress = System.getenv("JDBC_DATABASE_URL");
    }

    public Connection getConnection() throws SQLException {
        /*
        Properties props = new Properties();
        props.setProperty("user", "postgres");
        props.setProperty("password", "root");
        props.setProperty("ssl", "false");
        */
        return DriverManager.getConnection(databaseAddress);
    }
}