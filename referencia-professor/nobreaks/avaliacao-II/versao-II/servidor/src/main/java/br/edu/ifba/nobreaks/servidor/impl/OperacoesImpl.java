package br.edu.ifba.nobreaks.servidor.impl;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.TreeMap;

import br.edu.ifba.nobreaks.servidor.operacoes.Operacoes;

public class OperacoesImpl implements Operacoes<NoBreak, Leitura> {

    private static final int LIMIAR_ROTACIONAMENTO_LEITURAS = 40;

    private Map<NoBreak, Queue<Leitura>> bancoDeDados = new TreeMap<>();
    private Map<NoBreak, Integer> altasOscilacoes = new TreeMap<>();

    @Override
    public void gravar(NoBreak noBreak, Leitura leitura) {
        Queue<Leitura> leituras = new LinkedList<>();
        if (bancoDeDados.containsKey(noBreak)) {
            leituras = bancoDeDados.get(noBreak);
        } else {
            bancoDeDados.put(noBreak, leituras);
        }

        try {
            Thread.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (leituras.size() > LIMIAR_ROTACIONAMENTO_LEITURAS) {
            leituras.poll();

            System.out.println("limite de rotacionamento atingido, última leitura descartada");
        }
        leituras.add(leitura);

        System.out.println("gravada nova leitura para o noBreak: " + noBreak);
    }

    @Override
    public void gravar(NoBreak noBreak, int oscilacoes) {
        System.out.println(oscilacoes > 0? "oscilação informada pelo noBreak: " + noBreak: "nenhuma oscilação informada pelo noBreak: " + noBreak);

        if (altasOscilacoes.keySet().contains(noBreak)) {
            oscilacoes += altasOscilacoes.get(noBreak);
        }
        altasOscilacoes.put(noBreak, oscilacoes);
    }

    // O(N)
    @Override
    public int detectarAltasOscilacoes() {
        int contador = 0;

        for (Integer oscilacoes : altasOscilacoes.values())
        {
            contador += oscilacoes;
        }

        return contador;
    }
    
}
