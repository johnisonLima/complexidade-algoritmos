package br.edu.ifba.minasaquaticas.impl;

public class Mina implements Comparable<Mina> {
    private Integer id = 0;
    private Integer modelo = 0;

    public Mina(Integer id, Integer modelo) {
        this.id = id;
        this.modelo = modelo;
    }


    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getModelo() {
        return modelo;
    }
    public void setModelo(Integer modelo) {
        this.modelo = modelo;
    }

    @Override
    public int compareTo(Mina o) {
        return this.id.compareTo(o.getId());
    }

    @Override
    public String toString() {
        return "Mina [ID=" + id + ", Modelo=" + modelo + "]";
    }
}
