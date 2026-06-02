package br.edu.ifba.nobreaks.servidor.operacoes;

public interface Operacoes<Monitorado, Leitura> {

    public void gravar(Monitorado monitorado, Leitura leitura);

    public void gravar(Monitorado monitorado, int oscilacoes);

    public int detectarAltasOscilacoes();
}
