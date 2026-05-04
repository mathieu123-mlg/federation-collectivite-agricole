package org.agricultural.federation.agriculturalfederation.entity;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Member extends MemberInformation {
    private String id;
    private List<Referee> referees;

    public Member(String firstName, String lastName, Date birthDate, Gender gender, String address, String profession, String phoneNumber, String email, MemberOccupation occupation, String id, List<Referee> referees) {
        super(firstName, lastName, birthDate, gender, address, profession, phoneNumber, email, occupation);
        this.id = id;
        this.referees = referees;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Referee> getReferees() {
        return referees;
    }

    public void setReferees(List<Referee> referees) {
        this.referees = referees;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Member member = (Member) o;
        return Objects.equals(id, member.id) && Objects.equals(referees, member.referees);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, referees);
    }

    @Override
    public String toString() {
        return "Member{" +
               super.toString() +
               "id='" + id + '\'' +
               ", referees=" + referees +
               '}';
    }
}
