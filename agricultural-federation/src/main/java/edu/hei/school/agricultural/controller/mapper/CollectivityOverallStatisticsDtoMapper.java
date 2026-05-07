package edu.hei.school.agricultural.controller.mapper;

import edu.hei.school.agricultural.controller.dto.CollectivityInformation;
import edu.hei.school.agricultural.controller.dto.CollectivityOverallStatistics;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
@RequiredArgsConstructor
public class CollectivityOverallStatisticsDtoMapper {

    public CollectivityOverallStatistics mapToDto(ResultSet rs) throws SQLException {
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