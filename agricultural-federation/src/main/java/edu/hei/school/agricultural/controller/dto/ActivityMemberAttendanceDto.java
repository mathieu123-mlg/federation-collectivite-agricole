package edu.hei.school.agricultural.controller.dto;

public class ActivityMemberAttendanceDto {
    private String id;
    private MemberDescription memberDescription;
    private String attendanceStatus;

    public ActivityMemberAttendanceDto() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public MemberDescription getMemberDescription() {
        return memberDescription;
    }

    public void setMemberDescription(MemberDescription memberDescription) {
        this.memberDescription = memberDescription;
    }

    public String getAttendanceStatus() {
        return attendanceStatus;
    }

    public void setAttendanceStatus(String attendanceStatus) {
        this.attendanceStatus = attendanceStatus;
    }
}
