package br.edu.ifba.nobreaks.clientes.sensoriamento;

import java.util.List;

public interface Sensoriamento<Leitura> {

    public List<Leitura> gerar(int totalLeituras);

}
