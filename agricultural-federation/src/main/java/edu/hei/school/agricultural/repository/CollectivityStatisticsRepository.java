package edu.hei.school.agricultural.repository;

import edu.hei.school.agricultural.entity.CollectivityLocalStatistics;
import edu.hei.school.agricultural.entity.CollectivityOverallStatistics;
import edu.hei.school.agricultural.mapper.CollectivityOverallStatisticsMapper;
import edu.hei.school.agricultural.mapper.CollectivityStatisticsMapper;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CollectivityStatisticsRepository {
    private final Connection connection;
    private final CollectivityStatisticsMapper collectivityStatisticsMapper;
    private final CollectivityOverallStatisticsMapper collectivityOverallStatisticsMapper;

    public CollectivityStatisticsRepository(Connection connection, CollectivityStatisticsMapper collectivityStatisticsMapper, CollectivityOverallStatisticsMapper collectivityOverallStatisticsMapper) {
        this.connection = connection;
        this.collectivityStatisticsMapper = collectivityStatisticsMapper;
        this.collectivityOverallStatisticsMapper = collectivityOverallStatisticsMapper;
    }

    public List<CollectivityLocalStatistics> getLocalStatistics(String collectivityId, LocalDate from, LocalDate to) {
        String sql = """
                
                select m.id as member_id, m.first_name, m.last_name, m.email, m.occupation,
                       COALESCE(SUM(case when mp.creation_date BETWEEN ? and ? then mp.amount else 0 end), 0)
                           as earned_amount,
                       GREATEST(0, COALESCE((select SUM(mf.amount)
                                             from membership_fee mf where mf.collectivity_id = ?
                                            and mf.status = 'ACTIVE' and mf.eligible_from <= ?), 0)
                                - COALESCE(SUM(case when mp.creation_date BETWEEN ? and ? then mp.amount else 0 end), 0))
                           as unpaid_amount
                from member m
                join collectivity_member cm on cm.member_id = m.id
                    and cm.collectivity_id = ?
                left join member_payment mp on mp.member_debited_id = m.id
                    and mp.creation_date BETWEEN ? and ?
                group by m.id, m.first_name, m.last_name, m.email, m.occupation
                order by m.last_name, m.first_name
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(from));
            ps.setDate(2, Date.valueOf(to));
            ps.setString(3, collectivityId);
            ps.setDate(4, Date.valueOf(to));
            ps.setDate(5, Date.valueOf(from));
            ps.setDate(6, Date.valueOf(to));
            ps.setString(7, collectivityId);
            ps.setDate(8, Date.valueOf(from));
            ps.setDate(9, Date.valueOf(to));

            List<CollectivityLocalStatistics> results = new ArrayList<>();
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    results.add(collectivityStatisticsMapper.mapFromResultSet(rs));
                }
            }
            return results;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<CollectivityOverallStatistics> getOverallStatistics(LocalDate from, LocalDate to) {
        String sql = """
                select c.name, c.number,
                       count(distinct case when cm.join_date BETWEEN ? and ? then cm.member_id end) as new_members_number,
                       COALESCE(
                           cast(count(distinct case
                               when not exists (
                                   select 1 from membership_fee mf
                                   where mf.collectivity_id = c.id and mf.status = 'ACTIVE'
                                     and mf.eligible_from <= ?
                                     and not exists (
                                         select 1 from member_payment mp
                                         where mp.member_debited_id = cm.member_id
                                           and mp.membership_fee_id = mf.id
                                           and mp.creation_date BETWEEN ? and ?
                                     )
                               ) then cm.member_id end) as float)
                           / nullif(count(distinct cm.member_id), 0) * 100
                       , 0) as overall_member_current_due_percentage
                from collectivity c
                join collectivity_member cm on cm.collectivity_id = c.id
                group by c.id, c.name, c.number
                order by c.number
                """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(from));
            ps.setDate(2, Date.valueOf(to));
            ps.setDate(3, Date.valueOf(to));
            ps.setDate(4, Date.valueOf(from));
            ps.setDate(5, Date.valueOf(to));
            List<CollectivityOverallStatistics> results = new ArrayList<>();
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    results.add(collectivityOverallStatisticsMapper.mapFromResultSet(rs));
                }
            }
            return results;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}