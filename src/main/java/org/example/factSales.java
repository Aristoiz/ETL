package org.example;

import java.sql.*;
import java.util.ArrayList;

public class factSales {
    public factSales() {
        int contador = 0;
        Conexion conn = new Conexion();
        ConexionDWH connDWH = new ConexionDWH();
        ArrayList<String[]> sales = new ArrayList<>();

        ArrayList<String[]> factsales = new ArrayList<>();

        try (Statement stm = conn.getConection().createStatement();
             ResultSet rs = stm.executeQuery("SELECT date_part('year', s.date_sale), date_part('month', s.date_sale), cl.first_name,cl.last_name,cl.country,cl.job_title,cl.gender, p.product, s.sale_paid, s.articles, s.id_sale " +
                     "FROM sale s, card c, product p, client cl, sale_product sp " +
                     "WHERE s.id_sale = sp.id_sale " +
                     "AND sp.id_product = p.id_product " +
                     "AND s.id_card = c.id_card " +
                     "AND c.id_client = cl.id_client;")) {
            while (rs.next()) {
                sales.add(new String[]{rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9), rs.getString(10), rs.getString(11)});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (String[] sale: sales){
            // Calcula el porcentaje de progreso
            int progressPercentage = (contador + 1) * 100 / sales.size();

            // Genera la barra de progreso visual con bloques
            StringBuilder progressBar = new StringBuilder("[");
            int progressBlocks = progressPercentage / 2; // Calcula el número de bloques (50 es el 100%)

            for (int i = 0; i < 50; i++) {
                if (i < progressBlocks) {
                    progressBar.append("#"); // Parte completada de la barra
                } else {
                    progressBar.append(" "); // Parte pendiente
                }
            }

            progressBar.append("] " + progressPercentage + "%");

            // Imprime la barra de progreso en la misma línea usando \r (retorno de carro)
            System.out.print("\rSale: " + (contador + 1) + " de " + sales.size() + " " + progressBar.toString());

            int idClient = 0;
            int idProduct = 0;
            int idFecha = 0;
            try (Statement stm = connDWH.getConection().createStatement();
                 ResultSet rs = stm.executeQuery("SELECT idfecha FROM dimfechas WHERE anio = " + sale[0] + " AND mes = " + sale[1] + ";")) {
                if(rs.next()) {
                    idFecha = rs.getInt(1);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try (PreparedStatement ps = connDWH.getConection().prepareStatement(
                    "SELECT idclient FROM dimclients WHERE name = ? AND lastname = ? AND country = ? AND job_title = ? AND gender = ?"))
            {

                ps.setString(1, sale[2]);
                ps.setString(2, sale[3]);
                ps.setString(3, sale[4]);
                ps.setString(4, sale[5]);
                ps.setString(5, sale[6]);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        idClient = rs.getInt(1);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try (Statement stm = connDWH.getConection().createStatement();
                 ResultSet rs = stm.executeQuery("SELECT idproduct FROM dimproducts WHERE product = '" + sale[7] + "';")) {
                if(rs.next()) {
                    idProduct = rs.getInt(1);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            factsales.add(new String[]{String.valueOf(idFecha), String.valueOf(idClient), String.valueOf(idProduct), sale[8], sale[9], sale[10]});
            contador++;
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
