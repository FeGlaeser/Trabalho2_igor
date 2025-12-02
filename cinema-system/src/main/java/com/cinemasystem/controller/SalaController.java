package com.cinemasystem.controller;

import com.cinemasystem.model.Sala;
import com.cinemasystem.repository.SalaRepository;
import io.javalin.http.Context;
import java.sql.SQLException;
import java.util.List;

public class SalaController {

    private final SalaRepository repository = new SalaRepository();

    public void cadastrar(Context ctx) {
        try {
            Sala novaSala = ctx.bodyAsClass(Sala.class);
            
            if (novaSala.getCapacidade() <= 0) {
                ctx.status(400).result("A capacidade da sala deve ser um número positivo.");
                return;
            }
            
            repository.cadastrar(novaSala);
            ctx.status(201).json(novaSala);
        } catch (SQLException e) {
            ctx.status(500).result("Erro ao cadastrar sala: " + e.getMessage());
        } catch (Exception e) {
            ctx.status(400).result("Dados inválidos para a sala: " + e.getMessage());
        }
    }

    public void buscarTodos(Context ctx) {
        try {
            List<Sala> salas = repository.buscarTodos();
            ctx.json(salas);
        } catch (SQLException e) {
            ctx.status(500).result("Erro ao buscar salas: " + e.getMessage());
        }
    }
    
    public void buscarPorId(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Sala sala = repository.buscarPorId(id);
            
            if (sala != null) {
                ctx.json(sala);
            } else {
                ctx.status(404).result("Sala não encontrada.");
            }
        } catch (NumberFormatException e) {
            ctx.status(400).result("ID da sala inválido.");
        } catch (SQLException e) {
            ctx.status(500).result("Erro ao buscar sala: " + e.getMessage());
        }
    }
}