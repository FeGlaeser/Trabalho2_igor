package com.cinemasystem.repository;

import com.cinemasystem.config.DatabaseConfig;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;
import java.sql.SQLException;

public class IngressoRepository {

    /**
     * Chama a procedure armazenada 'vender_ingresso' no PostgreSQL.
     * @param sessaoId O ID da sessão.
     * @param numAssento O número do assento a ser vendido.
     * @return Uma mensagem de status (sucesso ou erro).
     */
    public String venderIngresso(int sessaoId, int numAssento) {
        String sql = "{CALL vender_ingresso(?, ?, ?, ?)}";
        String mensagem = "Erro desconhecido na venda.";
        boolean sucesso = false;

        try (Connection conn = DatabaseConfig.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, sessaoId);
            stmt.setInt(2, numAssento);

            stmt.registerOutParameter(3, Types.BOOLEAN); 
            stmt.registerOutParameter(4, Types.VARCHAR); 

            stmt.execute();

            sucesso = stmt.getBoolean(3);
            mensagem = stmt.getString(4);

        } catch (SQLException e) {
            e.printStackTrace();
            mensagem = "Falha de comunicação com o BD: " + e.getMessage();
        }

        if (sucesso) {
            return "Sucesso: " + mensagem;
        } else {
            return "Falha: " + mensagem;
        }
    }
    
    public int contarIngressosVendidos(int sessaoId) {
        String sql = "{? = call contar_ingressos_vendidos(?)}";
        int total = 0;

        try (Connection conn = DatabaseConfig.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.registerOutParameter(1, Types.INTEGER); 
            
            stmt.setInt(2, sessaoId);

            stmt.execute();

            total = stmt.getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }
}