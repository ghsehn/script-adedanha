/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.guisehn.db;

import com.guisehn.entidades.Palavra;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author guilhermesehn
 */
public class PalavrasProvider {
    
    public static void adicionarPalavra(Palavra palavra) throws SQLException {
        Connection conexao = BancoDados.obterConexao();
        String sql = "INSERT INTO palavras (palavra, tema) VALUES (?, ?)";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, palavra.getPalavra());
            stmt.setString(2, palavra.getTema());
            stmt.execute();
        }
    }
    
    public static List<Palavra> buscarPalavras(String tema, boolean ordemAleatoria)
    {
        Connection conexao = BancoDados.obterConexao();
        String sql = "SELECT * FROM palavras WHERE tema = ? ORDER BY " +
                (ordemAleatoria ? "RANDOM()" : "palavra ASC");
        List<Palavra> palavras = new ArrayList<>();
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, tema);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Palavra palavra = new Palavra();
                palavra.setId(rs.getInt("id"));
                palavra.setPalavra(rs.getString("palavra"));
                palavra.setTema(rs.getString("tema"));
                palavras.add(palavra);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        
        return palavras;
    }

    public static void excluirPalavra(int idPalavra) {
        Connection conexao = BancoDados.obterConexao();
        String sql = "DELETE FROM palavras WHERE id = ?";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, idPalavra);
            stmt.execute();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    public static String buscarPalavraAdedanha(String tema, char letraInicial,
            List<String> palavrasExcluidas)
    {
        Connection conexao = BancoDados.obterConexao();
        String sql = "SELECT * FROM palavras WHERE tema = ? AND palavra LIKE ? %s ORDER BY RANDOM() " +
                "LIMIT 1 COLLATE NOCASE";
        String palavra = "";
        String trechoExclusao = "";
        
        if (!palavrasExcluidas.isEmpty()) {
            StringBuilder sbExclusao = new StringBuilder();
            sbExclusao.append('?');

            for (int i = 1, len = palavrasExcluidas.size(); i < len; i++) {
                sbExclusao.append(",?");
            }

            trechoExclusao = " AND palavra NOT IN (" + sbExclusao.toString() + ")";
        }
        
        sql = String.format(sql, trechoExclusao);
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, tema);
            stmt.setString(2, letraInicial + "%");
            
            int i = 3;
            for (String palavraExcluida : palavrasExcluidas) {
                stmt.setString(i++, palavraExcluida);
            }
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                palavra = rs.getString("palavra");
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        
        return palavra;
    }
    
    public static boolean criarEstrutura() throws SQLException {
        Statement stmt = BancoDados.obterConexao().createStatement();
        
        if (BancoDados.tabelaExiste("palavras")) {
            /*ResultSet rs = stmt.executeQuery("select * from palavras order by tema asc, palavra asc");
            while (rs.next()) {
                System.out.println(rs.getInt("id") + "|" + rs.getString("tema") + "|" +
                        rs.getString("palavra"));
            }*/

            return false;
        }
        
        String ddl = "CREATE TABLE palavras (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "palavra VARCHAR(255)," +
                "tema VARCHAR(100)" +
                ")";

        stmt.executeUpdate(ddl);
        
        return true;
    }
    
}
