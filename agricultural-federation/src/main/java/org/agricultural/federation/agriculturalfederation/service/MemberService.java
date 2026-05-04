package org.agricultural.federation.agriculturalfederation.service;

import org.agricultural.federation.agriculturalfederation.entity.*;
import org.agricultural.federation.agriculturalfederation.exception.BadRequestException;
import org.agricultural.federation.agriculturalfederation.repository.MemberRepository;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public List<Member> createMembers(List<CreateMember> newMembers) {
        if (newMembers == null || newMembers.isEmpty()) {
            throw new BadRequestException("Request body cannot be empty");
        }
        List<CreateMember> membersToCreate = new ArrayList<>();
        for (CreateMember newMember : newMembers) {
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
