package edu.hei.school.agricultural.repository;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import edu.hei.school.agricultural.mapper.ActivityMapper;
import edu.hei.school.agricultural.entity.Activity;
import edu.hei.school.agricultural.entity.MemberOccupation;
import edu.hei.school.agricultural.entity.MonthlyRecurrenceRule;
@Repository
public class ActivityRepository {
private final Connection connection;
    private final ActivityMapper activityMapper;

    public ActivityRepository(Connection connection, ActivityMapper activityMapper) {
        this.connection = connection;
        this.activityMapper = activityMapper;
    }

    public List<Activity> findByCollectivityId(String collectivityId) {
        List<Activity> activities = new ArrayList<>();
        String sql = """
                select id, collectivity_id, label, activity_type,
                       week_ordinal, day_of_week, executive_date,
                       occupations_concerned
                from activity where collectivity_id = ?
                """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, collectivityId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                activities.add(activityMapper.mapFromResultSet(rs));
            }
            return activities;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Activity> findById(String id) {
        String sql = """
                select id, collectivity_id, label, activity_type,
                       week_ordinal, day_of_week, executive_date,
                       occupations_concerned
                from activity where id = ?
                """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(activityMapper.mapFromResultSet(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Activity> saveAll(List<Activity> activities) {
        String sql = """
                insert into activity
                (id, collectivity_id, label, activity_type,
                 week_ordinal, day_of_week, executive_date,
                 occupations_concerned)
                values (?, ?, ?, ?::activity_type, ?, ?, ?, ?)
                """;
        List<Activity> saved = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            for (Activity a : activities) {
                String id = java.util.UUID.randomUUID().toString();
                a.setId(id);
                ps.setString(1, id);
                ps.setString(2, a.getCollectivityId());
                ps.setString(3, a.getLabel());
                ps.setString(4, a.getActivityType() == null ? null : a.getActivityType().name());

                MonthlyRecurrenceRule rule = a.getRecurrenceRule();
                if (rule != null) {
                    ps.setObject(5, rule.getWeekOrdinal());
                    ps.setString(6, rule.getDayOfWeek());
                } else {
                    ps.setNull(5, Types.INTEGER);
                    ps.setNull(6, Types.VARCHAR);
                }

                if (a.getExecutiveDate() != null) {
                    ps.setDate(7, Date.valueOf(a.getExecutiveDate()));
                } else {
                    ps.setNull(7, Types.DATE);
                }

                List<MemberOccupation> occ = a.getMemberOccupationConcerned();
                if (occ != null && !occ.isEmpty()) {
                    ps.setString(8, occ.stream()
                        .map(Enum::name)
                        .collect(Collectors.joining(",")));
                } else {
                    ps.setNull(8, Types.VARCHAR);
                }
                ps.addBatch();
            }
            ps.executeBatch();
            for (Activity a : activities) {
                findById(a.getId()).ifPresent(saved::add);
            }
            return saved;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
