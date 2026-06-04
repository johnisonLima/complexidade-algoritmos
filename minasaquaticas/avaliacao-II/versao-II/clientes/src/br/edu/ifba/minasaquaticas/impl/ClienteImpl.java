package br.edu.ifba.minasaquaticas.impl;

// ENCRIPTAÇÃO
import java.io.File;
import java.io.FileInputStream;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.Cipher;

import java.util.Base64;
import java.util.Map;
import java.util.TreeMap;

import com.fasterxml.jackson.databind.ObjectMapper;
// ****************************

import io.github.cdimascio.dotenv.Dotenv;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import br.edu.ifba.minasaquaticas.comunicacao.Cliente;
import br.edu.ifba.minasaquaticas.comunicacao.Resultado;
import br.edu.ifba.minasaquaticas.sensoriamento.Sensoriamento;

public class ClienteImpl implements Cliente<Mina, Leitura>, Runnable {
    private static final Dotenv dotenv = Dotenv.configure().directory("../").load();

    /**
    * Quantidade de leituras geradas por cada mina.
    */
    private static final int TOTAL_LEITURAS = 100;

    /**
    * URL base do servidor.
    */
    private static final String URL_SERVIDOR = "http://localhost:8080";

    /**
    * Endpoint para envio de leituras.
    */
    private static final String URL_LEITURAS = URL_SERVIDOR + "/minas/leituras/";

    /**
    * Endpoint para envio de eventos.
    */
    private static final String URL_EVENTOS = URL_SERVIDOR + "/minas/eventos/";

    /**
    * Algoritmo utilizado para criptografia.
    */
    private static final String ALGORITMO_ENCRIPTACAO = "RSA";

    /**
    * Caminho da chave pública.
    */
    private static final String CAMINHO_CHAVE_PUBLICA = dotenv.get("PUBLIC_KEY_PATH");

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
    * Chave pública utilizada para criptografia.
    */
    private PublicKey chave = null;

    /**
    * Métricas para comparação entre as versões.
    */
    private int leiturasEnviadas = 0;
    private int leiturasIgnoradas = 0;
    private int eventosDetectados = 0;

    @Override
    public void configurar(Mina mina, Sensoriamento<Leitura> sensoriamento) throws Exception {

        this.mina = mina;
        this.sensoriamento = sensoriamento;

        this.chave = getChave();
    }

    /**
    * Detecta uma possível movimentação suspeita próxima à mina.
    *
    * Neste projeto consideramos suspeito quando a proximidade atual entra em uma faixa crítica.
    */
    @Override
    public boolean ocorreuMovimentoSuspeito(Leitura leituraAtual, Leitura ultimaLeitura, int limiarProfundidade, int limiarProximidade) {

        return leituraAtual.getProximidade() <= limiarProximidade;
    }

    /**
    * Envia uma leitura para o servidor.
    */
    @Override
    public Resultado enviar(Leitura leitura) throws Exception {

        Resultado resultado = Resultado.SUCESSO;

        Map<String, String> dados = new TreeMap<>();

        dados.put("id", mina.getId().toString());

        dados.put("modelo", mina.getModelo().toString());

        dados.put("profundidade", leitura.getProfundidade().toString());

        dados.put("proximidade",leitura.getProximidade().toString());

        String envio = prepararEnvio(dados);

        URL urlEnvio = new URL(URL_LEITURAS + envio);

        HttpURLConnection conexao = (HttpURLConnection) urlEnvio.openConnection();

        conexao.setRequestMethod("POST");

        if(conexao.getResponseCode() != 200){

            resultado = Resultado.ERRO;

            throw new Exception("Erro de comunicação com o servidor.");
        }

        conexao.disconnect();

        return resultado;
    }

    /**
    * Converte um objeto em JSON,
    * criptografa e codifica em Base64.
    */
    private String prepararEnvio(Map<String, String> dados) throws Exception {

        ObjectMapper mapeador = new ObjectMapper();

        String json = mapeador.writeValueAsString(dados);

        byte[] bytesEncriptados = encriptar(json);

        return Base64.getUrlEncoder().encodeToString(bytesEncriptados);
    }

