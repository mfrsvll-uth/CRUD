package com.mycompany.proyectojavadb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {

    private static final String URL =
            "jdbc:sqlserver://localhost:1433;databaseName=AVANZADA1;encrypt=true;trustServerCertificate=true";

    private static final String USER = "java_user";

    private static final String PASSWORD = "221722";

    public static Connection conectar() {
        try {
            Connection conexion = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Conexion exitosa a SQL Server.");
            return conexion;

        } catch (SQLException e) {
            System.out.println("Error al conectar: " + e.getMessage());
            return null;
        }
    }
}