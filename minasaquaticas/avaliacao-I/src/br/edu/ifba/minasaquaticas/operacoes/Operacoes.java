package br.edu.ifba.minasaquaticas.operacoes;

import java.util.List;
import java.util.Map;

import br.edu.ifba.minasaquaticas.impl.Mina;
import br.edu.ifba.minasaquaticas.ordenador.TipoOrdenacao;

public interface Operacoes<Monitorado, Leitura> {

    // D.1 - Implementar
    public void imprimirMonitorado(List<Monitorado> monitorado);

    // D.2 - Implementar
    public void imprimir(Map<Monitorado, List<Leitura>> leituras);

    // D.3 - Implementar
    public Map<Monitorado, List<Leitura>> ordenar(Map<Monitorado, List<Leitura>> leituras, TipoOrdenacao tipoOrdenacao);

    // D.4 - Implementar
    public int detectarObjetoEmTransito(Map<Mina, List<Leitura>> leiturasPorMinas, int limiteProximidadeCritica);

}
