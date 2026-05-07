package edu.hei.school.agricultural.repository;

import edu.hei.school.agricultural.entity.CollectivityLocalStatistics;
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

    public CollectivityStatisticsRepository(Connection connection, CollectivityStatisticsMapper collectivityStatisticsMapper) {
        this.connection = connection;
        this.collectivityStatisticsMapper = collectivityStatisticsMapper;
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
}