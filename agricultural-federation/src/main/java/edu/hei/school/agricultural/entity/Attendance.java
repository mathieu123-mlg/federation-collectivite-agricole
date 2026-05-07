package edu.hei.school.agricultural.entity;

public class Attendance {

    private String id;
    private String activityId;
    private Member member;
    private AttendanceStatus attendanceStatus;

    public Attendance() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String a) {
        this.activityId = a;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member m) {
        this.member = m;
    }

    public AttendanceStatus getAttendanceStatus() {
        return attendanceStatus;
    }

    public void setAttendanceStatus(AttendanceStatus s) {
        this.attendanceStatus = s;
    }
}
