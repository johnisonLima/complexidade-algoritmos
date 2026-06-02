package br.edu.ifba.minasaquaticas;

import java.io.IOException;
import java.net.URI;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

public class Servidor {

    /**
     * Endereço base do servidor.
     */
    private static final String BASE_URI =
        "http://0.0.0.0:8080/";

    /**
     * Inicializa o servidor HTTP.
     */
    public static HttpServer iniciarServidor() {

        ResourceConfig configuracao =
            new ResourceConfig()
                .packages("br.edu.ifba.minasaquaticas");

        return GrizzlyHttpServerFactory
            .createHttpServer(
                URI.create(BASE_URI),
                configuracao
            );
    }

    public static void main(String[] args)
        throws IOException {

        HttpServer servidor =
            iniciarServidor();

        System.out.println();
        System.out.println(
            "Servidor de Minas Aquaticas iniciado."
        );
        System.out.println(
            "URL: " + BASE_URI
        );
        System.out.println();
        System.out.println(
            "Pressione ENTER para encerrar."
        );

        System.in.read();

        servidor.shutdownNow();
    }
}