package org.agricultural.federation.agriculturalfederation.entity;

import java.util.List;

public class CreateCollectivity {
    private String location;
    private List<Integer> members;
    private boolean federationApproval;
    private CollectivityStructure collectivityStructure;

    public CreateCollectivity(List<Integer> members, String location, boolean federationApproval, CollectivityStructure collectivityStructure) {
        this.members = members;
        this.location = location;
        this.federationApproval = federationApproval;
        this.collectivityStructure = collectivityStructure;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<Integer> getMembers() {
        return members;
    }

    public void setMembers(List<Integer> members) {
        this.members = members;
    }

    public boolean isFederationApproval() {
        return federationApproval;
    }

    public void setFederationApproval(boolean federationApproval) {
        this.federationApproval = federationApproval;
    }

    public CollectivityStructure getStructure() {
        return collectivityStructure;
    }

    public void setStructure(CollectivityStructure collectivityStructure) {
        this.collectivityStructure = collectivityStructure;
    }
}
