/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.guisehn.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author guilhermesehn
 */
public class TemasProvider {
 
    public static void adicionarTema(String nomeTema) throws SQLException {
        Connection conexao = BancoDados.obterConexao();
        String sql = "INSERT INTO temas (nome) VALUES (?)";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, nomeTema);
            stmt.execute();
        }
    }
    
    public static List<String> buscarTemas() {
        Connection conexao = BancoDados.obterConexao();
        String sql = "SELECT * FROM temas ORDER BY nome ASC";
        List<String> temas = new ArrayList<>();
        
        try (Statement stmt = conexao.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                temas.add(rs.getString("nome"));
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        
        return temas;
    }
    
    public static void excluirTema(String nomeTema) {
        Connection conexao = BancoDados.obterConexao();
        String sql = "DELETE FROM temas WHERE nome = ?";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, nomeTema);
            stmt.execute();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    public static boolean criarEstrutura() throws SQLException {
        if (BancoDados.tabelaExiste("temas"))
            return false;
        
        String ddl = "CREATE TABLE temas (nome VARCHAR(100) PRIMARY KEY)";
        Statement stmt = BancoDados.obterConexao().createStatement();
        stmt.executeUpdate(ddl);
        
        return true;
    }
    
}
