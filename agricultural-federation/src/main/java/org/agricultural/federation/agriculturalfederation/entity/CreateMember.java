package org.agricultural.federation.agriculturalfederation.entity;

import java.sql.Date;
import java.util.List;

public class CreateMember extends MemberInformation {
    private String collectivityIdentifier;
    private List<Referee> referees;
    private boolean registrationFeePaid;
    private boolean membershipDuesPaid;

    public CreateMember(String firstName, String lastName, Date birthDate, Gender gender, String address, String profession, String phoneNumber, String email, MemberOccupation occupation, String collectivityIdentifier, List<Referee> referees, boolean registrationFeePaid, boolean membershipDuesPaid) {
        super(firstName, lastName, birthDate, gender, address, profession, phoneNumber, email, occupation);
        this.collectivityIdentifier = collectivityIdentifier;
        this.referees = referees;
        this.registrationFeePaid = registrationFeePaid;
        this.membershipDuesPaid = membershipDuesPaid;
    }

    public String getCollectivityIdentifier() {
        return collectivityIdentifier;
    }

    public void setCollectivityIdentifier(String collectivityIdentifier) {
        this.collectivityIdentifier = collectivityIdentifier;
    }

    public List<Referee> getReferees() {
        return referees;
    }

    public void setReferees(List<Referee> referees) {
        this.referees = referees;
    }

    public boolean isRegistrationFeePaid() {
        return registrationFeePaid;
    }

    public void setRegistrationFeePaid(boolean registrationFeePaid) {
        this.registrationFeePaid = registrationFeePaid;
    }

    public boolean isMembershipDuesPaid() {
        return membershipDuesPaid;
    }

    public void setMembershipDuesPaid(boolean membershipDuesPaid) {
        this.membershipDuesPaid = membershipDuesPaid;
    }
}
