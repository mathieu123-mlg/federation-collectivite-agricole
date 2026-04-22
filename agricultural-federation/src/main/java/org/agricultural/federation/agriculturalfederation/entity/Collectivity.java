package org.agricultural.federation.agriculturalfederation.entity;

import java.time.Instant;
import java.util.List;

public class Collectivity {
    private Integer id;
    private CollectivityIdentifier identifier;
    private String location;
    private String speciality;
    private Instant creationDate;
    private boolean federationApproval;
    private List<Member> members;
    private CollectivityStructure collectivityStructure;

    public Collectivity() {
    }

    public Collectivity(Integer id, CollectivityIdentifier identifier, String location, Instant creationInstant, boolean federationApproval) {
        this.id = id;
        this.identifier = identifier;
        this.location = location;
        this.creationDate = creationInstant;
        this.federationApproval = federationApproval;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public CollectivityIdentifier getIdentifier() {
        return identifier;
    }

    public void setIdentifier(CollectivityIdentifier identifier) {
        this.identifier = identifier;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public Instant getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public boolean isFederationApproval() {
        return federationApproval;
    }

    public void setFederationApproval(boolean federationApproval) {
        this.federationApproval = federationApproval;
    }

    public List<Member> getMembers() {
        return members;
    }

    public void setMembers(List<Member> members) {
        this.members = members;
    }

    public CollectivityStructure getStructure() {
        return collectivityStructure;
    }

    public void setStructure(CollectivityStructure collectivityStructure) {
        this.collectivityStructure = collectivityStructure;
    }
}