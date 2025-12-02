package com.cinemasystem.repository;

import com.cinemasystem.config.DatabaseConfig;
import com.cinemasystem.model.Sessao;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SessaoRepository {

    private static final String INSERT = "INSERT INTO Sessoes (filme_id, sala_id, data_hora, preco) VALUES (?, ?, ?, ?)";
    private static final String SELECT_ALL = "SELECT sessao_id, filme_id, sala_id, data_hora, preco FROM Sessoes ORDER BY data_hora";
    private static final String SELECT_BY_ID = "SELECT sessao_id, filme_id, sala_id, data_hora, preco FROM Sessoes WHERE sessao_id = ?";

    private Sessao mapResultSetToSessao(ResultSet rs) throws SQLException {
        int id = rs.getInt("sessao_id");
        int filmeId = rs.getInt("filme_id");
        int salaId = rs.getInt("sala_id");
        
        Timestamp timestamp = rs.getTimestamp("data_hora");
        LocalDateTime dataHora = timestamp.toLocalDateTime();
        
        BigDecimal preco = rs.getBigDecimal("preco");
        
        return new Sessao(id, filmeId, salaId, dataHora, preco);
    }

    public void cadastrar(Sessao sessao) throws SQLException {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, sessao.getFilmeId());
            stmt.setInt(2, sessao.getSalaId());
            stmt.setTimestamp(3, Timestamp.valueOf(sessao.getDataHora())); 
            stmt.setBigDecimal(4, sessao.getPreco());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    sessao.setId(rs.getInt(1));
                }
            }
        }
    }

    public List<Sessao> buscarTodos() throws SQLException {
        List<Sessao> sessoes = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL)) {

            while (rs.next()) {
                sessoes.add(mapResultSetToSessao(rs));
            }
        }
        return sessoes;
    }

    public Sessao buscarPorId(int id) throws SQLException {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ID)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToSessao(rs);
                }
            }
        }
        return null; 
    }
    
}