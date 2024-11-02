/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.yovany.eventos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class TestDBConnection {
     public static void main(String[] args) {
        String url = "jdbc:mariadb://26.53.166.218:3306/eventosempresa";
        String user = "Rodeumg";
        String password = "1234";

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            if (connection != null) {
                System.out.println("Conexión exitosa a la base de datos.");
            }
        } catch (SQLException e) {
            System.out.println("Error de conexión: " + e.getMessage());
        }
    }
}
