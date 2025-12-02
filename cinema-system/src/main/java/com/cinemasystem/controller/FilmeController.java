package com.cinemasystem.controller;

import com.cinemasystem.model.Filme;
import com.cinemasystem.repository.FilmeRepository;
import io.javalin.http.Context;
import java.sql.SQLException;
import java.util.List;

public class FilmeController {

    private final FilmeRepository repository = new FilmeRepository();

    public void cadastrar(Context ctx) {
        try {
            Filme novoFilme = ctx.bodyAsClass(Filme.class);
            repository.cadastrar(novoFilme);
            ctx.status(201).json(novoFilme);
        } catch (SQLException e) {
            ctx.status(500).result("Erro ao cadastrar filme: " + e.getMessage());
        } catch (Exception e) {
            ctx.status(400).result("Dados inválidos: " + e.getMessage());
        }
    }

    public void buscarTodos(Context ctx) {
        try {
            List<Filme> filmes = repository.buscarTodos();
            ctx.json(filmes);
        } catch (SQLException e) {
            ctx.status(500).result("Erro ao buscar filmes: " + e.getMessage());
        }
    }
}