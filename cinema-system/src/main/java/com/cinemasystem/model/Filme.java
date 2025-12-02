package com.cinemasystem.model;

public class Filme {
    private int id;
    private String titulo;
    private int duracaoMinutos;
    private String genero;
    private String classificacaoEtaria; // NOVO CAMPO

    // Construtor Completo
    public Filme(int id, String titulo, int duracaoMinutos, String genero, String classificacaoEtaria) {
        this.id = id;
        this.titulo = titulo;
        this.duracaoMinutos = duracaoMinutos;
        this.genero = genero;
        this.classificacaoEtaria = classificacaoEtaria;
    }

    public Filme() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public int getDuracaoMinutos() { return duracaoMinutos; }
    public void setDuracaoMinutos(int duracaoMinutos) { this.duracaoMinutos = duracaoMinutos; }
    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }
    
    public String getClassificacaoEtaria() { return classificacaoEtaria; }
    public void setClassificacaoEtaria(String classificacaoEtaria) { this.classificacaoEtaria = classificacaoEtaria; }
}