package br.edu.ifba.minasaquaticas.impl;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import br.edu.ifba.minasaquaticas.comunicacao.Cliente;
import br.edu.ifba.minasaquaticas.comunicacao.Resultado;
import br.edu.ifba.minasaquaticas.sensoriamento.Sensoriamento;

public class ClienteImpl implements Cliente<Mina, Leitura>, Runnable {

    /**
     * Quantidade de leituras geradas por cada mina.
     */
    private static final int TOTAL_LEITURAS = 100;

    /**
     * URL base do servidor.
     */
    private static final String URL_SERVIDOR = "http://localhost:8080";

    /**
     * Endpoint utilizado para envio das leituras.
     */
    private static final String URL_MINAS = URL_SERVIDOR + "/minas/";

    private Mina mina;

    private Sensoriamento<Leitura> sensoriamento;

    @Override
    public void configurar(
        Mina mina,
        Sensoriamento<Leitura> sensoriamento
    ) {
        this.mina = mina;
        this.sensoriamento = sensoriamento;
    }

    @Override
    public Resultado enviar(Leitura leitura) throws Exception {

        Resultado resultado = Resultado.SUCESSO;

        URL urlEnvio = new URL(
            URL_MINAS
            + mina.getId()
            + "/"
            + mina.getModelo()
            + "/"
            + leitura.getProfundidade()
            + "/"
            + leitura.getProximidade()
        );

        HttpURLConnection conexao =
            (HttpURLConnection) urlEnvio.openConnection();

        conexao.setRequestMethod("POST");

        if (conexao.getResponseCode() != 200) {

            resultado = Resultado.ERRO;

            throw new Exception(
                "Erro de comunicação com o servidor."
            );
        }

        conexao.disconnect();

        return resultado;
    }

    @Override
    public void run() {

        List<Leitura> leituras =
            sensoriamento.obterDados(TOTAL_LEITURAS);

        for (Leitura leitura : leituras) {

            try {

                enviar(leitura);

                System.out.println(
                    mina
                    + " enviou leitura -> "
                    + leitura
                );

                Thread.sleep(50);

            } catch (Exception e) {

                e.printStackTrace();
            }
        }
    }
}