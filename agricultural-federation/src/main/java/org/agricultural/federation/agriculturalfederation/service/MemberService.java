package org.agricultural.federation.agriculturalfederation.service;

import org.agricultural.federation.agriculturalfederation.entity.*;
import org.agricultural.federation.agriculturalfederation.exception.BadRequestException;
import org.agricultural.federation.agriculturalfederation.exception.NotFoundException;
import org.agricultural.federation.agriculturalfederation.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final CollectivityService collectivityService;

    public MemberService(MemberRepository memberRepository, CollectivityService collectivityService) {
        this.memberRepository = memberRepository;
        this.collectivityService = collectivityService;
    }

    public List<Member> createMembers(List<CreateMember> newMembers) {
        if (newMembers == null || newMembers.isEmpty()) {
            throw new BadRequestException("Request body cannot be empty");
        }
        List<CreateMember> membersToCreate = new ArrayList<>();
        for (CreateMember newMember : newMembers) {
            collectivityService.findCollectivityById(newMember.getCollectivityIdentifier());
            for (Referee referee : newMember.getReferees()) {
                if (referee.getMemberId() == null) {
                    throw new BadRequestException("CreateMembers.referee.memberId is required");
                } else {
                    if (!memberRepository.existingId(referee.getMemberId())) {
                        throw new NotFoundException("CreateMembers.referee.memberId not found");
                    }
                }
            }
            if (newMember.isRegistrationFeePaid() && newMember.isMembershipDuesPaid()) {
                membersToCreate.add(newMember);
            }
        }
        return memberRepository.saveMembers(membersToCreate);
    }

    public List<MemberPayment> createMembersPayments(String id, List<CreateMemberPayment> createMembersPayments) {
        return memberRepository.createMembersPayments(id, createMembersPayments);
    }
}
