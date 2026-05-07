package edu.hei.school.agricultural.mapper;

import edu.hei.school.agricultural.entity.CollectivityLocalStatistics;
import edu.hei.school.agricultural.entity.MemberDescription;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class CollectivityStatisticsMapper {

    public CollectivityLocalStatistics mapFromResultSet(ResultSet rs) {
        try {
            MemberDescription memberDescription = MemberDescription.builder()
                    .id(rs.getString("member_id"))
                    .firstName(rs.getString("first_name"))
                    .lastName(rs.getString("last_name"))
                    .email(rs.getString("email"))
                    .occupation(rs.getString("occupation"))
                    .build();

            return CollectivityLocalStatistics.builder()
                    .memberDescription(memberDescription)
                    .earnedAmount(rs.getDouble("earned_amount"))
                    .unpaidAmount(rs.getDouble("unpaid_amount"))
                    .build();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
