package edu.hei.school.agricultural.controller;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.hei.school.agricultural.controller.dto.ActivityMemberAttendanceDto;
import edu.hei.school.agricultural.controller.dto.CreateActivityMemberAttendanceDto;
import edu.hei.school.agricultural.controller.dto.CreateCollectivityActivityDto;
import edu.hei.school.agricultural.controller.dto.MemberDescription;
import edu.hei.school.agricultural.controller.dto.MonthlyRecurrenceRuleDto;
import edu.hei.school.agricultural.entity.Activity;
import edu.hei.school.agricultural.entity.ActivityType;
import edu.hei.school.agricultural.entity.Attendance;
import edu.hei.school.agricultural.entity.AttendanceStatus;
import edu.hei.school.agricultural.entity.MemberOccupation;
import edu.hei.school.agricultural.entity.MonthlyRecurrenceRule;
import edu.hei.school.agricultural.exception.BadRequestException;
import edu.hei.school.agricultural.exception.NotFoundException;
import edu.hei.school.agricultural.repository.MemberRepository;
import edu.hei.school.agricultural.service.ActivityService;

@RestController
public class ActivityController {

    private final ActivityService activityService;
    private final MemberRepository memberRepository;

    public ActivityController(ActivityService activityService,
            MemberRepository memberRepository) {
        this.activityService = activityService;
        this.memberRepository = memberRepository;
    }

    @GetMapping("/collectivities/{id}/activities")
    public ResponseEntity<?> getActivities(@PathVariable String id) {
        try {
            List<CreateCollectivityActivityDto> dtos = new ArrayList<>();
            for (Activity a : activityService.getActivities(id)) {
                dtos.add(toDto(a));
            }
            return ResponseEntity.status(OK).body(dtos);
        } catch (NotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }


    @PostMapping("/collectivities/{id}/activities")
    public ResponseEntity<?> createActivities(
            @PathVariable String id,
            @RequestBody List<CreateCollectivityActivityDto> dtos) {
        try {
            List<Activity> activities = new ArrayList<>();
            for (CreateCollectivityActivityDto dto : dtos) {
                Activity a = new Activity();
                a.setLabel(dto.getLabel());
                if (dto.getActivityType() != null) {
                    a.setActivityType(
                        ActivityType.valueOf(dto.getActivityType()));
                }
                if (dto.getExecutiveDate() != null) {
                    a.setExecutiveDate(dto.getExecutiveDate());
                }
                if (dto.getRecurrenceRule() != null) {
                    MonthlyRecurrenceRule rule = new MonthlyRecurrenceRule();
                    rule.setWeekOrdinal(
                        dto.getRecurrenceRule().getWeekOrdinal());
                    rule.setDayOfWeek(
                        dto.getRecurrenceRule().getDayOfWeek());
                    a.setRecurrenceRule(rule);
                }
                if (dto.getMemberOccupationConcerned() != null) {
                    List<MemberOccupation> occupations = new ArrayList<>();
                    for (String occ : dto.getMemberOccupationConcerned()) {
                        occupations.add(MemberOccupation.valueOf(occ));
                    }
                    a.setMemberOccupationConcerned(occupations);
                }
                activities.add(a);
            }
            List<CreateCollectivityActivityDto> result = new ArrayList<>();
            for (Activity a : activityService.createActivities(id, activities)) {
                result.add(toDto(a));
            }
            return ResponseEntity.status(OK).body(result);
        } catch (NotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(e.getMessage());
        } catch (BadRequestException e) {
            return ResponseEntity.status(BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                .body(e.getMessage());
        }
    }
    @GetMapping("/collectivities/{id}/activities/{activityId}/attendance")
    public ResponseEntity<?> getAttendance(
            @PathVariable String id,
            @PathVariable String activityId) {
        try {
            List<ActivityMemberAttendanceDto> dtos = new ArrayList<>();
            for (Attendance a : activityService
                    .getAttendance(id, activityId)) {
                dtos.add(toAttendanceDto(a));
            }
            return ResponseEntity.status(OK).body(dtos);
        } catch (NotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                .body(e.getMessage());
        }
    }


    @PostMapping("/collectivities/{id}/activities/{activityId}/attendance")
    public ResponseEntity<?> recordAttendance(
            @PathVariable String id,
            @PathVariable String activityId,
            @RequestBody List<CreateActivityMemberAttendanceDto> dtos) {
        try {
            List<Attendance> attendances = new ArrayList<>();
            for (CreateActivityMemberAttendanceDto dto : dtos) {
                var member = memberRepository
                    .findById(dto.getMemberIdentifier())
                    .orElseThrow(() -> new NotFoundException(
                        "Member.id=" + dto.getMemberIdentifier()
                        + " not found"));
                Attendance a = new Attendance();
                a.setMember(member);
                a.setAttendanceStatus(
                    AttendanceStatus.valueOf(dto.getAttendanceStatus()));
                attendances.add(a);
            }
            List<ActivityMemberAttendanceDto> result = new ArrayList<>();
            for (Attendance a : activityService
                    .recordAttendance(id, activityId, attendances)) {
                result.add(toAttendanceDto(a));
            }
            return ResponseEntity.status(CREATED).body(result);
        } catch (BadRequestException e) {
            return ResponseEntity.status(BAD_REQUEST).body(e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                .body(e.getMessage());
        }
    }
    // HELPERS : 

    private CreateCollectivityActivityDto toDto(Activity a) {
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

    private ActivityMemberAttendanceDto toAttendanceDto(Attendance a) {
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
