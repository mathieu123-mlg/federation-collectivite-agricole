package edu.hei.school.agricultural.controller.mapper;

import edu.hei.school.agricultural.controller.dto.ActivityMemberAttendanceDto;
import edu.hei.school.agricultural.controller.dto.MemberDescription;
import edu.hei.school.agricultural.entity.Attendance;
import org.springframework.stereotype.Component;

@Component
public class ActivityMemberAttendanceDtoMapper {
    public ActivityMemberAttendanceDto mapToDto(Attendance a) {
        ActivityMemberAttendanceDto dto = new ActivityMemberAttendanceDto();
        dto.setId(a.getId());
        dto.setAttendanceStatus(a.getAttendanceStatus() == null ? null :
                a.getAttendanceStatus().name());

        if (a.getMember() != null) {
            MemberDescription desc = new MemberDescription();
            desc.setId(a.getMember().getId());
            desc.setFirstName(a.getMember().getFirstName());
            desc.setLastName(a.getMember().getLastName());
            desc.setEmail(a.getMember().getEmail());
            desc.setOccupation(a.getMember().getOccupation() == null ? null :
                    a.getMember().getOccupation().name());
            dto.setMemberDescription(desc);
        }
        return dto;
    }
}
