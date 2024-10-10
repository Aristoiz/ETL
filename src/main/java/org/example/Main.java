package org.example;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;



public class Main {


    public static void main(String[] args) {
        //Se carga todo
        DimClients dimClients = new DimClients();
        dimProducts dimProducts = new dimProducts();
        dimFechas dimFechas = new dimFechas();
        factSales factSales = new factSales();
    }
}