package br.edu.ifba.nobreaks.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import br.edu.ifba.nobreaks.sensoriamento.Sensoriamento;

public class SensoriamentoImpl implements Sensoriamento<Leitura> {

    private static final int TEMPERATURA_NORMAL = 35; // Temperatura ambiente típica para nobreaks
    private static final int OSCILACAO_MAXIMA_TEMPERATURA = 10;

    private static final int CARGA_BATERIA_NORMAL = 100; // 100% carga
    private static final int OSCILACAO_MAXIMA_CARGA_BATERIA = 20; // Oscilação em %


    // O(N)
    @Override
    public List<Leitura> gerar(int totalLeituras) {
        List<Leitura> leituras = new ArrayList<>();

        Random randomizador = new Random();
        for (int i = 0; i < totalLeituras; i++) {
            int oscilacao = TEMPERATURA_NORMAL * randomizador.nextInt(OSCILACAO_MAXIMA_TEMPERATURA)/100;
            int temperatura = (randomizador.nextBoolean()? TEMPERATURA_NORMAL + oscilacao: TEMPERATURA_NORMAL - oscilacao);

            oscilacao = CARGA_BATERIA_NORMAL * randomizador.nextInt(OSCILACAO_MAXIMA_CARGA_BATERIA)/100;
            int cargaBateria = Math.max(0, (randomizador.nextBoolean()? CARGA_BATERIA_NORMAL - oscilacao: CARGA_BATERIA_NORMAL - oscilacao)); // Garante não negativo

            Leitura leitura = new Leitura(temperatura, cargaBateria);
            leituras.add(leitura);
        }

        return leituras;
    }
    
}
