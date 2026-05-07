package edu.hei.school.agricultural.service;

import java.util.List;

import org.springframework.stereotype.Service;

import edu.hei.school.agricultural.entity.Activity;
import edu.hei.school.agricultural.entity.Attendance;
import edu.hei.school.agricultural.exception.BadRequestException;
import edu.hei.school.agricultural.exception.NotFoundException;
import edu.hei.school.agricultural.repository.ActivityRepository;
import edu.hei.school.agricultural.repository.AttendanceRepository;
import edu.hei.school.agricultural.repository.CollectivityRepository;
import edu.hei.school.agricultural.repository.MemberRepository;

@Service
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final AttendanceRepository attendanceRepository;
    private final CollectivityRepository collectivityRepository;
    private final MemberRepository memberRepository;

    public ActivityService(ActivityRepository activityRepository,
            AttendanceRepository attendanceRepository,
            CollectivityRepository collectivityRepository,
            MemberRepository memberRepository) {
        this.activityRepository = activityRepository;
        this.attendanceRepository = attendanceRepository;
        this.collectivityRepository = collectivityRepository;
        this.memberRepository = memberRepository;
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
