package br.edu.ifba.minasaquaticas.comunicacao;

import br.edu.ifba.minasaquaticas.sensoriamento.Sensoriamento;

public interface Cliente<Monitorado, Leitura> {

    /**
     * Associa uma mina ao mecanismo de sensoriamento.
     */
    public void configurar(
        Monitorado monitorado,
        Sensoriamento<Leitura> sensoriamento
    ) throws Exception;

    /**
     * Verifica se ocorreu uma alteração significativa
     * entre duas leituras consecutivas.
     *
     * Objetivo:
     * reduzir o tráfego de rede enviando apenas
     * leituras relevantes ao servidor.
     */
    public boolean ocorreuMovimentoSuspeito(
        Leitura leituraAtual,
        Leitura ultimaLeitura,
        int limiarProfundidade,
        int limiarProximidade
    );

    /**
     * Envia uma leitura ao servidor.
     */
    public Resultado enviar(
        Leitura leitura
    ) throws Exception;

    /**
     * Envia ao servidor a quantidade de eventos
     * detectados pela mina.
     */
    public Resultado enviar(
        int eventos
    ) throws Exception;
}