package org.example;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DimClients {
    public DimClients(){
        Conexion conn = new Conexion();
        ConexionDWH connDWH = new ConexionDWH();
        // SELECT (id_client, country, job_title, gender) FROM clients order by id_client;
        ArrayList<String[]> clients = new ArrayList<>();
        try {
            Statement stmt = conn.getConection().createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM client order by id_client;");
            while (rs.next()) {
                clients.add(new String[]{rs.getString("id_client"), rs.getString("country"),
                        rs.getString("job_title"), rs.getString("gender")});
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        // INSERT INTO client_dwh (country, job, gender)

        try {
            PreparedStatement stmt = connDWH.getConection().prepareStatement("INSERT INTO dimclients(country, job_title, gender) VALUES (?, ?, ?);");
            for (String[] client : clients) {
                System.out.println(client[3]);
                stmt.setString(1, client[1]);
                stmt.setString(2, client[2]);
                stmt.setString(3, client[3]);
                stmt.executeUpdate();
            }
        }catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
