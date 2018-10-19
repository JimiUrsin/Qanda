package com.qanda;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    private String databaseAddress;

    public Database() throws ClassNotFoundException {
        this.databaseAddress = System.getenv("JDBC_DATABASE_URL"); // Korvaa hardcodettu databaseAddressillä jos pyöritetään lokaalisti
    }

    public Connection getConnection() throws SQLException {
        /* Lokaalia pyöritystä varten
        Properties props = new Properties();
        props.setProperty("user", "postgres");
        props.setProperty("password", "root");
        props.setProperty("ssl", "false");
        */
        return DriverManager.getConnection(databaseAddress);
    }
}