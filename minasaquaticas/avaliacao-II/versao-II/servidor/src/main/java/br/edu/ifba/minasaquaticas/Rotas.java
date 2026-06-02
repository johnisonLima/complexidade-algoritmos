package br.edu.ifba.minasaquaticas;

import java.util.Map;
import java.util.List;

import br.edu.ifba.minasaquaticas.impl.Leitura;
import br.edu.ifba.minasaquaticas.impl.Mina;
import br.edu.ifba.minasaquaticas.impl.OperacoesImpl;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;

@Path("/minas")
public class Rotas {

    /**
     * Estrutura central do servidor.
     */
    private static final OperacoesImpl operacoes =
        new OperacoesImpl();

    @POST
    @Path("/{id}/{modelo}/{profundidade}/{proximidade}")
    public String gravarLeitura(

        @PathParam("id")
        Integer id,

        @PathParam("modelo")
        Integer modelo,

        @PathParam("profundidade")
        Integer profundidade,

        @PathParam("proximidade")
        Integer proximidade
    ) {

        Mina mina =
            new Mina(id, modelo);

        Leitura leitura =
            new Leitura(
                profundidade,
                proximidade
            );

        operacoes.gravar(
            mina,
            leitura
        );

        System.out.println(
            "Leitura recebida -> "
            + mina
            + " "
            + leitura
        );

        return "Leitura registrada com sucesso.";
    }

    /**
     * Registra a quantidade de eventos
     * detectados localmente por uma mina.
     */
    @POST
    @Path("/transito/{id}/{eventos}")
    public String gravarEventos(

        @PathParam("id")
        Integer id,

        @PathParam("eventos")
        Integer eventos
    ) {

        Mina mina =
            new Mina(id, id * 2);

        operacoes.gravar(
            mina,
            eventos
        );

        System.out.println(
            "Eventos recebidos -> "
            + mina
            + " Quantidade: "
            + eventos
        );

        return "Eventos registrados com sucesso.";
    }

    /**
     * Verifica se o servidor está ativo.
     */
    @GET
    @Path("/status")
    public String status() {

        return "Servidor de Minas Aquaticas ativo.";
    }

    /**
     * Quantidade de minas e leituras recebidas.
     */
    @GET
    @Path("/quantidade")
    public String quantidade() {

        return
            "Minas monitoradas: "
            + operacoes.quantidadeMinas()
            + "\nLeituras recebidas: "
            + operacoes.quantidadeLeituras()
            + "\nEventos detectados: "
            + operacoes.quantidadeEventos();
    }

    /**
     * Retorna a quantidade total de eventos
     * detectados pelas minas.
     * A COMPLEXIDADE FOI PARA O CLIENTE, POIS O SERVIDOR APENAS RETORNA UM VALOR AGREGADO.
     */
    @GET
    @Path("/transito")
    public String detectarTransito() {

        return
            "Eventos de transito detectados: "
            + operacoes.quantidadeEventos();
    }
}