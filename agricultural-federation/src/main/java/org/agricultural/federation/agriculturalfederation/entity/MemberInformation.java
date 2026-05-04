package org.agricultural.federation.agriculturalfederation.entity;

import java.sql.Date;
import java.util.Objects;

public abstract class MemberInformation {
    private String firstName;
    private String lastName;
    private Date birthDate;
    private Gender gender;
    private String address;
    private String profession;
    private String phoneNumber;
    private String email;
    private MemberOccupation occupation;

    public MemberInformation(String firstName, String lastName, Date birthDate, Gender gender, String address, String profession, String phoneNumber, String email, MemberOccupation occupation) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.gender = gender;
        this.address = address;
        this.profession = profession;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.occupation = occupation;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public MemberOccupation getOccupation() {
        return occupation;
    }

    public void setOccupation(MemberOccupation occupation) {
        this.occupation = occupation;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        MemberInformation that = (MemberInformation) o;
        return Objects.equals(firstName, that.firstName) && Objects.equals(lastName, that.lastName) && Objects.equals(birthDate, that.birthDate) && gender == that.gender && Objects.equals(address, that.address) && Objects.equals(profession, that.profession) && Objects.equals(phoneNumber, that.phoneNumber) && Objects.equals(email, that.email) && occupation == that.occupation;
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, birthDate, gender, address, profession, phoneNumber, email, occupation);
    }

    @Override
    public String toString() {
        return "MemberInformation{" +
               "firstName='" + firstName + '\'' +
               ", lastName='" + lastName + '\'' +
               ", birthDate=" + birthDate +
               ", gender=" + gender +
               ", address='" + address + '\'' +
               ", profession='" + profession + '\'' +
               ", phoneNumber='" + phoneNumber + '\'' +
               ", email='" + email + '\'' +
               ", occupation=" + occupation +
               '}';
    }
}
