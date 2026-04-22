package org.agricultural.federation.agriculturalfederation.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;

import org.agricultural.federation.agriculturalfederation.entity.CreateMember;
import org.agricultural.federation.agriculturalfederation.entity.Member;
import org.agricultural.federation.agriculturalfederation.mapper.RowMapper;
import org.springframework.stereotype.Repository;
@Repository
public class MemberRepository {
    private final Connection connection;
    private final RowMapper rowMapper;

    public MemberRepository(Connection connection, RowMapper rowMapper) {
        this.connection = connection;
        this.rowMapper = rowMapper;
    }

    public Member save(CreateMember cm) {
        String sql = """
            INSERT INTO member 
            (first_name, last_name, birth_date, gender, address, 
             profession, phone_number, email, adhesion_date)
            VALUES (?, ?, ?, ?::gender_enum, ?, ?, ?, ?, ?)
            RETURNING id, first_name, last_name, birth_date, gender::text,
                      address, profession, phone_number, email, adhesion_date
            """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, cm.getFirstName());
            ps.setString(2, cm.getLastName());
            ps.setDate(3, java.sql.Date.valueOf(cm.getBirthDate()));
            ps.setString(4, cm.getGender().name());
            ps.setString(5, cm.getAddress());
            ps.setString(6, cm.getProfession());
            ps.setString(7, cm.getPhoneNumber());
            ps.setString(8, cm.getEmail());
            ps.setTimestamp(9, Timestamp.from(Instant.now()));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rowMapper.mapToMember(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save member", e);
        }
        throw new RuntimeException("Member not saved");
    }

    public void saveReferee(Integer candidateId, Integer refereeId,
                            Integer collectivityId, String relationship) {
        String sql = """
            INSERT INTO referee 
            (candidate_id, referee_id, collectivity_id, relationship)
            VALUES (?, ?, ?, ?)
            """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, candidateId);
            ps.setInt(2, refereeId);
            ps.setInt(3, collectivityId);
            ps.setString(4, relationship);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save referee", e);
        }
    }

    public void saveMemberCollectivity(Integer memberId, Integer collectivityId) {
        String sql = """
            INSERT INTO member_collectivity 
            (member_id, collectivity_id, join_date)
            VALUES (?, ?, ?)
            """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, memberId);
            ps.setInt(2, collectivityId);
            ps.setDate(3, java.sql.Date.valueOf(LocalDate.now()));
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to link member to collectivity", e);
        }
    }

    public boolean existsById(Integer id) {
        String sql = "SELECT 1 FROM member WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isSeniorMember(Integer memberId) {
        String sql = """
            SELECT role FROM member_role mr
            JOIN mandate m ON mr.mandate_id = m.id
            WHERE mr.member_id = ?
            AND m.end_date >= CURRENT_DATE
            """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, memberId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String role = rs.getString("role");
                    if (role.equals("SENIOR") || role.equals("PRESIDENT") ||
                        role.equals("VICE_PRESIDENT") || role.equals("TREASURER") ||
                        role.equals("SECRETARY")) {
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public Integer getCollectivityIdOfMember(Integer memberId) {
        String sql = """
            SELECT collectivity_id FROM member_collectivity
            WHERE member_id = ? AND leave_date IS NULL
            """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, memberId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("collectivity_id");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
