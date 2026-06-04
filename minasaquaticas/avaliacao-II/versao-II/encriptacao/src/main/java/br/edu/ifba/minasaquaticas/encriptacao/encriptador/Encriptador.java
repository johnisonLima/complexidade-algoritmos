package br.edu.ifba.minasaquaticas.encriptacao.encriptador;

import br.edu.ifba.minasaquaticas.encriptacao.excecoes.FalhaEncriptacao;

import java.security.KeyPair;

/**
 * Define operações de
 * criptografia e descriptografia.
 */

public abstract class Encriptador {

    protected KeyPair chaves = null;

    protected String algoritmoDeEncriptacao = null;

    public Encriptador(KeyPair chaves, String algoritmoDeEncriptacao) {

        this.chaves = chaves;

        this.algoritmoDeEncriptacao = algoritmoDeEncriptacao;
    }

    public abstract String encriptar(String dados) throws FalhaEncriptacao;

    public abstract String desencriptar(String encriptacao) throws FalhaEncriptacao;
}