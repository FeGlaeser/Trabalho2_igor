package com.cinemasystem.controller;

import com.cinemasystem.model.Sessao;
import com.cinemasystem.repository.SessaoRepository;
import io.javalin.http.Context;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import org.postgresql.util.PSQLState; 

public class SessaoController {

    private final SessaoRepository repository = new SessaoRepository();

    public void cadastrar(Context ctx) {
        try {
            Sessao novaSessao = ctx.bodyAsClass(Sessao.class);
            
            if (novaSessao.getDataHora() == null || novaSessao.getPreco() == null) {
                ctx.status(400).result("Dados incompletos para a sessão: Data/Hora e Preço são obrigatórios.");
                return;
            }

            repository.cadastrar(novaSessao);
            ctx.status(201).json(novaSessao);
        } catch (DateTimeParseException e) {
            ctx.status(400).result("Erro de formato de data/hora. Use o formato ISO-8601 (ex: 2025-12-05T19:30:00).");
        } catch (SQLException e) {
            String sqlState = e.getSQLState();
            
            if (PSQLState.UNIQUE_VIOLATION.getState().equals(sqlState)) {
                ctx.status(409).result("Falha ao cadastrar: A sala já está ocupada nesta data e horário.");
            } else if (PSQLState.FOREIGN_KEY_VIOLATION.getState().equals(sqlState)) {
                ctx.status(400).result("Falha ao cadastrar: ID do Filme ou da Sala não encontrado no sistema.");
            } else {
                ctx.status(500).result("Erro interno do banco de dados: " + e.getMessage());
            }
        } catch (Exception e) {
            ctx.status(400).result("Dados inválidos: " + e.getMessage());
        }
    }

    public void buscarTodos(Context ctx) {
        try {
            ctx.json(repository.buscarTodos());
        } catch (SQLException e) {
            ctx.status(500).result("Erro ao buscar sessões: " + e.getMessage());
        }
    }
    
    public void buscarPorId(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Sessao sessao = repository.buscarPorId(id);
            
            if (sessao != null) {
                ctx.json(sessao);
            } else {
                ctx.status(404).result("Sessão não encontrada.");
            }
        } catch (NumberFormatException e) {
            ctx.status(400).result("ID da sessão inválido.");
        } catch (SQLException e) {
            ctx.status(500).result("Erro ao buscar sessão: " + e.getMessage());
        }
    }
}