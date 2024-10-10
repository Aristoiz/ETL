package org.example;

import java.sql.*;
import java.util.ArrayList;

public class factSales {
    public factSales() {
        Conexion conn = new Conexion();
        ConexionDWH connDWH = new ConexionDWH();
        ArrayList<String[]> factsales = new ArrayList<>();

        try (Statement stm = conn.getConection().createStatement();
             ResultSet rs = stm.executeQuery("SELECT date_part('year', s.date_sale), date_part('month', s.date_sale), cl.id_client, sp.id_product, s.sale_paid, s.articles, s.id_sale " +
                     "FROM sale s, card c, product p, client cl, sale_product sp " +
                     "WHERE s.id_sale = sp.id_sale " +
                     "AND sp.id_product = p.id_product " +
                     "AND s.id_card = c.id_card " +
                     "AND c.id_client = cl.id_client;")) {

            while (rs.next()) {
                try (PreparedStatement stmt = connDWH.getConection().prepareStatement("SELECT idfecha FROM dimfechas WHERE anio = ? AND mes = ?")) {
                    stmt.setInt(1, Integer.parseInt(rs.getString(1)));
                    stmt.setInt(2, Integer.parseInt(rs.getString(2)));
                    try (ResultSet rs2 = stmt.executeQuery()) {
                        if (rs2.next()) {
                            factsales.add(new String[]{rs2.getString(1), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7)});
                        } else {
                            System.out.println("No se encontró la fecha para el año " + rs.getString(1) + " y el mes " + rs.getString(2));
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Inserción en batch
        String insertQuery = "INSERT INTO factSales(idfecha, idclient, idproduct, sale_paid, articles, codigoventa) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connDWH.getConection().prepareStatement(insertQuery)) {
            for (String[] fact : factsales) {
                pstmt.setInt(1, Integer.parseInt(fact[0]));
                pstmt.setInt(2, Integer.parseInt(fact[1]));
                pstmt.setInt(3, Integer.parseInt(fact[2]));
                pstmt.setFloat(4, Float.parseFloat(fact[3]));
                pstmt.setInt(5, Integer.parseInt(fact[4]));
                pstmt.setInt(6, Integer.parseInt(fact[5]));
                pstmt.addBatch();
            }
            pstmt.executeBatch(); // Ejecuta el lote
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
