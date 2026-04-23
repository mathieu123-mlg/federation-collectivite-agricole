package org.agricultural.federation.agriculturalfederation.controller;

import org.agricultural.federation.agriculturalfederation.entity.CreateMember;
import org.agricultural.federation.agriculturalfederation.entity.CreateMemberPayment;
import org.agricultural.federation.agriculturalfederation.entity.MemberPayment;
import org.agricultural.federation.agriculturalfederation.exception.BadRequestException;
import org.agricultural.federation.agriculturalfederation.exception.NotFoundException;
import org.agricultural.federation.agriculturalfederation.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/members")
    public ResponseEntity<?> createMembers(@RequestBody List<CreateMember> newMembers) {
        try {
            return ResponseEntity
                    .status(201)
                    .body(memberService.createMembers(newMembers));
        } catch (BadRequestException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @PostMapping("/members/{id}/payments")
    public ResponseEntity<List<MemberPayment>> createMembersPayments(
            @PathVariable Integer id,
            @RequestBody List<CreateMemberPayment> createMembersPayments
    ) {
        return ResponseEntity
                .status(201)
                .body(memberService.createMembersPayments(id, createMembersPayments));
    }
}
