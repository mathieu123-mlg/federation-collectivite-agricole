package edu.hei.school.agricultural.repository;

import edu.hei.school.agricultural.controller.mapper.AttendanceMapper;
import edu.hei.school.agricultural.entity.Attendance;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class AttendanceRepository {
    private final Connection connection;
    private final AttendanceMapper attendanceMapper;

    public AttendanceRepository(Connection connection, AttendanceMapper attendanceMapper) {
        this.connection = connection;
        this.attendanceMapper = attendanceMapper;
    }

    public boolean alreadyConfirmed(String activityId, String memberId) {
        String sql = """
                select 1 from attendance
                where activity_id = ? and member_id = ?
                and attendance_status != 'UNDEFINED'
                """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, activityId);
            ps.setString(2, memberId);
            return ps.executeQuery().next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Attendance> saveAll(String activityId, List<Attendance> attendances) {
        String upsertSql = """
                insert into attendance (id, activity_id, member_id, attendance_status)
                values (?, ?, ?, ?::attendance_status)
                on conflict (activity_id, member_id)
                do update set attendance_status = excluded.attendance_status
                """;
        try (PreparedStatement ps = connection.prepareStatement(upsertSql)) {
            for (Attendance a : attendances) {
                String id = java.util.UUID.randomUUID().toString();
                a.setId(id);
                ps.setString(1, id);
                ps.setString(2, activityId);
                ps.setString(3, a.getMember().getId());
                ps.setString(4, a.getAttendanceStatus().name());
                ps.addBatch();
            }
            ps.executeBatch();
            return findByActivityId(activityId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Attendance> findByActivityId(String activityId) {
        List<Attendance> list = new ArrayList<>();
        String sql = """
                select id, activity_id, member_id, attendance_status
                from attendance where activity_id = ?
                """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, activityId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(attendanceMapper.mapFromResultSet(rs));
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
