package src.br.edu.ifba.encriptacao.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

import src.br.edu.ifba.encriptacao.aleatoriedade.GeradorDeAleatoriedadeReal;
import src.br.edu.ifba.encriptacao.chaves.GeradorDeChaves;
import src.br.edu.ifba.encriptacao.excecoes.FalhaGeracaoDeChaves;

public class GeradorDeChavesImpl implements GeradorDeChaves<GeradorDeAleatoriedadeReal> {

    private static int TAMANHO_CHAVES_ENCRIPTACAO = 1024;

    private GeradorDeAleatoriedadeReal geradorDeAleatoriedade = null;
    private String algoritmoDeEncriptacao = null;

    @Override
    public void inicializar(GeradorDeAleatoriedadeReal geradorDeAleatoriedade, String algoritmoDeEncriptacao) {
        this.geradorDeAleatoriedade = geradorDeAleatoriedade;
        this.algoritmoDeEncriptacao = algoritmoDeEncriptacao;
    }

    @Override
    public KeyPair gerarChaves() throws FalhaGeracaoDeChaves {
        KeyPair chaves = null;

        try {
            KeyPairGenerator geradorDePares = KeyPairGenerator.getInstance(algoritmoDeEncriptacao);
            geradorDePares.initialize(TAMANHO_CHAVES_ENCRIPTACAO, geradorDeAleatoriedade);

            chaves = geradorDePares.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new FalhaGeracaoDeChaves("falha gerando chave: " + e.getMessage());
        }

        return chaves;
    }

    @Override
    public KeyPair gerarChaves(String caminhoChavePrivada, String caminhoChavePublica) throws FalhaGeracaoDeChaves {
        KeyPair chaves = gerarChaves();

        byte[] bytes = chaves.getPublic().getEncoded();
        gravar(caminhoChavePublica, bytes);

        bytes = chaves.getPrivate().getEncoded();
        gravar(caminhoChavePrivada, bytes);

        return chaves;
    }

    private void gravar(String caminho, byte[] bytes) throws FalhaGeracaoDeChaves {
        FileOutputStream stream = null;

        File f = new File(caminho);
        if (f.exists()) {
            f.delete();
        }

        try {
            stream = new FileOutputStream(f);
            stream.write(bytes);
            stream.close();
        } catch (IOException e) {
            throw new FalhaGeracaoDeChaves("erro gravando chaves no arquivos");
        }
    }

    @Override
    public void finalizar() throws FalhaGeracaoDeChaves {
        geradorDeAleatoriedade.finalizar();
    }

}
