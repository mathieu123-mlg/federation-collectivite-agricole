package org.agricultural.federation.agriculturalfederation.entity;

import java.time.Instant;
import java.util.List;

public class Collectivity {
    private Integer id;
    private Integer number;
    private String name;
    private String location;
    private String speciality;
    private Instant creationDate;
    private boolean federationApproval;
    private List<Member> members;
    private CollectivityStructure collectivityStructure;

    public Collectivity() {
    }

    public Collectivity(Integer id, Integer number, String name, String location, String speciality, Instant creationDate, boolean federationApproval, List<Member> members, CollectivityStructure collectivityStructure) {
        this.id = id;
        this.number = number;
        this.name = name;
        this.location = location;
        this.speciality = speciality;
        this.creationDate = creationDate;
        this.federationApproval = federationApproval;
        this.members = members;
        this.collectivityStructure = collectivityStructure;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCollectivityStructure(CollectivityStructure collectivityStructure) {
        this.collectivityStructure = collectivityStructure;
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