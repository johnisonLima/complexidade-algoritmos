package src.br.edu.ifba.encriptacao.impl;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import src.br.edu.ifba.encriptacao.encriptador.Encriptador;
import src.br.edu.ifba.encriptacao.excecoes.FalhaEncriptacao;

public class EncriptadorImpl extends Encriptador {

    public EncriptadorImpl(KeyPair chaves, String algoritmoDeEncriptacao) {
        super(chaves, algoritmoDeEncriptacao);
    }

    @Override
    public String encriptar(String dados) throws FalhaEncriptacao {
        String encriptacao = "";

        synchronized (encriptacao) {
            try {
                Cipher cifrador = Cipher.getInstance(algoritmoDeEncriptacao);
                cifrador.init(Cipher.ENCRYPT_MODE, chaves.getPublic());

                byte[] cifragem = cifrador.doFinal(dados.getBytes(StandardCharsets.UTF_8));
                encriptacao = Base64.getEncoder().encodeToString(cifragem);
            }
            catch (NoSuchAlgorithmException e) {
                throw new FalhaEncriptacao("falha encriptando dados: " + e.getMessage());
            } catch (NoSuchPaddingException e) {
                throw new FalhaEncriptacao("falha encriptando dados: " + e.getMessage());
            } catch (InvalidKeyException e) {
                throw new FalhaEncriptacao("falha encriptando dados: " + e.getMessage());
            } catch (IllegalBlockSizeException e) {
                throw new FalhaEncriptacao("falha encriptando dados: " + e.getMessage());
            } catch (BadPaddingException e) {
                throw new FalhaEncriptacao("falha encriptando dados: " + e.getMessage());
            }

        }
        
        return encriptacao;
    }

    @Override
    public String desencriptar(String encriptacao) throws FalhaEncriptacao {
        String dados = null;

        try {
            Cipher cifrador = Cipher.getInstance(algoritmoDeEncriptacao);
            cifrador.init(Cipher.DECRYPT_MODE, chaves.getPrivate());

            byte[] bytes = Base64.getDecoder().decode(encriptacao);
            byte[] bytesDecriptados = cifrador.doFinal(bytes);

            dados = new String(bytesDecriptados, StandardCharsets.UTF_8);
        } catch (NoSuchAlgorithmException e) {
            throw new FalhaEncriptacao("falha encriptando dados: " + e.getMessage());
        } catch (NoSuchPaddingException e) {
            throw new FalhaEncriptacao("falha encriptando dados: " + e.getMessage());
        } catch (InvalidKeyException e) {
            throw new FalhaEncriptacao("falha encriptando dados: " + e.getMessage());
        } catch (IllegalBlockSizeException e) {
            throw new FalhaEncriptacao("falha encriptando dados: " + e.getMessage());
        } catch (BadPaddingException e) {
            throw new FalhaEncriptacao("falha encriptando dados: " + e.getMessage());
        }

        return dados;
    }

}
