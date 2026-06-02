package br.edu.ifba.minasaquaticas.impl;

import java.util.List;

import br.edu.ifba.minasaquaticas.ordenador.Ordenador;
import br.edu.ifba.minasaquaticas.ordenador.TipoOrdenacao;

public class OrdenadorImpl extends Ordenador<Leitura> {

    public OrdenadorImpl(List<Leitura> leituras, TipoOrdenacao tipoOrdenacao) {
        super(leituras, tipoOrdenacao);
    }

    // O(N log N)
    // Parace dois loops sim, mas na verdade é um processo de construção de heap (O(N)) seguido por N extrações do maior elemento (O(N log N))
    // Se a entrada de dados aumentar, o tempo de ordenação crescerá significativamente, especialmente para grandes listas de leituras, devido ao O(N log N) do algoritmo de heap sort
    @Override
    public void ordenar() {
        int n = leituras.size();

        // O(N)
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(n, i);
        }

        // O(N log N)
        for (int i = n - 1; i > 0; i--) {
            trocar(0, i);       
            heapify(i, 0);       
        }
    }

    // O(log N)
    // Pois a função heapify é chamada recursivamente para ajustar a posição de um elemento em uma estrutura de heap
    private void heapify(int tamanhoHeap, int i) {
        int maior = i;
        int filhoEsquerdo = 2 * i + 1;
        int filhoDireito  = 2 * i + 2;

        if (filhoEsquerdo < tamanhoHeap && comparar(filhoEsquerdo, maior) > 0) {
            maior = filhoEsquerdo;
        }

        if (filhoDireito < tamanhoHeap && comparar(filhoDireito, maior) > 0) {
            maior = filhoDireito;
        }

        if (maior != i) {
            trocar(i, maior);
            heapify(tamanhoHeap, maior); 
        }
    }

    // O(1)
    private int comparar(int indexA, int indexB) {
        Leitura a = leituras.get(indexA);
        Leitura b = leituras.get(indexB);

        if (tipoOrdenacao == TipoOrdenacao.POR_PROFUNDIDADE) {
            return Integer.compare(a.getProfundidade(), b.getProfundidade());
        } else {
            return Integer.compare(a.getProximidade(), b.getProximidade());
        }
    }

    // O(1)
    private void trocar(int i, int j) {
        Leitura temp = leituras.get(i);
        leituras.set(i, leituras.get(j));
        leituras.set(j, temp);
    }
}