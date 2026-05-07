package edu.hei.school.agricultural.controller.dto;

public class AttendanceDto {

    private Member member;
    private boolean present;
    private boolean justified;

    public AttendanceDto() {
    }

    public AttendanceDto(Member member, boolean present, boolean justified) {
        this.member = member;
        this.present = present;
        this.justified = justified;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public boolean isPresent() {
        return present;
    }

    public void setPresent(boolean present) {
        this.present = present;
    }

    public boolean isJustified() {
        return justified;
    }

    public void setJustified(boolean justified) {
        this.justified = justified;
    }

}
