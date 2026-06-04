package br.edu.ifba.minasaquaticas.encriptacao.chaves;

import br.edu.ifba.minasaquaticas.encriptacao.excecoes.FalhaGeracaoDeChaves;

import java.security.KeyPair;
import java.security.SecureRandom;

/**
 * Responsável pela geração
 * de pares de chaves pública
 * e privada.
 */

public interface GeradorDeChaves<GeradorDeAleatoriedade extends SecureRandom> {

    public void inicializar(GeradorDeAleatoriedade geradorDeAleatoriedade, String algoritmoDeEncriptacao);

    public KeyPair gerarChaves() throws FalhaGeracaoDeChaves;

    public KeyPair gerarChaves(String caminhoChavePrivada, String caminhoChavePublica) throws FalhaGeracaoDeChaves;

    public void finalizar() throws FalhaGeracaoDeChaves;
}