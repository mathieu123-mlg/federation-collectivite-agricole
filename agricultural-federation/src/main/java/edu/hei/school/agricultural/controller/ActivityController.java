package edu.hei.school.agricultural.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.hei.school.agricultural.entity.Activity;
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
            List<CollectivityActivityDto> dtos = new ArrayList<>();
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
