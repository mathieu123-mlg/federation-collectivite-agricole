package edu.hei.school.agricultural.controller.dto;

public class MemberDescription {

    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String occupation;

    public MemberDescription() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String f) {
        this.firstName = f;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String l) {
        this.lastName = l;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String e) {
        this.email = e;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String o) {
        this.occupation = o;
    }
}
