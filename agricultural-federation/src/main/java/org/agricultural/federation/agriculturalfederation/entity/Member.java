package org.agricultural.federation.agriculturalfederation.entity;

import java.time.Instant;

public class Member {
    private Integer id;
    private String first_name;
    private String last_name;
    private Instant birth_date;
    private Gender gender;
    private String address;
    private String profession;
    private String phone_number;
    private String email;
    private Instant adhesion_date;

    public Member(Integer id, String first_name, String last_name, Instant birth_date, Gender gender, String address, String profession, String phone_number, String email, Instant adhesion_date) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.birth_date = birth_date;
        this.gender = gender;
        this.address = address;
        this.profession = profession;
        this.phone_number = phone_number;
        this.email = email;
        this.adhesion_date = adhesion_date;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public Instant getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(Instant birth_date) {
        this.birth_date = birth_date;
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

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Instant getAdhesion_date() {
        return adhesion_date;
    }

    public void setAdhesion_date(Instant adhesion_date) {
        this.adhesion_date = adhesion_date;
    }
}
