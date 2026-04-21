package org.agricultural.federation.agriculturalfederation.entity;

import java.time.Instant;

public class Collectivity {
    private Integer id;
    private String name;
    private String location;
    private final Instant creationDatetime;
    private boolean federationApproval;

    public Collectivity(Integer id, String name, String location, Instant creationDatetime, boolean federationApproval) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.creationDatetime = creationDatetime;
        this.federationApproval = federationApproval;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Instant getCreationDatetime() {
        return creationDatetime;
    }

    public boolean isFederationApproval() {
        return federationApproval;
    }

    public void setFederationApproval(boolean federationApproval) {
        this.federationApproval = federationApproval;
    }
}
