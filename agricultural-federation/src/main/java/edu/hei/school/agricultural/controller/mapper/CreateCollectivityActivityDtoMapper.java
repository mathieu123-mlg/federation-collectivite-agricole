package edu.hei.school.agricultural.controller.mapper;

import edu.hei.school.agricultural.controller.dto.CreateCollectivityActivityDto;
import edu.hei.school.agricultural.controller.dto.MonthlyRecurrenceRuleDto;
import edu.hei.school.agricultural.entity.Activity;
import edu.hei.school.agricultural.entity.MemberOccupation;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CreateCollectivityActivityDtoMapper {

    public CreateCollectivityActivityDto mapTo(Activity a) {
        CreateCollectivityActivityDto dto = new CreateCollectivityActivityDto();
        dto.setId(a.getId());
        dto.setLabel(a.getLabel());
        dto.setActivityType(a.getActivityType() == null ? null :
                a.getActivityType().name());
        dto.setExecutiveDate(a.getExecutiveDate());

        if (a.getRecurrenceRule() != null) {
            MonthlyRecurrenceRuleDto rDto = new MonthlyRecurrenceRuleDto();
            rDto.setWeekOrdinal(a.getRecurrenceRule().getWeekOrdinal());
            rDto.setDayOfWeek(a.getRecurrenceRule().getDayOfWeek());
            dto.setRecurrenceRule(rDto);
        }

        if (a.getMemberOccupationConcerned() != null) {
            List<String> occs = new ArrayList<>();
            for (MemberOccupation occ : a.getMemberOccupationConcerned()) {
                occs.add(occ.name());
            }
            dto.setMemberOccupationConcerned(occs);
        }
        return dto;
    }
}
