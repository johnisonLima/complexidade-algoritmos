package br.edu.ifba.minasaquaticas;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.edu.ifba.minasaquaticas.impl.Leitura;
import br.edu.ifba.minasaquaticas.impl.Mina;
import br.edu.ifba.minasaquaticas.impl.OperacoesImpl;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;

@Path("/minas")
public class Rotas {

    /**
    * Estrutura central do servidor.
    */
    private static final OperacoesImpl operacoes = new OperacoesImpl();

    /**
    * Algoritmo utilizado para criptografia.
    */
    private static final String ALGORITMO_ENCRIPTACAO = "RSA";

    /**
    * Caminho da chave privada.
    */
    private static final String CAMINHO_CHAVE_PRIVADA = "E:\\MEGAsync\\projectServ\\www\\projetos_pessoais\\complexidade-algoritmos\\minasaquaticas\\avaliacao-II\\versao-II\\servidor\\chave\\ch_privada.txt";

    /**
    * Chave privada carregada uma única vez.
    */
    private PrivateKey chave = null;

    /**
    * Carrega a chave privada.
    */
    private PrivateKey getChavePrivada() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {

        if (chave == null) {

            File arquivo = new File(CAMINHO_CHAVE_PRIVADA);

            FileInputStream stream = new FileInputStream(arquivo);

            byte[] bytes = stream.readAllBytes();

            stream.close();

            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(bytes);

            KeyFactory fabrica = KeyFactory.getInstance(ALGORITMO_ENCRIPTACAO);

            chave = fabrica.generatePrivate(spec);
        }

        return chave;
    }

    /**
    * Descriptografa dados recebidos.
    */
    private String desencriptar(byte[] encriptado) throws NoSuchAlgorithmException, NoSuchPaddingException,
        InvalidKeyException, InvalidKeySpecException, IOException, IllegalBlockSizeException, BadPaddingException {

        Cipher cifrador = Cipher.getInstance(ALGORITMO_ENCRIPTACAO);

        cifrador.init(Cipher.DECRYPT_MODE, getChavePrivada());

        byte[] desencriptado = cifrador.doFinal(encriptado);

        return new String(desencriptado);
    }

    /**
    * Recebe leituras criptografadas.
    */
    @POST
    @Path("/leituras/{dados}")
    public Response gravarLeitura(@PathParam("dados") String dados) {

        Response resposta = Response.serverError().build();

        try {

            String json = desencriptar(Base64.getUrlDecoder().decode(dados));

            ObjectMapper mapeador = new ObjectMapper();

            JsonNode dic = mapeador.readTree(json);

            Mina mina = new Mina(dic.get("id").asInt(), dic.get("modelo").asInt());

            Leitura leitura = new Leitura(dic.get("profundidade").asInt(), dic.get("proximidade").asInt());

            operacoes.gravar(mina, leitura);

            System.out.println("Leitura recebida -> " + mina + " " + leitura);

            resposta = Response.ok().build();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return resposta;
    }

    /**
    * Recebe eventos detectados pelo cliente.
    */
    @POST
    @Path("/eventos/{dados}")
    public Response gravarEventos(@PathParam("dados") String dados) {

        Response resposta = Response.serverError().build();

        try {

            String json = desencriptar(Base64.getUrlDecoder().decode(dados));

            ObjectMapper mapeador = new ObjectMapper();

            JsonNode dic = mapeador.readTree(json);

            Mina mina = new Mina(dic.get("id").asInt(), dic.get("modelo").asInt());

            int eventos = dic.get("eventos").asInt();

            operacoes.gravar(mina, eventos);

            System.out.println("Eventos recebidos -> " + mina + " Quantidade: " + eventos);

            resposta = Response.ok().build();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return resposta;
    }

    /**
    * Verifica se o servidor está ativo.
    */
    @GET
    @Path("/status")
    public String status() {
        return "Servidor de Minas Aquaticas ativo.";
    }

    /**
    * Quantidade de minas e leituras.
    */
    @GET
    @Path("/quantidade")
    public String quantidade() {

        return "Minas monitoradas: " + operacoes.quantidadeMinas()
            + "\nLeituras recebidas: " + operacoes.quantidadeLeituras();
    }

    /**
    * Total de eventos detectados.
    */
    @GET
    @Path("/transito")
    public String detectarTransito() {

        return "Eventos de transito detectados: " + operacoes.quantidadeEventos();
    }
}