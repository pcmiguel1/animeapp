package com.pcmiguel.anime;

public class AnimeList {

    private int id;
    private String imagem;
    private String nome;
    private int totalEpisodios;
    private int episodiosVistos;
    private int rated;
    private int library;

    public AnimeList(int id, String imagem, String nome, int totalEpisodios, int episodiosVistos, int rated, int library) {
        this.id = id;
        this.imagem = imagem;
        this.nome = nome;
        this.totalEpisodios = totalEpisodios;
        this.episodiosVistos = episodiosVistos;
        this.rated = rated;
        this.library = library;
    }

    public int getId() {
        return id;
    }

    public String getImagem() {
        return imagem;
    }

    public String getNome() {
        return nome;
    }

    public int getTotalEpisodios() {
        return totalEpisodios;
    }

    public int getEpisodiosVistos() {
        return episodiosVistos;
    }

    public int getRated() {
        return rated;
    }

    public int getLibrary() {
        return library;
    }

    public void setRated(int rated) {
        this.rated = rated;
    }

    public void setEpisodiosVistos(int episodiosVistos) {
        this.episodiosVistos = episodiosVistos;
    }
}
