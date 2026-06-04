package br.edu.ifba.minasaquaticas.encriptacao;

import java.security.SecureRandom;

import br.edu.ifba.minasaquaticas.encriptacao.aleatoriedade.GeradorDeAleatoriedadeReal;
import br.edu.ifba.minasaquaticas.encriptacao.chaves.GeradorDeChaves;
import br.edu.ifba.minasaquaticas.encriptacao.impl.GeradorDeChavesImpl;

public class App {

    /**
    * Vídeo utilizado como fonte de aleatoriedade.
    */
    private static final String CAMINHO_VIDEO =
        "E:\\MEGAsync\\projectServ\\www\\projetos_pessoais\\complexidade-algoritmos\\minasaquaticas\\avaliacao-II\\versao-II\\encriptacao\\video\\aranha.mp4";

    /**
    * Algoritmo de criptografia utilizado.     */
    private static final String ALGORITMO_ENCRIPTACAO =
        "RSA";

    /**
    * Arquivo da chave pública.
    */
    private static final String CAMINHO_CHAVE_PUBLICA =
        "E:\\MEGAsync\\projectServ\\www\\projetos_pessoais\\complexidade-algoritmos\\minasaquaticas\\avaliacao-II\\versao-II\\clientes\\chave\\ch_publica.txt";

    /**
    * Arquivo da chave privada.
    */
    private static final String CAMINHO_CHAVE_PRIVADA =
        "E:\\MEGAsync\\projectServ\\www\\projetos_pessoais\\complexidade-algoritmos\\minasaquaticas\\avaliacao-II\\versao-II\\servidor\\chave\\ch_privada.txt";

    /**
    * Quantidade máxima de quadros
    * descartados antes da geração.
    */
    private static final int DESLOCAMENTO_MAXIMO = 100;

    public static void main(String[] args)
        throws Exception {

        GeradorDeAleatoriedadeReal geradorDeAleatoriedade = new GeradorDeAleatoriedadeReal(CAMINHO_VIDEO);

        GeradorDeChaves<GeradorDeAleatoriedadeReal>geradorDeChaves = new GeradorDeChavesImpl();

        geradorDeChaves.inicializar(geradorDeAleatoriedade, ALGORITMO_ENCRIPTACAO);

        /**
        * Escolhe um deslocamento aleatório
        * para iniciar a leitura dos quadros
        * em posições diferentes do vídeo.
        */
        SecureRandom randomizador = new SecureRandom();

        int deslocamento = randomizador.nextInt(DESLOCAMENTO_MAXIMO);

        for (int i = 0; i <= deslocamento; i++) {

            System.out.println("Deslocando quadro " + (i + 1));

            geradorDeAleatoriedade.nextInt();
        }

        /**
        * Geração e gravação das chaves.
        */
        geradorDeChaves.gerarChaves(CAMINHO_CHAVE_PRIVADA, CAMINHO_CHAVE_PUBLICA);

        geradorDeChaves.finalizar();

        System.out.println();
        System.out.println("Chaves geradas com sucesso.");

        System.out.println("Chave pública: " + CAMINHO_CHAVE_PUBLICA);
        
        System.out.println("Chave privada: " + CAMINHO_CHAVE_PRIVADA);
    }
}