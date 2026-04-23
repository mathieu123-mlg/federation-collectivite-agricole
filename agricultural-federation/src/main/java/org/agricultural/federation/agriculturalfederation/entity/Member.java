package org.agricultural.federation.agriculturalfederation.entity;

import java.time.Instant;

public class Member {
    private Integer id;
    private String firstName;
    private String lastName;
    private Instant birthDate;
    private Gender gender;
    private String address;
    private String profession;
    private String phoneNumber;
    private String email;
    private Instant adhesionDate;

    

    public Member(Integer id, String firstName, String lastName, Instant birthDate, Gender gender, String address,
            String profession, String phoneNumber, String email, Instant adhesionDate) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.gender = gender;
        this.address = address;
        this.profession = profession;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.adhesionDate = adhesionDate;
    }

    public Member() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Instant getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Instant birthDate) {
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

    public Instant getAdhesionDate() {
        return adhesionDate;
    }

    public void setAdhesionDate(Instant adhesionDate) {
        this.adhesionDate = adhesionDate;
    }


}
