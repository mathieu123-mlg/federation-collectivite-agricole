package edu.hei.school.agricultural.controller.dto;

public class CreateActivityMemberAttendanceDto {
    private String memberIdentifier;
    private String attendanceStatus;

    public CreateActivityMemberAttendanceDto() {}

    public String getMemberIdentifier() {
        return memberIdentifier;
    }

    public void setMemberIdentifier(String memberIdentifier) {
        this.memberIdentifier = memberIdentifier;
    }

    public String getAttendanceStatus() {
        return attendanceStatus;
    }

    public void setAttendanceStatus(String attendanceStatus) {
        this.attendanceStatus = attendanceStatus;
    }
}
