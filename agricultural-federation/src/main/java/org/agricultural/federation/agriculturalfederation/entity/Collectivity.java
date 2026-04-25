package org.agricultural.federation.agriculturalfederation.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.Instant;
import java.util.List;

public class Collectivity {
    private String id;
    private Integer number;
    private String name;
    private String location;
    private String speciality;
//    private boolean federationApproval;
    private List<Member> members;
    private CollectivityStructure collectivityStructure;
    private Instant updatedAt;

    public Collectivity() {
    }

    public Collectivity(String id, Integer number, String name, String location, String speciality, List<Member> members, CollectivityStructure collectivityStructure, Instant updatedAt) {
        this.id = id;
        this.number = number;
        this.name = name;
        this.location = location;
        this.speciality = speciality;
        this.members = members;
        this.collectivityStructure = collectivityStructure;
        this.updatedAt = updatedAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public List<Member> getMembers() {
        return members;
    }

    public void setMembers(List<Member> members) {
        this.members = members;
    }

    public CollectivityStructure getCollectivityStructure() {
        return collectivityStructure;
    }

    public void setCollectivityStructure(CollectivityStructure collectivityStructure) {
        this.collectivityStructure = collectivityStructure;
    }

    @JsonIgnore
    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    /*

    public Collectivity(String id, Integer number, String name, String location, String speciality, Instant creationDate, boolean federationApproval, List<Member> members, CollectivityStructure collectivityStructure) {
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
    }*/
}