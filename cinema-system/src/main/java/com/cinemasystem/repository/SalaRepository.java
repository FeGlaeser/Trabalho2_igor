package com.cinemasystem.repository;

import com.cinemasystem.config.DatabaseConfig;
import com.cinemasystem.model.Sala;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SalaRepository {

    private static final String INSERT = "INSERT INTO Salas (nome, capacidade) VALUES (?, ?)";
    private static final String SELECT_ALL = "SELECT sala_id, nome, capacidade FROM Salas";
    private static final String SELECT_BY_ID = "SELECT sala_id, nome, capacidade FROM Salas WHERE sala_id = ?";

    public void cadastrar(Sala sala) throws SQLException {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, sala.getNome());
            stmt.setInt(2, sala.getCapacidade());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    sala.setId(rs.getInt(1));
                }
            }
        }
    }

    public List<Sala> buscarTodos() throws SQLException {
        List<Sala> salas = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL)) {

            while (rs.next()) {
                Sala sala = new Sala(
                    rs.getInt("sala_id"),
                    rs.getString("nome"),
                    rs.getInt("capacidade")
                );
                salas.add(sala);
            }
        }
        return salas;
    }

    public Sala buscarPorId(int id) throws SQLException {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ID)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Sala(
                        rs.getInt("sala_id"),
                        rs.getString("nome"),
                        rs.getInt("capacidade")
                    );
                }
            }
        }
        return null;
    }
}