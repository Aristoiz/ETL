package org.example;

import java.sql.PreparedStatement;
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
            try(PreparedStatement stmt2 = connDWH.getConection().prepareStatement("INSERT INTO dimfechas(anio,mes) VALUES (?,?);")){
                for (String[] fecha : fechas) {
                    stmt2.setInt(1, Integer.parseInt(fecha[0]));
                    stmt2.setInt(2, Integer.parseInt(fecha[1]));
                    stmt2.addBatch();
                }
                stmt2.executeBatch();
            }catch (Exception e){
                System.out.println("Error: " + e.getMessage());
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }


    }

}
