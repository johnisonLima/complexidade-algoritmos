package br.edu.ifba.minasaquaticas.encriptacao.excecoes;

/**
* Exceção lançada quando ocorre
* alguma falha durante a geração
* das chaves criptográficas.
*/
public class FalhaGeracaoDeChaves extends Exception {

    public FalhaGeracaoDeChaves(String mensagem) {
        super(mensagem);
    }

    public FalhaGeracaoDeChaves(Throwable causa) {
        super(causa);
    }
}