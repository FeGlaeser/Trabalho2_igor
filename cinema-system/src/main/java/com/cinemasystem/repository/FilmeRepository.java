package com.cinemasystem.repository;

import com.cinemasystem.config.DatabaseConfig;
import com.cinemasystem.model.Filme;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FilmeRepository {

    private static final String INSERT = "INSERT INTO Filmes (titulo, duracao_minutos, genero, classificacao_etaria) VALUES (?, ?, ?, ?)";
    private static final String SELECT_ALL = "SELECT filme_id, titulo, duracao_minutos, genero, classificacao_etaria FROM Filmes";

    public void cadastrar(Filme filme) throws SQLException {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, filme.getTitulo());
            stmt.setInt(2, filme.getDuracaoMinutos());
            stmt.setString(3, filme.getGenero());
            stmt.setString(4, filme.getClassificacaoEtaria()); // NOVO PARÂMETRO

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    filme.setId(rs.getInt(1));
                }
            }
        }
    }
    
    public List<Filme> buscarTodos() throws SQLException {
        List<Filme> filmes = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL)) {

            while (rs.next()) {
                Filme filme = new Filme(
                    rs.getInt("filme_id"),
                    rs.getString("titulo"),
                    rs.getInt("duracao_minutos"),
                    rs.getString("genero"),
                    rs.getString("classificacao_etaria") // NOVO CAMPO NO MAPPING
                );
                filmes.add(filme);
            }
        }
        return filmes;
    }
}