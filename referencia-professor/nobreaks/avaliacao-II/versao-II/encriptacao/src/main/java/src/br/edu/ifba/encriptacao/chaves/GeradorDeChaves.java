package src.br.edu.ifba.encriptacao.chaves;

import java.security.KeyPair;
import java.security.SecureRandom;

import src.br.edu.ifba.encriptacao.excecoes.FalhaGeracaoDeChaves;

public interface GeradorDeChaves<GeradorDeAleatoriedade extends SecureRandom> {
    
    public void inicializar(GeradorDeAleatoriedade geradorDeAleatoriedade, String algoritmoDeEncriptacao);

    public KeyPair gerarChaves()  throws FalhaGeracaoDeChaves;

    public KeyPair gerarChaves(String caminhoChavePrivada, String caminhoChavePublica)  throws FalhaGeracaoDeChaves;

    public void finalizar() throws FalhaGeracaoDeChaves;

}
