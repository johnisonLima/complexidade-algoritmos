package br.edu.ifba.minasaquaticas.sensoriamento;

import java.util.List;

public interface Sensoriamento<TipoDado> {

    public List<TipoDado> obterDados(int quantidade);
}
