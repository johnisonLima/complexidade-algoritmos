package br.edu.ifba.nobreaks;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import br.edu.ifba.nobreaks.impl.OperacoesImpl;
import br.edu.ifba.nobreaks.impl.Nobreak;
import br.edu.ifba.nobreaks.impl.SensoriamentoImpl;
import br.edu.ifba.nobreaks.impl.Leitura;
import br.edu.ifba.nobreaks.operacoes.Operacoes;
import br.edu.ifba.nobreaks.ordenador.TipoOrdenacao;
import br.edu.ifba.nobreaks.sensoriamento.Sensoriamento;

public class App {

    private static final int TOTAL_NOBREAKS = 10;
    private static final int TOTAL_LEITURAS = 10;
    
    private static final int LIMITE_OSCILACAO_TEMPERATURA = 5;
    private static final int LIMITE_OSCILACAO_CARGA_BATERIA = 15;

    public static void main(String[] args) throws Exception {
        Sensoriamento<Leitura> sensoriamento = new SensoriamentoImpl();

        // gerando leituras para N nobreaks
        Map<Nobreak, List<Leitura>> leituras = new TreeMap<>();
        for (int i = 0; i < TOTAL_NOBREAKS; i++) {
            leituras.put(new Nobreak(i + "", "Modelo #" + i), sensoriamento.gerar(TOTAL_LEITURAS));
        }

        Operacoes<Nobreak, Leitura> operacoes = new OperacoesImpl();

        // d.1 imprimindo os nobreaks
        System.out.println("imprimindo os nobreaks:");
        operacoes.imprimir(new ArrayList<Nobreak>(leituras.keySet()));

        // d2. imprimindo leituras por nobreak
        System.out.println("imprimindo leituras por cada nobreak:");
        operacoes.imprimir(leituras);

        // d3. ordenando os dados das leituras por nobreak
        System.out.println("ordenando as leituras de temperatura por cada nobreak:");
        Map<Nobreak, List<Leitura>> leiturasOrdenadas = operacoes.ordenar(leituras, TipoOrdenacao.POR_CARGA_BATERIA);
        operacoes.imprimir(leiturasOrdenadas);

        // d4. procurando por um padrao de temperaturas nas leituras
        List<Leitura> padrao = new ArrayList<>();
        padrao.add(new Leitura(20, 80));
        padrao.add(new Leitura(25, 80));

        System.out.println("procurando/contando oscilacoes");
        int oscilacoes = operacoes.contarAltasOscilacoes(leituras, LIMITE_OSCILACAO_TEMPERATURA, LIMITE_OSCILACAO_CARGA_BATERIA);
        if (oscilacoes > 0) {
            System.out.println("número de altas oscilações encontradas: " + oscilacoes);
        } else {
            System.out.println("nenhuma alta oscilação encontrada");
        }
    }
}
