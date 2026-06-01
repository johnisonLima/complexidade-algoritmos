package br.edu.ifba.nobreaks.operacoes;

import java.util.List;
import java.util.Map;

import br.edu.ifba.nobreaks.impl.Nobreak;
import br.edu.ifba.nobreaks.ordenador.TipoOrdenacao;

public interface Operacoes<Monitorado, Leitura> {

    // implementando d.1
    public void imprimir(List<Monitorado> monitorados);

    // implementando d.2
    public void imprimir(Map<Monitorado, List<Leitura>> leituras);

    // implementando d.3
    public Map<Monitorado, List<Leitura>> ordenar(Map<Monitorado, List<Leitura>> leituras, TipoOrdenacao tipoOrdenacao);

    // implementando d.4
    public int contarAltasOscilacoes(Map<Nobreak, List<Leitura>> leiturasPorNobreaks, int limiteOscilacaoTemperatura, int limiteOscilacaoCargaBateria);
    
}

