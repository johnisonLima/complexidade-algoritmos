package br.edu.ifba.nobreaks.servidor;

import br.edu.ifba.nobreaks.servidor.impl.Leitura;
import br.edu.ifba.nobreaks.servidor.impl.NoBreak;
import br.edu.ifba.nobreaks.servidor.impl.OperacoesImpl;
import br.edu.ifba.nobreaks.servidor.operacoes.Operacoes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("nobreaks")
public class Rotas {

    private static Operacoes<NoBreak, Leitura> operacoes = null;
    private static Operacoes<NoBreak, Leitura> getOperacoes() {
        if (operacoes == null) {
            operacoes = new OperacoesImpl();
        }

        return operacoes;
    }

    private static final String INFORMACOES = "serviço de atendimento a nobreaks, v1.0";

    @GET
    @Path("/")
    public Response getInformacoes() {
        return Response.ok(INFORMACOES, MediaType.TEXT_PLAIN).build();
    }

    @POST
    @Path("{id}/{temperatura}/{carga}")
    public Response gravarLeitura(@PathParam("id") String idNoBreak, @PathParam("temperatura") int temperatura, @PathParam("carga") int cargaBateria) {
        NoBreak noBreak = new NoBreak(idNoBreak, "único");
        Leitura leitura = new Leitura(temperatura, cargaBateria);

        getOperacoes().gravar(noBreak, leitura);

        return Response.ok().build();
    }

    @POST
    @Path("{id}/{oscilacoes}")
    public Response gravarOscilacao(@PathParam("id") String id, @PathParam("oscilacoes") int oscilacoes) {
        NoBreak noBreak = new NoBreak(id, "único");

        getOperacoes().gravar(noBreak, oscilacoes);

        return Response.ok().build();
    }

    @GET
    @Path("oscilacoes")
    public Response detectarOscilacoes() {
        int oscilacoes = getOperacoes().detectarAltasOscilacoes();

        return Response.ok(oscilacoes + "", MediaType.TEXT_PLAIN).build();
    }

}
