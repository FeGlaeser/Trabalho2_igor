package com.cinemasystem.model;

import java.time.LocalDateTime;
import java.math.BigDecimal;

public class Sessao {
    private int id;
    private int filmeId;
    private int salaId;
    private LocalDateTime dataHora;
    private BigDecimal preco; 

    public Sessao(int id, int filmeId, int salaId, LocalDateTime dataHora, BigDecimal preco) {
        this.id = id;
        this.filmeId = filmeId;
        this.salaId = salaId;
        this.dataHora = dataHora;
        this.preco = preco;
    }

    public Sessao(int filmeId, int salaId, LocalDateTime dataHora, BigDecimal preco) {
        this.filmeId = filmeId;
        this.salaId = salaId;
        this.dataHora = dataHora;
        this.preco = preco;
    }

    public Sessao() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFilmeId() {
        return filmeId;
    }

    public void setFilmeId(int filmeId) {
        this.filmeId = filmeId;
    }

    public int getSalaId() {
        return salaId;
    }

    public void setSalaId(int salaId) {
        this.salaId = salaId;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }
}