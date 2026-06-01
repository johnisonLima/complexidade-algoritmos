package br.edu.ifba.minasaquaticas.ordenador;

import java.util.List;

public abstract class Ordenador<TipoDado> {

    protected List<TipoDado> leituras = null;
    protected TipoOrdenacao tipoOrdenacao = TipoOrdenacao.POR_PROFUNDIDADE;   

    public Ordenador(List<TipoDado> leituras, TipoOrdenacao tipoOrdenacao) {
        this.leituras = leituras;
        this.tipoOrdenacao = tipoOrdenacao;
    }

    public List<TipoDado> getLeituras() {
        return leituras;
    }

    public void setLeituras(List<TipoDado> leituras) {
        this.leituras = leituras;
    }

    public TipoOrdenacao getTipoOrdenacao() {
        return tipoOrdenacao;
    }

    public void setTipoOrdenacao(TipoOrdenacao tipoOrdenacao) {
        this.tipoOrdenacao = tipoOrdenacao;
    }

    public abstract void ordenar();

}
