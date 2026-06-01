package br.edu.ifba.minasaquaticas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.edu.ifba.minasaquaticas.impl.Leitura;
import br.edu.ifba.minasaquaticas.impl.Mina;
import br.edu.ifba.minasaquaticas.impl.OperacoesImpl;
import br.edu.ifba.minasaquaticas.sensoriamento.Sensoriamento;
import br.edu.ifba.minasaquaticas.impl.SensoriamentoImpl;
import br.edu.ifba.minasaquaticas.operacoes.Operacoes;
import br.edu.ifba.minasaquaticas.ordenador.TipoOrdenacao;

public class MinasAguaticas {

    private static final int TOTAL_MINAS = 10;
    private static final int TOTAL_LEITURAS = 10;
    private static final int LIMITE_PROXIMIDADE_CRITICA = 5;

    public static void main(String[] args) throws Exception {
        Sensoriamento<Leitura> sensoriamento = new SensoriamentoImpl();

        Map<Mina, List<Leitura>> leituras = new HashMap<>();

        for (int i = 0; i < TOTAL_MINAS; i++) {
            leituras.put(new Mina(i, (int) (i+1 * 2.5)), sensoriamento.obterDados(TOTAL_LEITURAS));
        }

        Operacoes<Mina, Leitura> operacoes = new OperacoesImpl();        

        // // D.1 Imprimindo Minas Aquáticas
        System.out.println("Minas Aquáticas sendo monitoradas:");
        System.out.println("---------------------------------");
        operacoes.imprimirMonitorado(new ArrayList<Mina>(leituras.keySet()));

        // D.2 Imprimindo Leituras
        System.out.println("\nLeituras das Minas Aquáticas:");
        System.out.println("---------------------------------");
        operacoes.imprimir(leituras);

        // D.3 Ordenando Leituras por Profundidade ou Proximidade
        TipoOrdenacao tipoOrdenacao = TipoOrdenacao.POR_PROFUNDIDADE; 

        Map<Mina, List<Leitura>> leiturasOrdenadas = operacoes.ordenar(leituras, tipoOrdenacao);
        
        System.out.println("Ordenando leituras " + tipoOrdenacao);
        System.out.println("---------------------------------");

        operacoes.imprimir(leiturasOrdenadas);

        // // D.4 Detectando Objetos em Trânsito        
        int objetosEmTransito = operacoes.detectarObjetoEmTransito(leituras, LIMITE_PROXIMIDADE_CRITICA);
        System.out.println("\nNúmero de objetos em trânsito entre minas: " + objetosEmTransito);

        
    }
}
