package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConexionDWH {
    public ConexionDWH() {
        connx = Conexion();
    }
    static Connection connx;
    public static Connection Conexion() {
        try {
            String url ="jdbc:postgresql://localhost:5432/sales_dwh";
            Properties properties = new Properties();
            properties.setProperty("user", "aristoiz");
            properties.setProperty("password", "1234");
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
