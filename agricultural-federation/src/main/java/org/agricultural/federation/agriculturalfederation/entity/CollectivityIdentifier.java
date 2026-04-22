package org.agricultural.federation.agriculturalfederation.entity;

public class CollectivityIdentifier {
    private Integer number;
    private String name;

    public CollectivityIdentifier(Integer number, String name) {
        this.number = number;
        this.name = name;
    }

    public CollectivityIdentifier() {
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
