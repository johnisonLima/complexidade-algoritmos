package br.edu.ifba.minasaquaticas.encriptacao.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

import br.edu.ifba.minasaquaticas.encriptacao.aleatoriedade.GeradorDeAleatoriedadeReal;
import br.edu.ifba.minasaquaticas.encriptacao.chaves.GeradorDeChaves;
import br.edu.ifba.minasaquaticas.encriptacao.excecoes.FalhaGeracaoDeChaves;

public class GeradorDeChavesImpl
    implements GeradorDeChaves<GeradorDeAleatoriedadeReal> {

    /**
    * Tamanho das chaves RSA.
    */
    private static final int TAMANHO_CHAVES_ENCRIPTACAO = 1024;

    private GeradorDeAleatoriedadeReal geradorDeAleatoriedade = null;

    private String algoritmoDeEncriptacao = null;

    @Override
    public void inicializar(GeradorDeAleatoriedadeReal geradorDeAleatoriedade, String algoritmoDeEncriptacao) {

        this.geradorDeAleatoriedade = geradorDeAleatoriedade;

        this.algoritmoDeEncriptacao = algoritmoDeEncriptacao;
    }

    /**
    * Gera um novo par de chaves
    * utilizando RSA.
    */
    @Override
    public KeyPair gerarChaves() throws FalhaGeracaoDeChaves {

        KeyPair chaves = null;

        try {

            KeyPairGenerator geradorDePares = KeyPairGenerator.getInstance(algoritmoDeEncriptacao);

            geradorDePares.initialize(TAMANHO_CHAVES_ENCRIPTACAO, geradorDeAleatoriedade);

            chaves = geradorDePares.generateKeyPair();

        } catch (NoSuchAlgorithmException e) {

            throw new FalhaGeracaoDeChaves("falha gerando chaves: " + e.getMessage());
        }

        return chaves;
    }

    /**
    * Gera as chaves e grava os arquivos
    * de chave pública e privada.
    */
    @Override
    public KeyPair gerarChaves(String caminhoChavePrivada, String caminhoChavePublica) throws FalhaGeracaoDeChaves {

        KeyPair chaves = gerarChaves();

        byte[] bytes = chaves.getPublic().getEncoded();

        gravar(caminhoChavePublica, bytes);

        bytes = chaves.getPrivate().getEncoded();

        gravar(caminhoChavePrivada, bytes);

        return chaves;
    }

    /**
    * Grava uma chave em arquivo.
    */
    private void gravar(String caminho, byte[] bytes) throws FalhaGeracaoDeChaves {

        File arquivo = new File(caminho);

        if(arquivo.exists()){
            arquivo.delete();
        }

        try (
            FileOutputStream stream = new FileOutputStream(arquivo)) {

            stream.write(bytes);

        } catch (IOException e) {

            throw new FalhaGeracaoDeChaves("erro gravando chaves em arquivo");
        }
    }

    @Override
    public void finalizar()
        throws FalhaGeracaoDeChaves {

        geradorDeAleatoriedade.finalizar();
    }
}