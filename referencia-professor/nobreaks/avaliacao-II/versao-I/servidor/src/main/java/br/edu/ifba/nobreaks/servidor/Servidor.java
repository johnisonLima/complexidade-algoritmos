package br.edu.ifba.nobreaks.servidor;

import java.io.IOException;
import java.net.URI;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

public class Servidor {

    private static final String BASE_URL = "http://0.0.0.0:8080/";

    private static HttpServer iniciarServidor() {
        ResourceConfig configuracao = new ResourceConfig().packages("br.edu.ifba.nobreaks.servidor");

        HttpServer servidor = GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URL), configuracao);

        return servidor;
    }

    public static void main(String[] args) throws IOException {
        HttpServer servidor = iniciarServidor();
        System.out.println("atendendo nobreaks...");
        System.in.read();
        servidor.shutdown();
    }
}