package org.agricultural.federation.agriculturalfederation.entity;

public class CreateCollectivityStructure {
    private Integer president;
    private Integer vicePresident;
    private Integer treasurer;
    private Integer secretary;

    public CreateCollectivityStructure(Integer president, Integer vicePresident, Integer treasurer, Integer secretary) {
        this.president = president;
        this.vicePresident = vicePresident;
        this.treasurer = treasurer;
        this.secretary = secretary;
    }

    public Integer getPresident() {
        return president;
    }

    public void setPresident(Integer president) {
        this.president = president;
    }

    public Integer getVicePresident() {
        return vicePresident;
    }

    public void setVicePresident(Integer vicePresident) {
        this.vicePresident = vicePresident;
    }

    public Integer getTreasurer() {
        return treasurer;
    }

    public void setTreasurer(Integer treasurer) {
        this.treasurer = treasurer;
    }

    public Integer getSecretary() {
        return secretary;
    }

    public void setSecretary(Integer secretary) {
        this.secretary = secretary;
    }
}
