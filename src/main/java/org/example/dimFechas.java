package org.example;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class dimFechas {
    public dimFechas(){
        Conexion conn = new Conexion();
        ConexionDWH connDWH = new ConexionDWH();
        ArrayList<String[]> fechas = new ArrayList<>();
        try {
            Statement stmt = conn.getConection().createStatement();
            ResultSet rs = stmt.executeQuery("SELECT distinct date_part('year',date_sale), date_part('month',date_sale) FROM sale order by 1,2;");
            while (rs.next()) {
                fechas.add(new String[]{rs.getString(1), rs.getString(2)});
            }
            for (String[] fecha : fechas) {
                System.out.println(fecha[0] + " " + fecha[1]);
                Statement stmt2 = connDWH.getConection().createStatement();
                stmt2.executeUpdate("INSERT INTO dimFechas(anio, mes) VALUES (" + fecha[0] + ", " + fecha[1] + ");");
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }


    }

}
