package src.br.edu.ifba.encriptacao;

import java.security.SecureRandom;

import src.br.edu.ifba.encriptacao.aleatoriedade.GeradorDeAleatoriedadeReal;
import src.br.edu.ifba.encriptacao.chaves.GeradorDeChaves;
import src.br.edu.ifba.encriptacao.impl.GeradorDeChavesImpl;

public class App {

    private static final String CAMINHO_DO_VIDEO = "/misc/ifba/workspaces/complexidade/09/encriptacao/video/lava.mp4";
    private static final String ALGORITMO_DE_ENCRIPTACAO = "RSA";

    private static final String CAMINHO_CHAVE_PUBLICA = "/misc/ifba/workspaces/complexidade/09/clientes/chave/ch_publica.chv";
    private static final String CAMINHO_CHAVE_PRIVADA = "/misc/ifba/workspaces/complexidade/09/servidor/chave/ch_privada.chv";

    private static final int DESLOCAMENTO_MAXIMO = 100;

    public static void main(String[] args) throws Exception {
        GeradorDeAleatoriedadeReal geradorDeAleatoriedadeReal = new GeradorDeAleatoriedadeReal(CAMINHO_DO_VIDEO);
        GeradorDeChaves<GeradorDeAleatoriedadeReal> geradorDeChaves = new GeradorDeChavesImpl();
        geradorDeChaves.inicializar(geradorDeAleatoriedadeReal, ALGORITMO_DE_ENCRIPTACAO);

        SecureRandom randomizador = new SecureRandom();
        int deslocamento = randomizador.nextInt(DESLOCAMENTO_MAXIMO);

        for (int i = 0; i <= deslocamento; i++) {
            System.out.println("deslocando " + (i + 1) + " frames");

            geradorDeAleatoriedadeReal.nextInt();
        }

        geradorDeChaves.gerarChaves(CAMINHO_CHAVE_PRIVADA, CAMINHO_CHAVE_PUBLICA);
        geradorDeChaves.finalizar();
    }
}
