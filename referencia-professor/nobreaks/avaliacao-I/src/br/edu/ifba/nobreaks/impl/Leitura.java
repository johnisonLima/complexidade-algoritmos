package br.edu.ifba.nobreaks.impl;

public class Leitura {

    Integer temperatura = 0;
    Integer cargaBateria = 0;

    public Leitura(Integer temperatura, Integer cargaBateria) {
        this.temperatura = temperatura;
        this.cargaBateria = cargaBateria;
    }
    

    public Integer getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(Integer temperatura) {
        this.temperatura = temperatura;
    }

    public Integer getCargaBateria() {
        return cargaBateria;
    }

    public void setCargaBateria(Integer cargaBateria) {
        this.cargaBateria = cargaBateria;
    }

    @Override
    public String toString() {
        return "temperatura: " + temperatura + ", cargaBateria: " + cargaBateria;
    }

}
