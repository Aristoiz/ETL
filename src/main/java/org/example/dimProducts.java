package org.example;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class dimProducts {
    public dimProducts() {
        Conexion conn = new Conexion();
        ConexionDWH connDWH = new ConexionDWH();
        // SELECT (id_client, country, job_title, gender) FROM clients order by id_client;
        ArrayList<String[]> product = new ArrayList<>();
        try {
            Statement stmt = conn.getConection().createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id_product,product.product FROM product order by id_product;");
            while (rs.next()) {
                product.add(new String[]{rs.getString("product")});
            }
            try (PreparedStatement stmt2 = connDWH.getConection().prepareStatement("INSERT INTO dimproducts(product) VALUES (?);")) {
                for (String[] prod : product) {
                    stmt2.setString(1, prod[0]);
                    stmt2.addBatch();
                }
                stmt2.executeBatch();
            }catch (SQLException e) {
                System.out.println("Error: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
