package com.general.daologic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectDAO {
    public Connection databaseLink;

    public Connection getConnection() {
        try {
            String databaseName = System.getenv("ELDBName");
            String databaseUser = System.getenv("ELDBUsername");
            String databasePassword = System.getenv("ELDBPassword");
            String url = "jdbc:postgresql://localhost:5432/" + databaseName;

            Class.forName("org.postgresql.Driver");

            databaseLink = DriverManager.getConnection(url, databaseUser, databasePassword);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return databaseLink;
    }
}
