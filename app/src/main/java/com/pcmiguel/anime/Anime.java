package com.pcmiguel.anime;

public class Anime {

    private int id;
    private String imagem;
    private String nome;
    private String ano;
    private String episodios;
    private String classificacao;

    Anime(int id, String imagem, String nome, String ano, String episodios, String classificacao) {
        this.id = id;
        this.imagem = imagem;
        this.nome = nome;
        this.ano = ano;
        this.episodios = episodios;
        this.classificacao = classificacao;
    }

    public int getId() { return id; }

    public String getImagem() {
        return imagem;
    }

    public String getNome() {
        return nome;
    }

    public String getAno() {
        return ano;
    }

    public String getEpisodios() {
        return episodios;
    }

    public String getClassificacao() {
        return classificacao;
    }

}
