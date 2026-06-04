package br.edu.ifba.minasaquaticas.encriptacao.impl;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import br.edu.ifba.minasaquaticas.encriptacao.encriptador.Encriptador;
import br.edu.ifba.minasaquaticas.encriptacao.excecoes.FalhaEncriptacao;

public class EncriptadorImpl extends Encriptador {

    public EncriptadorImpl(KeyPair chaves, String algoritmoDeEncriptacao) {

        super(chaves, algoritmoDeEncriptacao);
    }

    /**
    * Criptografa os dados utilizando
    * a chave pública.
    */
    @Override
    public String encriptar(String dados) throws FalhaEncriptacao {

        String encriptacao = "";

        try {

            Cipher cifrador = Cipher.getInstance(algoritmoDeEncriptacao);

            cifrador.init(Cipher.ENCRYPT_MODE, chaves.getPublic());

            byte[] cifragem = cifrador.doFinal(dados.getBytes(StandardCharsets.UTF_8));

            encriptacao = Base64.getEncoder().encodeToString(cifragem);

        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException
            | IllegalBlockSizeException | BadPaddingException e) {

            throw new FalhaEncriptacao("falha encriptando dados: " + e.getMessage());
        }

        return encriptacao;
    }

    /**
    * Descriptografa os dados utilizando
    * a chave privada.
    */
    @Override
    public String desencriptar(String encriptacao) throws FalhaEncriptacao {

        String dados = null;

        try {

            Cipher cifrador = Cipher.getInstance(algoritmoDeEncriptacao);

            cifrador.init(Cipher.DECRYPT_MODE, chaves.getPrivate());

            byte[] bytes = Base64.getDecoder().decode(encriptacao);

            byte[] bytesDecriptados = cifrador.doFinal(bytes);

            dados = new String(bytesDecriptados, StandardCharsets.UTF_8);

        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException
            | IllegalBlockSizeException | BadPaddingException e) {

            throw new FalhaEncriptacao("falha desencriptando dados: " + e.getMessage());
        }

        return dados;
    }
}