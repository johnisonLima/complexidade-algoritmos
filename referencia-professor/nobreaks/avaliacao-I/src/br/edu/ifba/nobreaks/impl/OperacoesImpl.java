package br.edu.ifba.nobreaks.impl;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import br.edu.ifba.nobreaks.operacoes.Operacoes;
import br.edu.ifba.nobreaks.ordenador.Ordenador;
import br.edu.ifba.nobreaks.ordenador.TipoOrdenacao;

public class OperacoesImpl implements Operacoes<Nobreak, Leitura> {

    // O(N)
    @Override
    public void imprimir(List<Nobreak> monitorados) {
        for (Nobreak nobreak : monitorados) {
            System.out.println("nobreak sendo monitorado: " + nobreak);
        }
    }

    // O(N*M)
    @Override
    public void imprimir(Map<Nobreak, List<Leitura>> leituras) {
        for (Nobreak nobreak : leituras.keySet()) {
            System.out.println("leituras do nobreak " + nobreak + ":");
            for (Leitura leitura : leituras.get(nobreak)) {
                System.out.println(leitura);
            }
        }
    }

    // O(N*MlogM)
    @Override
    public Map<Nobreak, List<Leitura>> ordenar(Map<Nobreak, List<Leitura>> leiturasPorNobreaks,
            TipoOrdenacao tipoOrdenacao) {
        Map<Nobreak, List<Leitura>> leiturasOrdenadas = new TreeMap<>();

        for (Nobreak nobreak : leiturasPorNobreaks.keySet()) {
            System.out.println("ordenando leituras do nobreak: " + nobreak);

            List<Leitura> leituras = leiturasPorNobreaks.get(nobreak);
            Ordenador<Leitura> ordenador = new OrdenadorImpl(leituras, tipoOrdenacao);
            ordenador.ordenar();

            leiturasOrdenadas.put(nobreak, leituras);
        }

        return leiturasOrdenadas;
    }

    // O(N*M^2)
    @Override
    public int contarAltasOscilacoes(Map<Nobreak, List<Leitura>> leiturasPorNobreaks, int limiteOscilacaoTemperatura, int limiteOscilacaoCargaBateria) {
        int contador = 0;

        for (Nobreak nobreak : leiturasPorNobreaks.keySet()) {
            List<Leitura> leituras = leiturasPorNobreaks.get(nobreak);
            int n = leituras.size();

            for (int i = 0; i < n; i++) {
                for (int j = i + 1; j < n; j++) {
                    
                    int oscilacaoTemperatura = Math.abs(leituras.get(i).getTemperatura() -
                            leituras.get(j).getTemperatura());
                    int oscilacaoCargaBateria = Math.abs(leituras.get(i).getCargaBateria() - leituras.get(j).getCargaBateria());

                    if (oscilacaoTemperatura > limiteOscilacaoTemperatura || oscilacaoCargaBateria > limiteOscilacaoCargaBateria) {
                        contador++;
                    }
                }
            }
        }

        return contador;
    }

}
