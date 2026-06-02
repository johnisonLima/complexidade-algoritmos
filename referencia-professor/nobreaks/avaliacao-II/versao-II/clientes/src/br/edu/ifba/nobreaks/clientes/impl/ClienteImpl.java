package br.edu.ifba.nobreaks.clientes.impl;

import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.Cipher;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.edu.ifba.nobreaks.clientes.comunicacao.Cliente;
import br.edu.ifba.nobreaks.clientes.comunicacao.Resultado;
import br.edu.ifba.nobreaks.clientes.sensoriamento.Sensoriamento;

public class ClienteImpl implements Cliente<NoBreak, Leitura>, Runnable {

    private static final int TOTAL_DE_LEITURAS = 1000;

    private static final String URL_SERVIDOR = "http://localhost:8080";
    private static final String URL_NOBREAKS = URL_SERVIDOR + "/nobreaks/";

    private static final String ALGORITMO_ENCRIPTACAO = "RSA";
    private static final String CAMINHO_CHAVE_PUBLICA = "/misc/ifba/workspaces/complexidade/09/clientes/chave/ch_publica.chv";

    private NoBreak noBreak = null;
    private Sensoriamento<Leitura> sensoriamento = null;

    private static final int LIMIAR_ENVIO_CARGA_BATERIA = 5;
    private static final int LIMIAR_ENVIO_TEMPERATURA = 5;

    private static final int LIMIAR_OSCILACOES_TEMPERATURA = 10;
    private static final int LIMIAR_OSCILACOES_CARGA = 10;

    private Leitura ultimaLeitura = new Leitura(0, 0);

    private PublicKey chave = null;

    @Override
    public void configurar(NoBreak noBreak, Sensoriamento<Leitura> sensoriamento) throws Exception {
        this.noBreak = noBreak;
        this.sensoriamento = sensoriamento;
        this.chave = getChave();
    }

    private PublicKey getChave() throws Exception {
        File arquivo = new File(CAMINHO_CHAVE_PUBLICA);
        FileInputStream stream = new FileInputStream(arquivo);

        byte[] bytes = stream.readAllBytes();
        stream.close();

        X509EncodedKeySpec spec = new X509EncodedKeySpec(bytes);
        KeyFactory kf = KeyFactory.getInstance(ALGORITMO_ENCRIPTACAO);
        
        return kf.generatePublic(spec);
    }

    private byte[] encriptar(String dados) throws Exception {
        Cipher cifrador = Cipher.getInstance(ALGORITMO_ENCRIPTACAO);
        cifrador.init(Cipher.ENCRYPT_MODE, chave);

        byte[] encriptado = cifrador.doFinal(dados.getBytes());

        return encriptado;
    }

    @SuppressWarnings("deprecation")
    @Override
    public Resultado enviar(Leitura leitura) throws Exception {
        Resultado resultado = Resultado.SUCESSO;

        Map<String, String> json = new HashMap<>();
        json.put("id", noBreak.getIdentificacao());
        json.put("temperatura", leitura.getTemperatura() + "");
        json.put("carga", leitura.getCargaBateria() + "");

        ObjectMapper mapeador = new ObjectMapper();
        URL urlEnvio = new URL(URL_NOBREAKS + "leituras/" + new String(Base64.getUrlEncoder().encode(encriptar(mapeador.writeValueAsString(json)))));

        HttpURLConnection conexao = (HttpURLConnection) urlEnvio.openConnection();
        conexao.setRequestMethod("POST");
        if (conexao.getResponseCode() != 200) {
            resultado = Resultado.ERRO;

            throw new Exception("erro de conexão com o servidor");
        }
        conexao.disconnect();

        return resultado;
    }

    @SuppressWarnings("deprecation")
    @Override
    public Resultado enviar(int oscilacoes) throws Exception {
        Resultado resultado = Resultado.SUCESSO;

        Map<String, String> json = new HashMap<>();
        json.put("id", noBreak.getIdentificacao());
        json.put("oscilacoes", oscilacoes + "");

        ObjectMapper mapeador = new ObjectMapper();
        URL urlEnvio = new URL(URL_NOBREAKS + "oscilacoes/" + new String(Base64.getUrlEncoder().encode(encriptar(mapeador.writeValueAsString(json)))));

        HttpURLConnection conexao = (HttpURLConnection) urlEnvio.openConnection();
        conexao.setRequestMethod("POST");
        if (conexao.getResponseCode() != 200) {
            resultado = Resultado.ERRO;

            throw new Exception("erro de conexão com o servidor");
        }
        conexao.disconnect();

        return resultado;
    }

    @Override
    public boolean ocorreuAltaOscilacao(Leitura leituraAtual, Leitura ultimaLeitura, int limiarOscilacaoTemperatura, int limiarOscilacaoCarga) {
        int oscilacaoTemperatura = Math.abs(leituraAtual.getTemperatura() - ultimaLeitura.getTemperatura());
        int oscilacaoBateria = Math.abs(leituraAtual.getCargaBateria() - ultimaLeitura.getCargaBateria());

        return (oscilacaoBateria > limiarOscilacaoCarga || oscilacaoTemperatura > limiarOscilacaoTemperatura);
    }

    @Override
    public void run() {
        List<Leitura> leituras = sensoriamento.gerar(TOTAL_DE_LEITURAS);

        for (Leitura leitura: leituras) {
            int diferencaTemperatura = Math.abs(leitura.getTemperatura() - ultimaLeitura.getTemperatura());
            int diferencaCarga = Math.abs(leitura.getCargaBateria() - ultimaLeitura.getCargaBateria());

            if (diferencaCarga > LIMIAR_ENVIO_CARGA_BATERIA || diferencaTemperatura > LIMIAR_ENVIO_TEMPERATURA) {
                System.out.println("leitura e altas oscilações sendo enviadas...");

                try {
                    enviar(leitura);

                    boolean temAltaOscilacao = ocorreuAltaOscilacao(leitura, ultimaLeitura, LIMIAR_OSCILACOES_TEMPERATURA, LIMIAR_OSCILACOES_CARGA);
                    enviar(temAltaOscilacao? 1: 0);

                    Thread.sleep(50);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                ultimaLeitura = leitura;
            } else {
                System.out.println("não ocorreram diferenças significativas desde a última leitura");
            }
        }
    }

}
