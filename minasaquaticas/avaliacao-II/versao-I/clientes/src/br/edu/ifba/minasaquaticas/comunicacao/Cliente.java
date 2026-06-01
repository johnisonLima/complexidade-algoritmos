package br.edu.ifba.minasaquaticas.comunicacao;

import br.edu.ifba.minasaquaticas.sensoriamento.Sensoriamento;

public interface Cliente<Mina, Leitura> {

    void configurar(
        Mina mina,
        Sensoriamento<Leitura> sensoriamento
    );

    Resultado enviar(Leitura leitura)
        throws Exception;
}