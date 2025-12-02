package com.cinemasystem.service;

import com.cinemasystem.repository.IngressoRepository;

public class VendaService {

    private final IngressoRepository ingressoRepository = new IngressoRepository();

    /**
     * Tenta vender um ingresso, delegando a chamada da PROCEDURE ao Repository.
     * @param sessaoId ID da sessão.
     * @param numAssento Número do assento a ser vendido.
     * @return String com o resultado da operação (Sucesso ou Falha) retornado pela PROCEDURE.
     */
    public String realizarVenda(int sessaoId, int numAssento) {
        if (sessaoId <= 0 || numAssento <= 0) {
            return "Falha: Dados de sessão ou assento inválidos (devem ser positivos).";
        }
        
        return ingressoRepository.venderIngresso(sessaoId, numAssento);
    }
    

    public int consultarLoteacao(int sessaoId) {
        if (sessaoId <= 0) {
            return 0;
        }
        return ingressoRepository.contarIngressosVendidos(sessaoId);
    }
}