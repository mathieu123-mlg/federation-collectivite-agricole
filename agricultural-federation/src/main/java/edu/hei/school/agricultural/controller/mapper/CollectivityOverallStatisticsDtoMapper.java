package edu.hei.school.agricultural.controller.mapper;

import edu.hei.school.agricultural.controller.dto.CollectivityInformation;
import edu.hei.school.agricultural.controller.dto.CollectivityOverallStatistics;
import org.springframework.stereotype.Component;

@Component
public class CollectivityOverallStatisticsDtoMapper {

    public CollectivityOverallStatistics mapToDto(edu.hei.school.agricultural.entity.CollectivityOverallStatistics entity) {

        CollectivityInformation collectivityInformationDto = CollectivityInformation.builder()
                .name(entity.getCollectivityInformation().getName())
                .number(entity.getCollectivityInformation().getNumber())
                .build();

        return CollectivityOverallStatistics.builder()
                .collectivityInformation(collectivityInformationDto)
                .newMembersNumber(entity.getNewMembersNumber())
                .overallMemberCurrentDuePercentage(entity.getOverallMemberCurrentDuePercentage())
                .build();
    }
}