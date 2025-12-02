package com.cinemasystem.model;

import java.time.LocalDateTime;

public class Ingresso {
    private int id;
    private int sessaoId;
    private int numAssento;
    private LocalDateTime vendidoEm;

    public Ingresso(int id, int sessaoId, int numAssento, LocalDateTime vendidoEm) {
        this.id = id;
        this.sessaoId = sessaoId;
        this.numAssento = numAssento;
        this.vendidoEm = vendidoEm;
    }
    
    public Ingresso(int sessaoId, int numAssento) {
        this.sessaoId = sessaoId;
        this.numAssento = numAssento;
    }

    public Ingresso() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSessaoId() {
        return sessaoId;
    }

    public void setSessaoId(int sessaoId) {
        this.sessaoId = sessaoId;
    }

    public int getNumAssento() {
        return numAssento;
    }

    public void setNumAssento(int numAssento) {
        this.numAssento = numAssento;
    }

    public LocalDateTime getVendidoEm() {
        return vendidoEm;
    }

    public void setVendidoEm(LocalDateTime vendidoEm) {
        this.vendidoEm = vendidoEm;
    }
}