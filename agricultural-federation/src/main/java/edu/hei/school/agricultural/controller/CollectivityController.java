package edu.hei.school.agricultural.controller;

import edu.hei.school.agricultural.controller.dto.ActivityMemberAttendanceDto;
import edu.hei.school.agricultural.controller.dto.CollectivityInformation;
import edu.hei.school.agricultural.controller.dto.CreateActivityMemberAttendanceDto;
import edu.hei.school.agricultural.controller.dto.CreateCollectivity;
import edu.hei.school.agricultural.controller.dto.CreateCollectivityActivityDto;
import edu.hei.school.agricultural.controller.dto.CreateMembershipFee;
import edu.hei.school.agricultural.controller.mapper.ActivityMemberAttendanceDtoMapper;
import edu.hei.school.agricultural.controller.mapper.CollectivityDtoMapper;
import edu.hei.school.agricultural.controller.mapper.CollectivityLocalStatisticsDtoMapper;
import edu.hei.school.agricultural.controller.mapper.CollectivityOverallStatisticsDtoMapper;
import edu.hei.school.agricultural.controller.mapper.CreateCollectivityActivityDtoMapper;
import edu.hei.school.agricultural.controller.mapper.FinancialAccountDtoMapper;
import edu.hei.school.agricultural.controller.mapper.MembershipFeeDtoMapper;
import edu.hei.school.agricultural.controller.mapper.TransactionDtoMapper;
import edu.hei.school.agricultural.entity.Activity;
import edu.hei.school.agricultural.entity.ActivityType;
import edu.hei.school.agricultural.entity.Attendance;
import edu.hei.school.agricultural.entity.AttendanceStatus;
import edu.hei.school.agricultural.entity.Collectivity;
import edu.hei.school.agricultural.entity.MemberOccupation;
import edu.hei.school.agricultural.entity.MembershipFee;
import edu.hei.school.agricultural.entity.MonthlyRecurrenceRule;
import edu.hei.school.agricultural.exception.BadRequestException;
import edu.hei.school.agricultural.exception.NotFoundException;
import edu.hei.school.agricultural.repository.MemberRepository;
import edu.hei.school.agricultural.service.CollectivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
public class CollectivityController {
    private final CollectivityDtoMapper collectivityDtoMapper;
    private final MembershipFeeDtoMapper membershipFeeDtoMapper;
    private final CollectivityService collectivityService;
    private final FinancialAccountDtoMapper financialAccountDtoMapper;
    private final TransactionDtoMapper transactionDtoMapper;
    private final CollectivityLocalStatisticsDtoMapper collectivityLocalStatisticsDtoMapper;
    private final CollectivityOverallStatisticsDtoMapper collectivityOverallStatisticsDtoMapper;
    private final MemberRepository memberRepository;
    private final ActivityMemberAttendanceDtoMapper activityMemberAttendanceDtoMapper;
    private final CreateCollectivityActivityDtoMapper createCollectivityActivityDtoMapper;

