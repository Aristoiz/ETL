package org.example;

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
            for (String[] prod : product) {
                System.out.println(prod[0]);
                Statement stmt2 = connDWH.getConection().createStatement();
                stmt2.executeUpdate("INSERT INTO dimProducts(product) VALUES ('" + prod[0] + "');");
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
