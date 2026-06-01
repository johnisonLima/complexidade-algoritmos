package br.edu.ifba.nobreaks.impl;

import java.util.List;

import br.edu.ifba.nobreaks.ordenador.Ordenador;
import br.edu.ifba.nobreaks.ordenador.TipoOrdenacao;

public class OrdenadorImpl extends Ordenador<Leitura> {

    public OrdenadorImpl(List<Leitura> leituras, TipoOrdenacao tipoOrdenacao) {
        super(leituras, tipoOrdenacao);
    }

    // O(NLogN)
    @Override
    public void ordenar() {
        quicksort(0, leituras.size() - 1);
    }

    // O(NLogN)
    private void quicksort(int inicio, int fim) {
        if (inicio < fim) {
            int pivoIndex = particionar(inicio, fim);

            quicksort(inicio, pivoIndex - 1);
            quicksort(pivoIndex + 1, fim);
        }
    }

    // O(N)
    private int particionar(int inicio, int fim) {
        Leitura pivo = leituras.get(fim); 
        int i = inicio - 1;

        for (int j = inicio; j < fim; j++) {
            if (tipoOrdenacao == TipoOrdenacao.POR_TEMPERATURA) {
                if (leituras.get(j).getTemperatura() <= pivo.getTemperatura()) {
                    i++;
                    trocar(i, j);
                }
            } else {
                if (leituras.get(j).getCargaBateria() <= pivo.getCargaBateria()) {
                    i++;
                    trocar(i, j);
                }
            }
        }
        trocar(i + 1, fim);

        return i + 1;
    }

    // O(1)
    private void trocar(int i, int j) {
        Leitura temp = leituras.get(i);
        leituras.set(i, leituras.get(j));
        leituras.set(j, temp);
    }
}