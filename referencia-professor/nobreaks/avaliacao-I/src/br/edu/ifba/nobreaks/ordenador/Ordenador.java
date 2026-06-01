package br.edu.ifba.nobreaks.ordenador;

import java.util.List;

public abstract class Ordenador<TipoDado> {
    
    protected List<TipoDado> leituras = null;
    protected TipoOrdenacao tipoOrdenacao = TipoOrdenacao.POR_TEMPERATURA;

    public Ordenador(List<TipoDado> leituras, TipoOrdenacao tipoOrdenacao) {
        this.leituras = leituras;
        this.tipoOrdenacao = tipoOrdenacao;
    }

    public List<TipoDado> getLeituras() {
        return leituras;
    }

    public abstract void ordenar();

}
