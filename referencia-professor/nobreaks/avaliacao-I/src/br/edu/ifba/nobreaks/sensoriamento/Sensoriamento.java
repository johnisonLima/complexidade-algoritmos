package br.edu.ifba.nobreaks.sensoriamento;

import java.util.List;

public interface Sensoriamento<TipoDado> {

    public List<TipoDado> gerar(int totalLeituras);

}
