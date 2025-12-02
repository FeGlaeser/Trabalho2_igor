package com.cinemasystem;

import com.cinemasystem.controller.FilmeController;
import com.cinemasystem.controller.IngressoController;
import com.cinemasystem.controller.SalaController;
import com.cinemasystem.controller.SessaoController; 
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;

public class Main {

    public static void main(String[] args) {
        Javalin app = Javalin.create(config -> {
            config.staticFiles.add("/public", Location.CLASSPATH); 
            config.bundledJavalinRenderer.set1(io.javalin.rendering.JavalinRenderer::register, 
                new io.javalin.rendering.template.JavalinMustache()); 
        }).start(7000); 

        IngressoController ingressoController = new IngressoController();
        FilmeController filmeController = new FilmeController();
        SessaoController sessaoController = new SessaoController(); 
        SalaController salaController = new SalaController(); 

        app.post("/api/filmes", filmeController::cadastrar);
        app.get("/api/filmes", filmeController::buscarTodos);
        
        app.post("/api/salas", salaController::cadastrar);
        app.get("/api/salas", salaController::buscarTodos);
        app.get("/api/salas/{id}", salaController::buscarPorId);
        
        app.post("/api/sessoes", sessaoController::cadastrar);
        app.get("/api/sessoes", sessaoController::buscarTodos);
        app.get("/api/sessoes/{id}", sessaoController::buscarPorId);

        app.post("/api/ingressos/vender", ingressoController::vender);
        app.get("/api/ingressos/contagem/{sessaoId}", ingressoController::getContagem);

        app.get("/", ctx -> {
            ctx.render("templates/venda.html"); 
        });

        System.out.println("Servidor Javalin rodando na porta 7000. Acesse http://localhost:7000");
    }
}