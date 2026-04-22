package org.agricultural.federation.agriculturalfederation.controller;

import java.util.List;

import org.agricultural.federation.agriculturalfederation.entity.CreateMembershipFee;
import org.agricultural.federation.agriculturalfederation.exception.BadRequestException;
import org.agricultural.federation.agriculturalfederation.exception.NotFoundException;
import org.agricultural.federation.agriculturalfederation.service.MembershipFeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public class MembershipFeeController {

    private final MembershipFeeService membershipFeeService;

    public MembershipFeeController(MembershipFeeService membershipFeeService) {
        this.membershipFeeService = membershipFeeService;
    }

    @GetMapping("/collectivities/{id}/membershipFees")
    public ResponseEntity<?> getMembershipFees(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(membershipFeeService.getByCollectivityId(id));
        } catch (NotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @PostMapping("/collectivities/{id}/membershipFees")
    public ResponseEntity<?> createMembershipFees(
            @PathVariable Integer id,
            @RequestBody List<CreateMembershipFee> fees) {
        try {
            return ResponseEntity.ok(
                    membershipFeeService.createMembershipFees(id, fees));
        } catch (BadRequestException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}
