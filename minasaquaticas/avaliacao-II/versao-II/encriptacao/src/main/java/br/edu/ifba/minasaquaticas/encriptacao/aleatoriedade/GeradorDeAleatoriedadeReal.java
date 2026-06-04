package br.edu.ifba.minasaquaticas.encriptacao.aleatoriedade;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.SecureRandom;

import javax.imageio.ImageIO;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;

import br.edu.ifba.minasaquaticas.encriptacao.excecoes.FalhaGeracaoDeChaves;

/**
 * Fonte de aleatoriedade baseada em vídeo.
 *
 * A cada solicitação de números aleatórios,
 * novos quadros do vídeo são capturados e
 * convertidos em bytes para alimentar o
 * processo de geração das chaves RSA.
 */
public class GeradorDeAleatoriedadeReal
    extends SecureRandom {

    /**
    * Número máximo de tentativas para
    * capturar um quadro válido.
    */
    private static final int TENTATIVAS_CAPTURA_DE_QUADRO = 100;

    private FFmpegFrameGrabber grabber;

    public GeradorDeAleatoriedadeReal(String caminhoVideo) throws FalhaGeracaoDeChaves {

        Loader.load(org.bytedeco.opencv.global.opencv_core.class);

        grabber = new FFmpegFrameGrabber(caminhoVideo);

        try {
            grabber.start();

        } catch (Exception e) {
            throw new FalhaGeracaoDeChaves("falha de inicialização: " + e.getMessage());
        }
    }

    /**
    * Obtém o próximo quadro do vídeo.
    */
    private Frame proximoQuadro() throws FalhaGeracaoDeChaves {

        Frame quadro = null;

        try {
            quadro = grabber.grab();

        } catch (Exception e) {
            throw new FalhaGeracaoDeChaves("falha capturando quadro: " + e.getMessage());
        }

        return quadro;
    }

    /**
    * Converte um quadro em imagem.
    */
    private BufferedImage proximaImagem() throws FalhaGeracaoDeChaves {

        BufferedImage imagem = null;

        Java2DFrameConverter conversor = new Java2DFrameConverter();

        int tentativas = 0;

        do {

            tentativas++;

            Frame quadro = proximoQuadro();

            imagem = conversor.convert(quadro);

        } while (imagem == null && tentativas < TENTATIVAS_CAPTURA_DE_QUADRO);

        conversor.close();

        return imagem;
    }

    /**
    * Gera um inteiro utilizando os
    * bytes extraídos dos quadros.
    */
    @Override
    public int nextInt() {

        int valor = 0;

        int[] aleatoriedade = getAleatoriedade();

        if(aleatoriedade != null && aleatoriedade.length >= 4){
            valor |= aleatoriedade[0] << 24;
            valor |= aleatoriedade[1] << 16;
            valor |= aleatoriedade[2] << 8;
            valor |= aleatoriedade[3];
        }

        return valor;
    }

    /**
    * Gera um long utilizando os
    * bytes extraídos dos quadros.
    */
    @Override
    public long nextLong() {

        long valor = 0;

        int[] aleatoriedade = getAleatoriedade();

        if(aleatoriedade != null && aleatoriedade.length >= 9){

            valor |= (long) aleatoriedade[0] << 56;

            valor |= (long) aleatoriedade[1] << 48;

            valor |= (long) aleatoriedade[2] << 40;

            valor |= (long) aleatoriedade[3] << 32;

            valor |= (long) aleatoriedade[4] << 24;

            valor |= (long) aleatoriedade[5] << 16;

            valor |= (long) aleatoriedade[6] << 8;

            valor |= (long) aleatoriedade[7];
        }

        return valor;
    }

    /**
    * Extrai bytes da imagem para
    * compor a aleatoriedade.
    */
    private int[] getAleatoriedade() {

        int[] aleatoriedade = null;

        try {

            ByteArrayOutputStream stream = new ByteArrayOutputStream();

            ImageIO.write(proximaImagem(), "jpg", stream);

            byte[] bytes = stream.toByteArray();

            aleatoriedade = new int[bytes.length];

            for (int i = 0; i < bytes.length; i++) {

                aleatoriedade[i] = bytes[i] & 0xff;
            }

        } catch (IOException | FalhaGeracaoDeChaves e) {
            e.printStackTrace();
        }

        return aleatoriedade;
    }

    /**
    * Libera os recursos utilizados
    * pelo capturador de vídeo.
    */
    public void finalizar() throws FalhaGeracaoDeChaves {

        try {

            grabber.stop();
            grabber.release();

        } catch (Exception e) {
            throw new FalhaGeracaoDeChaves("falha finalizando: " + e.getMessage());
        }
    }
}