    /**
    * Envia ao servidor a quantidade de eventos
    * detectados pela mina.
    */
    @Override
    public Resultado enviar(int eventos) throws Exception {

        Resultado resultado = Resultado.SUCESSO;

        Map<String, String> dados = new TreeMap<>();

        dados.put("id", mina.getId().toString());

        dados.put("modelo", mina.getModelo().toString());

        dados.put("eventos", String.valueOf(eventos));

        String envio = prepararEnvio(dados);

        URL urlEnvio = new URL(URL_EVENTOS + envio);

        HttpURLConnection conexao = (HttpURLConnection) urlEnvio.openConnection();

        conexao.setRequestMethod("POST");

        if(conexao.getResponseCode() != 200){

            resultado = Resultado.ERRO;

            throw new Exception("Erro de comunicação com o servidor.");
        }

        conexao.disconnect();

        return resultado;
    }

    /**
    * Carrega a chave pública utilizada
    * para criptografar os dados.
    */
    private PublicKey getChave() throws Exception {

        File arquivo = new File(CAMINHO_CHAVE_PUBLICA);

        FileInputStream stream = new FileInputStream(arquivo);

        byte[] bytes = stream.readAllBytes();

        stream.close();

        X509EncodedKeySpec spec = new X509EncodedKeySpec(bytes);

        KeyFactory fabrica = KeyFactory.getInstance(ALGORITMO_ENCRIPTACAO);

        return fabrica.generatePublic(spec);
    }

    /**
    * Criptografa uma String utilizando RSA.
    */
    private byte[] encriptar(String dados) throws Exception {

        Cipher cifrador = Cipher.getInstance(ALGORITMO_ENCRIPTACAO);

        cifrador.init(Cipher.ENCRYPT_MODE, chave);

        return cifrador.doFinal(dados.getBytes());
    }

    @Override
    public void run() {

        List<Leitura> leituras = sensoriamento.obterDados(TOTAL_LEITURAS);

        Leitura ultimaLeitura = null;

        for(Leitura leitura : leituras){

            try {
                /**
                 * Primeira leitura sempre é enviada.
                 */
                if(ultimaLeitura == null) {

                    enviar(leitura);

                    leiturasEnviadas++;
                    ultimaLeitura = leitura;
                    continue;
                }

                boolean enviarLeitura = deveEnviarLeitura( leitura, ultimaLeitura);

                if(enviarLeitura){

                    enviar(leitura);

                    leiturasEnviadas++;

                    System.out.println(mina + " enviou leitura relevante -> " + leitura);
                } else {
                    leiturasIgnoradas++;
                }

                /**
                 * Detecta eventos independentemente
                 * da decisão de transmissão.
                 */
                if(ocorreuMovimentoSuspeito(leitura, ultimaLeitura, LIMIAR_ENVIO_PROFUNDIDADE, LIMIAR_ENVIO_PROXIMIDADE)){
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

        System.out.println("\nResumo da " + mina + "\nLeituras enviadas: " + leiturasEnviadas + 
            "\nLeituras ignoradas: " + leiturasIgnoradas + "\nEventos detectados: " + eventosDetectados + "\n");
    }

    /**
    * Verifica se a leitura deve ser enviada ao servidor.
    *
    * A leitura somente será transmitida quando
    * ocorrer uma variação significativa em relação
    * à última leitura registrada.
    */
    private boolean deveEnviarLeitura(Leitura leituraAtual, Leitura ultimaLeitura) {

        int diferencaProfundidade = Math.abs(leituraAtual.getProfundidade() - ultimaLeitura.getProfundidade());

        int diferencaProximidade = Math.abs(leituraAtual.getProximidade() - ultimaLeitura.getProximidade());

        return diferencaProfundidade >= LIMIAR_ENVIO_PROFUNDIDADE || diferencaProximidade >= LIMIAR_ENVIO_PROXIMIDADE;
    }

}