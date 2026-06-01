package br.edu.ifba.nobreaks.clientes.comunicacao;

import br.edu.ifba.nobreaks.clientes.sensoriamento.Sensoriamento;

public interface Cliente<Monitorado, Leitura> {

    public void configurar(Monitorado monitorado, Sensoriamento<Leitura> sensoriamento);

    public boolean ocorreuAltaOscilacao(Leitura leituraAtual, Leitura ultimaLeitura, int limiarOscilacaoTemperatura, int limiarOscilacaoCarga);

    public Resultado enviar(Leitura leitura) throws Exception;

    public Resultado enviar(int oscilacoes) throws Exception;

}
