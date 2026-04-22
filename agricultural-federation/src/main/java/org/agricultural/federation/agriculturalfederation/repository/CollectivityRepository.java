package org.agricultural.federation.agriculturalfederation.repository;

import org.agricultural.federation.agriculturalfederation.entity.Collectivity;
import org.agricultural.federation.agriculturalfederation.entity.CollectivityIdentifier;
import org.agricultural.federation.agriculturalfederation.entity.CollectivityStructure;
import org.agricultural.federation.agriculturalfederation.entity.Member;
import org.agricultural.federation.agriculturalfederation.exception.NotFoundException;
import org.agricultural.federation.agriculturalfederation.mapper.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class CollectivityRepository {
    private final Connection connection;
    private final RowMapper rowMapper;

    public CollectivityRepository(Connection connection, RowMapper rowMapper) {
        this.connection = connection;
        this.rowMapper = rowMapper;
    }

    public Optional<Collectivity> save(Collectivity collectivity) {
        String sql = """
                INSERT INTO collectivity (location, speciality, creation_datetime, federation_approval)
                VALUES (?, ?, ?, ?, ?) RETURNING id""";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, collectivity.getLocation());
            ps.setString(2, collectivity.getSpeciality());
            ps.setTimestamp(3, Timestamp.from(collectivity.getCreationDate()));
            ps.setBoolean(4, collectivity.isFederationApproval());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    collectivity.setId(rs.getInt("id"));
                    return Optional.of(collectivity);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save collectivity", e);
        }
        return Optional.empty();
    }

    public void saveMemberCollectivity(Integer collectivityId, Integer memberId) {
        String sql = "INSERT INTO member_collectivity (member_id, collectivity_id, join_date) VALUES (?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, memberId);
            ps.setInt(2, collectivityId);
            ps.setTimestamp(3, Timestamp.from(Instant.now()));
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to link member to collectivity", e);
        }
    }

    public Integer saveMandate(Integer collectivityId, Instant startDate, Instant endDate) {
        String sql = "INSERT INTO mandate (collectivity_id, start_date, end_date) VALUES (?, ?, ?) RETURNING id";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, collectivityId);
            ps.setTimestamp(2, Timestamp.from(startDate));
            ps.setTimestamp(3, Timestamp.from(endDate));
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt("id") : null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save mandate", e);
        }
    }

    public void saveMemberRole(Integer memberId, Integer mandateId, String role) {
        String sql = "INSERT INTO member_role (member_id, mandate_id, role) VALUES (?, ?, ?::role_enum)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, memberId);
            ps.setInt(2, mandateId);
            ps.setString(3, role);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save member role", e);
        }
    }

    public boolean existsByName(String name) {
        String sql = "SELECT 1 FROM collectivity WHERE name = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to check collectivity name", e);
        }
    }

    public boolean existsMemberById(Integer memberId) {
        String sql = "SELECT 1 FROM member WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, memberId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to check member existence", e);
        }
    }

    public Optional<LocalDate> findMemberAdhesionDate(Integer memberId) {
        String sql = "SELECT adhesion_date FROM member WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, memberId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(rs.getDate("adhesion_date").toLocalDate()) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch member adhesion date", e);
        }
    }

    public Optional<Collectivity> findByIdWithDetails(Integer id) {
        String sql = """
                SELECT id, name, location, speciality, creation_datetime, federation_approval
                FROM collectivity WHERE id = ?""";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Collectivity c = rowMapper.mapToCollectivity(rs);
                    c.setMembers(findMembersByCollectivityId(id));
                    c.setStructure(findStructureByCollectivityId(id));
                    return Optional.of(c);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch collectivity", e);
        }
        return Optional.empty();
    }

    private List<Member> findMembersByCollectivityId(Integer collectivityId) {
        String sql = """
                    SELECT m.id, m.first_name, m.last_name, m.birth_date, m.gender::text,
                           m.address, m.profession, m.phone_number, m.email, m.adhesion_date
                    FROM member m JOIN member_collectivity mc ON m.id = mc.member_id
                    WHERE mc.collectivity_id = ? AND mc.leave_date IS NULL
                """;
        List<Member> members = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, collectivityId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) members.add(rowMapper.mapToMember(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch members", e);
        }
        return members;
    }

    private CollectivityStructure findStructureByCollectivityId(Integer collectivityId) {
        String sql = """
                    SELECT mr.role, m.id, m.first_name, m.last_name, m.birth_date, m.gender::text,
                           m.address, m.profession, m.phone_number, m.email, m.adhesion_date
                    FROM mandate ma JOIN member_role mr ON ma.id = mr.mandate_id
                    JOIN member m ON mr.member_id = m.id
                    WHERE ma.collectivity_id = ? AND ma.end_date >= CURRENT_DATE
                """;
        CollectivityStructure collectivityStructure = new CollectivityStructure();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, collectivityId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Member member = rowMapper.mapToMember(rs);
                    switch (rs.getString("role")) {
                        case "PRESIDENT" -> collectivityStructure.setPresident(member.getId());
                        case "VICE_PRESIDENT" -> collectivityStructure.setVicePresident(member.getId());
                        case "TREASURER" -> collectivityStructure.setTreasurer(member.getId());
                        case "SECRETARY" -> collectivityStructure.setSecretary(member.getId());
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch structure", e);
        }
        return collectivityStructure;
    }

    public Optional<Collectivity> findCollectivityById(Integer id) {
        String sql = """
                    SELECT id, number, name, location, speciality, creation_datetime, federation_approval
                    FROM collectivity
                    WHERE id = ?
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Collectivity collectivity = rowMapper.mapToCollectivity(rs);
                    return Optional.of(collectivity);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch collectivity by id", e);
        }

        return Optional.empty();
    }

    public void assignCollectivityIdentifier(Integer collectivityId, Integer number, String name) {
        String sql = """
                update collectivity set number = ?, name = ?, updated_at = current_timestamp
                where id = ? and updated_at is null""";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, number);
            ps.setString(2, name);
            ps.setInt(3, collectivityId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public boolean existsById(Integer id) {
        String sql = "SELECT 1 FROM membership_fee WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}