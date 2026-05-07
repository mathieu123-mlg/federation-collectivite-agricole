package edu.hei.school.agricultural.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CollectivityOverallStatistics {
    private CollectivityInformation collectivityInformation;
    private Integer newMembersNumber;
    private Double overallMemberCurrentDuePercentage;
}