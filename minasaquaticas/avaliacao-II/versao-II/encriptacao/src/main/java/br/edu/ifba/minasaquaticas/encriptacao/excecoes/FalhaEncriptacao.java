package br.edu.ifba.minasaquaticas.encriptacao.excecoes;

/**
* Exceção lançada quando ocorre
* alguma falha durante o processo
* de criptografia ou descriptografia.
*/
public class FalhaEncriptacao extends Exception {

    public FalhaEncriptacao(String mensagem) {
        super(mensagem);
    }

    public FalhaEncriptacao(Throwable causa) {
        super(causa);
    }
}