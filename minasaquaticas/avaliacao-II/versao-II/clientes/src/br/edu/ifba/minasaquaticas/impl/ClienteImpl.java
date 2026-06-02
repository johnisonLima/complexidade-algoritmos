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
    private static final String URL_MINAS =
        URL_SERVIDOR + "/minas/";

    /**
     * Endpoint utilizado para envio dos eventos detectados.
     */
    private static final String URL_TRANSITO =
        URL_SERVIDOR + "/minas/transito/";

    /**
     * Limiar mínimo para envio de alterações de profundidade.
     */
    private static final int LIMIAR_ENVIO_PROFUNDIDADE = 10;

    /**
     * Limiar mínimo para envio de alterações de proximidade.
     */
    private static final int LIMIAR_ENVIO_PROXIMIDADE = 10;

    private Mina mina;

    private Sensoriamento<Leitura> sensoriamento;

    /**
     * Métricas para comparação entre as versões.
     */
    private int leiturasEnviadas = 0;
    private int leiturasIgnoradas = 0;
    private int eventosDetectados = 0;

    @Override
    public void configurar(
        Mina mina,
        Sensoriamento<Leitura> sensoriamento
    ) {
        this.mina = mina;
        this.sensoriamento = sensoriamento;
    }

    /**
     * Detecta uma possível movimentação suspeita
     * próxima à mina.
     *
     * Neste projeto consideramos suspeito quando
     * a proximidade atual entra em uma faixa crítica.
     */
    @Override
    public boolean ocorreuMovimentoSuspeito(
        Leitura leituraAtual,
        Leitura ultimaLeitura,
        int limiarProfundidade,
        int limiarProximidade
    ) {

        return leituraAtual.getProximidade()
            <= limiarProximidade;
    }

    /**
     * Envia uma leitura para o servidor.
     */
    @Override
    public Resultado enviar(
        Leitura leitura
    ) throws Exception {

        Resultado resultado =
            Resultado.SUCESSO;

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
            (HttpURLConnection)
                urlEnvio.openConnection();

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

    /**
     * Envia ao servidor a quantidade de eventos
     * detectados pela mina.
     */
    @Override
    public Resultado enviar(
        int eventos
    ) throws Exception {

        Resultado resultado =
            Resultado.SUCESSO;

        URL urlEnvio = new URL(
            URL_TRANSITO
            + mina.getId()
            + "/"
            + eventos
        );

        HttpURLConnection conexao =
            (HttpURLConnection)
                urlEnvio.openConnection();

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
            sensoriamento.obterDados(
                TOTAL_LEITURAS
            );

        Leitura ultimaLeitura = null;

        for (Leitura leitura : leituras) {

            try {

                /**
                 * Primeira leitura sempre é enviada.
                 */
                if (ultimaLeitura == null) {

                    enviar(leitura);

                    leiturasEnviadas++;

                    ultimaLeitura = leitura;

                    continue;
                }

                boolean enviarLeitura =
                    deveEnviarLeitura(
                        leitura,
                        ultimaLeitura
                    );

                if (enviarLeitura) {

                    enviar(leitura);

                    leiturasEnviadas++;

                    System.out.println(
                        mina
                        + " enviou leitura relevante -> "
                        + leitura
                    );

                } else {

                    leiturasIgnoradas++;
                }

                /**
                 * Detecta eventos independentemente
                 * da decisão de transmissão.
                 */
                if (
                    ocorreuMovimentoSuspeito(
                        leitura,
                        ultimaLeitura,
                        LIMIAR_ENVIO_PROFUNDIDADE,
                        LIMIAR_ENVIO_PROXIMIDADE
                    )
                ) {
                    eventosDetectados++;
                }

                ultimaLeitura = leitura;

                Thread.sleep(50);

            } catch (Exception e) {

                e.printStackTrace();
            }
        }

        try {

            /**
             * Ao final da execução a mina envia
             * ao servidor a quantidade de eventos
             * detectados localmente.
             */
            enviar(eventosDetectados);

        } catch (Exception e) {

            e.printStackTrace();
        }

        System.out.println(
            "\nResumo da "
            + mina
            + "\nLeituras enviadas: "
            + leiturasEnviadas
            + "\nLeituras ignoradas: "
            + leiturasIgnoradas
            + "\nEventos detectados: "
            + eventosDetectados
            + "\n"
        );
    }

    /**
     * Verifica se a leitura deve ser enviada ao servidor.
     *
     * A leitura somente será transmitida quando
     * ocorrer uma variação significativa em relação
     * à última leitura registrada.
     */
    private boolean deveEnviarLeitura(
        Leitura leituraAtual,
        Leitura ultimaLeitura
    ) {

        int diferencaProfundidade =
            Math.abs(
                leituraAtual.getProfundidade()
                - ultimaLeitura.getProfundidade()
            );

        int diferencaProximidade =
            Math.abs(
                leituraAtual.getProximidade()
                - ultimaLeitura.getProximidade()
            );

        return
            diferencaProfundidade >= LIMIAR_ENVIO_PROFUNDIDADE
            ||
            diferencaProximidade >= LIMIAR_ENVIO_PROXIMIDADE;
    }

}