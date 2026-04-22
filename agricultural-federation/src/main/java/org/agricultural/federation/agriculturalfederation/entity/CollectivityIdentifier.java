package org.agricultural.federation.agriculturalfederation.entity;

public class CollectivityIdentifier {
    private Integer numero;
    private String name;

    public CollectivityIdentifier(Integer numero, String name) {
        this.numero = numero;
        this.name = name;
    }

    public CollectivityIdentifier() {
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
