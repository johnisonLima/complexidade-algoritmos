package br.edu.ifba.nobreaks.clientes.impl;

public class NoBreak implements Comparable<NoBreak> {

    private String identificacao = "";
    private String modelo = "";

    // O(1)
    public NoBreak(String identificacao, String modelo) {
        this.identificacao = identificacao;
        this.modelo = modelo;
    }

    // O(1)
    public String getIdentificacao() {
        return identificacao;
    }

    // O(1)
    public void setIdentificacao(String identificacao) {
        this.identificacao = identificacao;
    }

    // O(1)
    public String getModelo() {
        return modelo;
    }

    // O(1)
    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    // O(1)
    @Override
    public String toString() {
        return "nobreak: " + identificacao;
    }

    // O(1)
    @Override
    public int compareTo(NoBreak outroNoBreak) {
        return identificacao.compareTo(outroNoBreak.getIdentificacao());
    }

}
