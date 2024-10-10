package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Conexion {

    public Conexion() {
        connx = Conexion();
    }
    static Connection connx;
    public static Connection Conexion() {
        try {
            String url ="jdbc:postgresql://localhost:5432/sales";
            Properties properties = new Properties();
            properties.setProperty("user", "*******");
            properties.setProperty("password", "****");
            Connection conn = DriverManager.getConnection(url, properties);
            return conn;
        }
        catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }

    }
    public Connection getConection() {
        return connx;
    }
}
