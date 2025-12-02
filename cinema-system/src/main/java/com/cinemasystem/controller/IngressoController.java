package com.cinemasystem.controller;

import com.cinemasystem.service.VendaService; 
import io.javalin.http.Context;

public class IngressoController {

    private final VendaService vendaService = new VendaService(); 

    public void vender(Context ctx) {
        try {
            int sessaoId = Integer.parseInt(ctx.formParam("sessao_id"));
            int numAssento = Integer.parseInt(ctx.formParam("num_assento"));

            String resultado = vendaService.realizarVenda(sessaoId, numAssento);

            if (resultado.startsWith("Sucesso")) {
                ctx.status(200).result(resultado);
            } else {
                ctx.status(409).result(resultado); 
            }

        } catch (NumberFormatException e) {
            ctx.status(400).result("Erro: IDs e números de assento devem ser inteiros válidos.");
        } catch (Exception e) {
            ctx.status(500).result("Erro interno ao processar a venda: " + e.getMessage());
        }
    }

    public void getContagem(Context ctx) {
        try {
            int sessaoId = Integer.parseInt(ctx.pathParam("sessaoId"));
            
            int totalVendidos = vendaService.consultarLoteacao(sessaoId);
            
            ctx.status(200).json(new LoteacaoResponse(sessaoId, totalVendidos));
            
        } catch (NumberFormatException e) {
            ctx.status(400).result("Erro: O ID da sessão deve ser um número inteiro.");
        }
    }
    
    private static class LoteacaoResponse {
        public int sessaoId;
        public int totalIngressosVendidos;
        
        public LoteacaoResponse(int sessaoId, int totalIngressosVendidos) {
            this.sessaoId = sessaoId;
            this.totalIngressosVendidos = totalIngressosVendidos;
        }
    }
}