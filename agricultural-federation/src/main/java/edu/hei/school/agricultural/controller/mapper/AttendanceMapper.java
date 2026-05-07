package edu.hei.school.agricultural.controller.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.stereotype.Component;

import edu.hei.school.agricultural.entity.Attendance;
import edu.hei.school.agricultural.entity.AttendanceStatus;
import edu.hei.school.agricultural.repository.MemberRepository;
@Component
public class AttendanceMapper {
    private final MemberRepository memberRepository;

    public AttendanceMapper(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Attendance mapFromResultSet(ResultSet rs) throws SQLException {
        Attendance attendance = new Attendance();
        attendance.setId(rs.getString("id"));
        attendance.setActivityId(rs.getString("activity_id"));
        String memberId = rs.getString("member_id");
        if (memberId != null) {
            memberRepository.findById(memberId)
                .ifPresent(attendance::setMember);
        }
        String status = rs.getString("attendance_status");
        if (status != null) {
            attendance.setAttendanceStatus(AttendanceStatus.valueOf(status));
        }
        return attendance;
    }
}
