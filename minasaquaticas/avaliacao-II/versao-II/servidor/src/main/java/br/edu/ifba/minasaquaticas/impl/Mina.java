package br.edu.ifba.minasaquaticas.impl;

public class Mina implements Comparable<Mina> {

    private Integer id = 0;

    private Integer modelo = 0;

    public Mina(
        Integer id,
        Integer modelo
    ) {

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
    public int compareTo(Mina outraMina) {
        return this.id.compareTo(
            outraMina.getId()
        );
    }

    /**
     * Duas minas são consideradas iguais
     * quando possuem o mesmo identificador.
     */
    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        Mina outraMina = (Mina) obj;

        return this.id.equals(
            outraMina.getId()
        );
    }

    /**
     * Utilizado pelo Map para localizar
     * corretamente a mina armazenada.
     */
    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {

        return "Mina [ID="
            + id
            + ", Modelo="
            + modelo
            + "]";
    }
}