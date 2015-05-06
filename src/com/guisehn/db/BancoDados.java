/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.guisehn.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author guilhermesehn
 */
public class BancoDados {
    
    private static Connection connection = null;
    
    public static Connection obterConexao() {
        if (connection == null) {
            try {
                Class.forName("org.sqlite.JDBC");
                connection = DriverManager.getConnection("jdbc:sqlite:dados.db");
                criarEstrutura();
            } catch (SQLException | ClassNotFoundException ex) {
                System.out.println(ex.getMessage());
            }
        }
        
        return connection;
    }
    
    private static void criarEstrutura() throws SQLException {
        TemasProvider.criarEstrutura();
        PalavrasProvider.criarEstrutura();
    }
    
    public static boolean tabelaExiste(String nome) throws SQLException {
        boolean existe = false;
        String sql = "SELECT name FROM sqlite_master WHERE type = 'table' AND name = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nome);
            ResultSet rs = stmt.executeQuery();
            if (rs.next())
                existe = true;
        }

        return existe;
    }
    
}
