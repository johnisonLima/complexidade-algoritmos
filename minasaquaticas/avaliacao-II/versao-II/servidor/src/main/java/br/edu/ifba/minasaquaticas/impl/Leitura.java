package br.edu.ifba.minasaquaticas.impl;

public class Leitura {
    Integer profundidade;
    Integer proximidade; 

    public Leitura(Integer profundidade, Integer proximidade) {
        this.profundidade = profundidade;
        this.proximidade = proximidade;
    }

    public Integer getProfundidade() {
        return profundidade;
    }

    public void setProfundidade(Integer profundidade) {
        this.profundidade = profundidade;
    }

    public Integer getProximidade() {
        return proximidade;
    }

    public void setProximidade(Integer proximidade) {
        this.proximidade = proximidade;
    }

    @Override
    public String toString() {
        return "Valores=>[profundidade: " + profundidade + ", proximidade: " + proximidade + "]";
    }

}
