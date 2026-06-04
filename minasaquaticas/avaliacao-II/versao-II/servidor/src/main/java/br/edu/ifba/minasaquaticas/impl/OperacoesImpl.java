package br.edu.ifba.minasaquaticas.impl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.TreeMap;

import br.edu.ifba.minasaquaticas.operacoes.Operacoes;
import br.edu.ifba.minasaquaticas.ordenador.Ordenador;
import br.edu.ifba.minasaquaticas.ordenador.TipoOrdenacao;

public class OperacoesImpl implements Operacoes<Mina, Leitura> {
    /**
    * Quantidade máxima de leituras mantidas
    * em memória para cada mina.
    */
    private static final int LIMIAR_ROTACIONAMENTO_LEITURAS = 40;

    /**
    * Mantém apenas as leituras mais recentes
    * de cada mina.
    */
    private final Map<Mina, Queue<Leitura>> leiturasPorMinas = new TreeMap<>();
    /**
    * Quantidade de eventos detectados
    * localmente por cada mina.
    */
    private final Map<Mina, Integer> eventosPorMinas = new TreeMap<>();

    /**
    * O(log N)
    *
    * Registra uma nova leitura para a mina.
    * Caso o limite de armazenamento seja atingido,
    * a leitura mais antiga é descartada.
    */
    @Override
    public synchronized void gravar(Mina mina, Leitura leitura) {

        Queue<Leitura> leituras = new LinkedList<>();

        if (leiturasPorMinas.containsKey(mina)) {

            leituras = leiturasPorMinas.get(mina);

        } else {

            leiturasPorMinas.put(mina, leituras);
        }

        if (leituras.size() >= LIMIAR_ROTACIONAMENTO_LEITURAS) {

            leituras.poll();

            System.out.println("Limite de leituras atingido. " + "Leitura mais antiga removida.");
        }

        leituras.add(leitura);
    }

    /**
    * Registra a quantidade de eventos
    * detectados pela mina durante sua execução.
    */
    @Override
    public synchronized void gravar(Mina mina, int eventos) {

        if (
            eventosPorMinas.containsKey(mina)
        ) {

            eventos += eventosPorMinas.get(mina);
        }

        eventosPorMinas.put(mina, eventos);
    }

    /**
    * Retorna todas as leituras armazenadas.
    */
    public Map<Mina, List<Leitura>> consultar() {

        Map<Mina, List<Leitura>> resultado = new TreeMap<>();

        for (Mina mina : leiturasPorMinas.keySet()) {

            resultado.put(mina, new ArrayList<>(leiturasPorMinas.get(mina)));
        }

        return resultado;
    }

    /**
    * Retorna os eventos registrados
    * por cada mina.
    */
    public Map<Mina, Integer> consultarEventos() {
        return eventosPorMinas;
    }

    /**
    * Retorna a quantidade de minas monitoradas.
    */
    public int quantidadeMinas() {
        return leiturasPorMinas.size();
    }

    /**
    * Retorna a quantidade total de leituras recebidas.
    */
    public int quantidadeLeituras() {

        int total = 0;

        for (Queue<Leitura> leituras : leiturasPorMinas.values()) {

            total += leituras.size();
        }

        return total;
    }

    /**
    * Retorna a quantidade total de eventos
    * detectados por todas as minas.
    */
    public int quantidadeEventos() {

        int total = 0;

        for (Integer eventos : eventosPorMinas.values()) {
            total += eventos;
        }

        return total;
    }


    // O(N)
    // É um único loop usado para imprimir elementos em sequence, onde N é o número de minas monitoradas
    // Não tem grande impacto mesmo com um número elevado de minas, pois é uma operação linear simples
    @Override
    public void imprimirMonitorado(List<Mina> monitorado) {
        for (Mina mina : monitorado) {
            System.out.println(mina);
        }
    }

    // O(N*M)
    // São dois loops aninhados que compartilham uma variavel. O for externo percorre cada mina (N) e o interno percorre as leituras de cada mina (M)
    // Tem impacto se aumentar o numero de miunas, mas principalmente se aumentar o numero de leituras por mina, pois o número total de iterações cresce proporcionalmente
    @Override
    public void imprimir(Map<Mina, List<Leitura>> leituras) {
        // O(N*M)
        for (Mina mina : leituras.keySet()) {
            System.out.println("Monitoramento " + mina);
            // O(M) 
            for (Leitura leitura : leituras.get(mina)) {
                System.out.println(leitura);
            }   
            System.out.println();
        }
    }

    // O(N*M log M) 
    // O Map é percorrido uma vez (O(N)) e para cada mina, as leituras são ordenadas usando um algoritmo de ordenação eficiente (O(M log M))
    // Se a entrada de dados aumentar em vai ficar complicado pq a ordenação comsome muito recurso computacional e tempo
    @Override
    public Map<Mina, List<Leitura>> ordenar(Map<Mina, List<Leitura>> leiturasPorMinas, TipoOrdenacao tipoOrdenacao) {
        Map<Mina, List<Leitura>> leiturasOrdenadas = new TreeMap<>();

        // O(N)
        for (Mina mina : leiturasPorMinas.keySet()) {
            
            List<Leitura> leiturasMina = leiturasPorMinas.get(mina);
            Ordenador<Leitura> ordenador = new OrdenadorImpl(leiturasMina, tipoOrdenacao);
            // O(M log M)
            ordenador.ordenar();
            leiturasOrdenadas.put(mina, ordenador.getLeituras());
        }
        return leiturasOrdenadas;
    }

    // O(N^2 * M) 
    // São dois loops aninhados para comparar cada par de minas e um terceiro loop para comparar as leituras no mesmo instante 
    // Se a entrada de dados aumentar, o número de comparações cresce exponencialmente, tornando a operação muito lenta e impraticável para grandes conjuntos de dados
    @Override
    public int detectarObjetoEmTransito(Map<Mina, List<Leitura>> leiturasPorMinas, int limiteProximidadeCritica) {
        int contador = 0;

        List<Mina> minas = new ArrayList<>(leiturasPorMinas.keySet());
        int n = minas.size();

        // O(N^2)
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {

                Mina minaA = minas.get(i);
                Mina minaB = minas.get(j);

                List<Leitura> leiturasA = leiturasPorMinas.get(minaA);
                List<Leitura> leiturasB = leiturasPorMinas.get(minaB);

                int m = Math.min(leiturasA.size(), leiturasB.size());

                // O(M) 
                for (int k = 0; k < m; k++) {
                    boolean alertaA = leiturasA.get(k).getProximidade() < limiteProximidadeCritica;
                    boolean alertaB = leiturasB.get(k).getProximidade() < limiteProximidadeCritica;
                
                    if (alertaA && alertaB) {
                        contador++;
                        System.out.println("Objeto em trânsito detectado entre as minas ID=" + minaA.getId() + " e ID=" + minaB.getId() + " na leitura " + (k+1));
                    }
                }
            }
        }

        return contador;
    }

}
