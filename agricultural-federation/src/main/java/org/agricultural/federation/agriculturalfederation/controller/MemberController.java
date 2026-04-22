package org.agricultural.federation.agriculturalfederation.controller;

import java.util.List;

import org.agricultural.federation.agriculturalfederation.entity.CreateMember;
import org.agricultural.federation.agriculturalfederation.exception.BadRequestException;
import org.agricultural.federation.agriculturalfederation.exception.NotFoundException;
import org.agricultural.federation.agriculturalfederation.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
    
}
