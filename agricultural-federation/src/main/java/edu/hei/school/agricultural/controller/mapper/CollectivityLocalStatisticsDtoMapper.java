package edu.hei.school.agricultural.controller.mapper;

import edu.hei.school.agricultural.controller.dto.CollectivityLocalStatistics;
import edu.hei.school.agricultural.controller.dto.MemberDescription;
import org.springframework.stereotype.Component;

@Component
public class CollectivityLocalStatisticsDtoMapper {

    public CollectivityLocalStatistics mapToDto(edu.hei.school.agricultural.entity.CollectivityLocalStatistics entity) {
        MemberDescription memberDescription = MemberDescription.builder()
                .id(entity.getMemberDescription().getId())
                .firstName(entity.getMemberDescription().getFirstName())
                .lastName(entity.getMemberDescription().getLastName())
                .email(entity.getMemberDescription().getEmail())
                .occupation(entity.getMemberDescription().getOccupation())
                .build();

        return CollectivityLocalStatistics.builder()
                .memberDescription(memberDescription)
                .earnedAmount(entity.getEarnedAmount())
                .unpaidAmount(entity.getUnpaidAmount())
                .build();
    }
}
