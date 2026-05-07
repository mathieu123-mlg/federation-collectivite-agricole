package edu.hei.school.agricultural.mapper;

import edu.hei.school.agricultural.entity.CollectivityInformation;
import edu.hei.school.agricultural.entity.CollectivityOverallStatistics;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class CollectivityOverallStatisticsMapper {

    public CollectivityOverallStatistics mapFromResultSet(ResultSet rs) throws SQLException {
        CollectivityInformation collectivityInformation = CollectivityInformation.builder()
                .name(rs.getString("name"))
                .number(rs.getInt("number"))
                .build();

        return CollectivityOverallStatistics.builder()
                .collectivityInformation(collectivityInformation)
                .newMembersNumber(rs.getInt("new_members_number"))
                .overallMemberCurrentDuePercentage(rs.getDouble("overall_member_current_due_percentage"))
                .build();
    }
}