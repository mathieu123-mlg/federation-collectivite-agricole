package org.agricultural.federation.agriculturalfederation.entity;

import java.util.Objects;

public class Referee {
    private String memberId;
    private String relationship;

    public Referee(String memberId, String relationship) {
        this.memberId = memberId;
        this.relationship = relationship;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Referee referee = (Referee) o;
        return Objects.equals(memberId, referee.memberId) && Objects.equals(relationship, referee.relationship);
    }

    @Override
    public int hashCode() {
        return Objects.hash(memberId, relationship);
    }
}
