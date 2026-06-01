package br.edu.ifba.nobreaks.clientes.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import br.edu.ifba.nobreaks.clientes.sensoriamento.Sensoriamento;

public class SensoriamentoImpl implements Sensoriamento<Leitura> {

    private static final int TEMPERATURA_NORMAL = 60;
    private static final int CARGA_BATERIA_NORMAL = 90;

    private static final int OSCILACAO_MAXIMA_TEMPERATURA = 10;
    private static final int OSCILACAO_MAXIMA_CARGA_BATERIA = 10;

    // Complexidade Linear, O(N)
    @Override
    public List<Leitura> gerar(int totalLeituras) {
        List<Leitura> leituras = new ArrayList<>();

        Random randomizador = new Random();
        for (int i = 0; i < totalLeituras; i++) {
            int oscilacao = TEMPERATURA_NORMAL * randomizador.nextInt(OSCILACAO_MAXIMA_TEMPERATURA)/100;
            int temperatura = randomizador.nextBoolean()? TEMPERATURA_NORMAL + oscilacao: TEMPERATURA_NORMAL - oscilacao;

            oscilacao = CARGA_BATERIA_NORMAL * randomizador.nextInt(OSCILACAO_MAXIMA_CARGA_BATERIA)/100;
            int cargaBateria = randomizador.nextBoolean()? TEMPERATURA_NORMAL + oscilacao: TEMPERATURA_NORMAL - oscilacao;            

            leituras.add(new Leitura(temperatura, cargaBateria));
        }

        return leituras;
    }
    
}