package br.edu.ifba.nobreaks.impl;

public class Nobreak implements Comparable<Nobreak> {

    private String identificacao = "";
    private String modelo = "";

    public Nobreak(String identificacao, String modelo) {
        this.identificacao = identificacao;
        this.modelo = modelo;
    }
    
    public String getIdentificacao() {
        return identificacao;
    }

    public String getModelo() {
        return modelo;
    }

    @Override
    public String toString() {
        return "id: " + identificacao + ", modelo: " + modelo;
    }

    @Override
    public int compareTo(Nobreak o) {
        return this.identificacao.compareTo(o.getIdentificacao());
    }

}
