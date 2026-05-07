package edu.hei.school.agricultural.service;

import edu.hei.school.agricultural.entity.Activity;
import edu.hei.school.agricultural.entity.Attendance;
import edu.hei.school.agricultural.entity.Collectivity;
import edu.hei.school.agricultural.entity.MembershipFee;
import edu.hei.school.agricultural.exception.BadRequestException;
import edu.hei.school.agricultural.exception.NotFoundException;
import edu.hei.school.agricultural.repository.ActivityRepository;
import edu.hei.school.agricultural.repository.AttendanceRepository;
import edu.hei.school.agricultural.repository.CollectivityRepository;
import edu.hei.school.agricultural.repository.MembershipFeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static edu.hei.school.agricultural.entity.ActivityStatus.ACTIVE;
import static java.util.UUID.randomUUID;

@Service
@RequiredArgsConstructor
public class CollectivityService {
    private final CollectivityRepository collectivityRepository;
    private final MembershipFeeRepository membershipFeeRepository;
    private final ActivityRepository activityRepository;
    private final AttendanceRepository attendanceRepository;

    public List<Collectivity> createCollectivities(List<Collectivity> collectivities) {
        for (Collectivity collectivity : collectivities) {
            if (!collectivity.hasEnoughMembers()) {
                throw new BadRequestException("Collectivity must have at least 10 members, otherwise actual is " + collectivity.getMembers().size());
            }
            collectivity.setId(randomUUID().toString());
        }
        return collectivityRepository.saveAll(collectivities);
    }

    public Collectivity getCollectivityById(String id) {
        return collectivityRepository.findById(id).orElseThrow(() -> new NotFoundException("Collectivity.id= " + id + " not found"));
    }

    public Collectivity updateInformations(String collectivityId, String actualName, Integer actualNumber) {
        Collectivity collectivity = collectivityRepository.findById(collectivityId)
                .orElseThrow(() -> new NotFoundException("Collectivity.id= " + collectivityId + " not found"));
        if (actualNumber != null && collectivityRepository.isNumberExists(actualNumber)) {
            throw new BadRequestException("Collectivity.number=" + actualNumber + " already exists");
        }
        if (actualName != null && collectivityRepository.isNameExists(actualName)) {
            throw new BadRequestException("Collectivity.name=" + actualName + " already exists");
        }
        collectivity.setName(actualName);
        collectivity.setNumber(actualNumber);
        return collectivityRepository.saveAll(List.of((collectivity))).getFirst();
    }

    public List<MembershipFee> getMembershipFeesByCollectivityIdentifier(String collectivityIdentifier) {
        Collectivity collectivity = collectivityRepository.findById(collectivityIdentifier)
                .orElseThrow(() ->
                        new NotFoundException("Collectivity.id= " + collectivityIdentifier + " not found"));

        return membershipFeeRepository.getMembershipFeesByCollectivityId(collectivity.getId());
    }

    public List<MembershipFee> createMembershipFees(String collectivityIdentifier, List<MembershipFee> membershipFees) {
        Collectivity collectivity = collectivityRepository.findById(collectivityIdentifier)
                .orElseThrow(() ->
                        new NotFoundException("Collectivity.id= " + collectivityIdentifier + " not found"));
        for (MembershipFee membershipFee : membershipFees) {
            membershipFee.setId(randomUUID().toString());
            membershipFee.setStatus(ACTIVE);
            membershipFee.setCollectivityOwner(collectivity);
        }
        return membershipFeeRepository.saveAll(membershipFees);
    }


    public List<Activity> getActivities(String collectivityId) {
        collectivityRepository.findById(collectivityId)
                .orElseThrow(() -> new NotFoundException("Collectivity not found with id: " + collectivityId));
        return activityRepository.findByCollectivityId(collectivityId);
    }

    public List<Activity> createActivities(String collectivityId,
                                           List<Activity> activities) {
        collectivityRepository.findById(collectivityId)
                .orElseThrow(() -> new NotFoundException(
                        "Collectivity.id=" + collectivityId + " not found"));

        for (Activity a : activities) {
            if (a.getRecurrenceRule() != null && a.getExecutiveDate() != null) {
                throw new BadRequestException(
                        "Cannot provide both recurrenceRule and executiveDate.");
            }
            a.setCollectivityId(collectivityId);
        }
        return activityRepository.saveAll(activities);
    }

    public List<Attendance> getAttendance(String collectivityId,
                                          String activityId) {
        collectivityRepository.findById(collectivityId)
                .orElseThrow(() -> new NotFoundException(
                        "Collectivity.id=" + collectivityId + " not found"));
        activityRepository.findById(activityId)
                .orElseThrow(() -> new NotFoundException(
                        "Activity.id=" + activityId + " not found"));
        return attendanceRepository.findByActivityId(activityId);
    }

    public List<Attendance> recordAttendance(String collectivityId,
                                             String activityId,
                                             List<Attendance> attendances) {
        collectivityRepository.findById(collectivityId)
                .orElseThrow(() -> new NotFoundException(
                        "Collectivity.id=" + collectivityId + " not found"));
        activityRepository.findById(activityId)
                .orElseThrow(() -> new NotFoundException(
                        "Activity.id=" + activityId + " not found"));

        for (Attendance a : attendances) {
            if (attendanceRepository.alreadyConfirmed(
                    activityId, a.getMember().getId())) {
                throw new BadRequestException(
                        "Attendance already confirmed for Member.id="
                        + a.getMember().getId()
                        + ". MISSING or ATTENDED status cannot be changed.");
            }
        }
        return attendanceRepository.saveAll(activityId, attendances);
    }
}