    @GetMapping("/collectivities/{id}")
    public ResponseEntity<?> getCollectivityById(@PathVariable String id) {
        try {
            return ResponseEntity.status(OK)
                    .body(collectivityDtoMapper.mapToDto(collectivityService.getCollectivityById(id)));
        } catch (BadRequestException e) {
            return ResponseEntity.status(BAD_REQUEST)
                    .body(e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }

    }

    @PostMapping("/collectivities")
    public ResponseEntity<?> createCollectivity(@RequestBody List<CreateCollectivity> createCollectivities) {
        try {
            List<Collectivity> collectivities = createCollectivities.stream()
                    .map(collectivityDtoMapper::mapToEntity)
                    .toList();
            return ResponseEntity.status(OK)
                    .body(collectivityService.createCollectivities(collectivities).stream()
                            .map(collectivityDtoMapper::mapToDto)
                            .toList());
        } catch (
                BadRequestException e) {
            return ResponseEntity.status(BAD_REQUEST)
                    .body(e.getMessage());
        } catch (
                NotFoundException e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }

    @PutMapping("/collectivities/{id}/informations")
    public ResponseEntity<?> updateCollectivityInformation(@PathVariable String id,
                                                           @RequestBody CollectivityInformation collectivityInformation) {
        String name = collectivityInformation.getName();
        Integer number = collectivityInformation.getNumber();
        try {
            return ResponseEntity.status(OK)
                    .body(collectivityDtoMapper.mapToDto(collectivityService.updateInformations(id, name, number)));
        } catch (BadRequestException e) {
            return ResponseEntity.status(BAD_REQUEST)
                    .body(e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }

    @GetMapping("/collectivities/{id}/membershipFees")
    public ResponseEntity<?> getCollectivityMembershipFeesByCollectivity(@PathVariable String id) {
        try {
            return ResponseEntity.status(OK)
                    .body(collectivityService.getMembershipFeesByCollectivityIdentifier(id).stream()
                            .map(membershipFeeDtoMapper::mapToDto)
                            .toList());
        } catch (BadRequestException e) {
            return ResponseEntity.status(BAD_REQUEST)
                    .body(e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }

    @PostMapping("/collectivities/{id}/membershipFees")
    public ResponseEntity<?> createCollectivityMembershipFee(
            @PathVariable String id,
            @RequestBody List<CreateMembershipFee> membershipFees) {
        try {
            List<MembershipFee> membershipFeesToCreate = membershipFees.stream()
                    .map(membershipFeeDtoMapper::mapToEntity)
                    .toList();
            return ResponseEntity.status(OK)
                    .body(collectivityService.createMembershipFees(id, membershipFeesToCreate).stream()
                            .map(membershipFeeDtoMapper::mapToDto)
                            .toList());
        } catch (BadRequestException e) {
            return ResponseEntity.status(BAD_REQUEST)
                    .body(e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }

    @GetMapping("/collectivities/{id}/financialAccounts")
    public ResponseEntity<?> getCollectivityFinancialAccounts(@PathVariable String id,
                                                              @RequestParam(required = false) LocalDate at) {
        try {
            return ResponseEntity.status(OK)
                    .body(collectivityService.getFinancialAccounts(id).stream()
                            .map(financialAccount -> financialAccountDtoMapper.mapToDto(financialAccount, at))
                            .toList());
        } catch (BadRequestException e) {
            return ResponseEntity.status(BAD_REQUEST)
                    .body(e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }

    @GetMapping("/collectivities/{id}/transactions")
    public ResponseEntity<?> getCollectivityTransactions(@PathVariable String id, @RequestParam LocalDate from, @RequestParam LocalDate to) {
        try {
            return ResponseEntity.status(OK)
                    .body(collectivityService.getTransactionsByCollectivity(id, from, to).stream()
                            .map(transactionDtoMapper::mapToDto)
                            .toList());
        } catch (BadRequestException e) {
            return ResponseEntity.status(BAD_REQUEST)
                    .body(e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }

    @GetMapping("/collectivities/{id}/statistics")
    public ResponseEntity<?> getCollectivityStatisticsById(@PathVariable String id, @RequestParam LocalDate from, @RequestParam LocalDate to) {
        try {
            return ResponseEntity.status(OK)
                    .body(collectivityService.getOverallStatistics(id, from, to).stream()
                            .map(collectivityLocalStatisticsDtoMapper::mapToDto)
                            .toList());
        } catch (BadRequestException e) {
            return ResponseEntity.status(BAD_REQUEST)
                    .body(e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }

    @GetMapping("/collectivities/statistics")
    public ResponseEntity<?> getCollectivitiesOverallStatistics(
            @RequestParam LocalDate from,
            @RequestParam LocalDate to) {
        try {
            return ResponseEntity.status(OK)
                    .body(collectivityService.getOverallStatistics(from, to).stream()
                            .map(collectivityOverallStatisticsDtoMapper::mapToDto)
                            .toList());
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }

    @GetMapping("/collectivities/{id}/activities")
    public ResponseEntity<?> getActivities(@PathVariable String id) {
        try {
            List<CreateCollectivityActivityDto> dtos = new ArrayList<>();
            for (Activity a : collectivityService.getActivities(id)) {
                dtos.add(createCollectivityActivityDtoMapper.mapTo(a));
            }
            return ResponseEntity.status(OK)
                    .body(dtos);
        } catch (NotFoundException e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(e.getMessage());
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
            for (Activity a : collectivityService.createActivities(id, activities)) {
                result.add(createCollectivityActivityDtoMapper.mapTo(a));
            }
            return ResponseEntity.status(OK)
                    .body(result);
        } catch (NotFoundException e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(e.getMessage());
        } catch (BadRequestException e) {
            return ResponseEntity.status(BAD_REQUEST)
                    .body(e.getMessage());
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
            for (Attendance a : collectivityService.getAttendance(id, activityId)) {
                dtos.add(activityMemberAttendanceDtoMapper.mapToDto(a));
            }
            return ResponseEntity.status(OK)
                    .body(dtos);
        } catch (NotFoundException e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(e.getMessage());
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
                var member = memberRepository.findById(dto.getMemberIdentifier())
                        .orElseThrow(() -> new NotFoundException("Member.id=" + dto.getMemberIdentifier() + " not found"));
                Attendance a = new Attendance();
                a.setMember(member);
                a.setAttendanceStatus(
                        AttendanceStatus.valueOf(dto.getAttendanceStatus()));
                attendances.add(a);
            }
            List<ActivityMemberAttendanceDto> result = new ArrayList<>();
            for (Attendance a : collectivityService
                    .recordAttendance(id, activityId, attendances)) {
                result.add(activityMemberAttendanceDtoMapper.mapToDto(a));
            }
            return ResponseEntity.status(CREATED)
                    .body(result);
        } catch (BadRequestException e) {
            return ResponseEntity.status(BAD_REQUEST)
                    .body(e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }
}
