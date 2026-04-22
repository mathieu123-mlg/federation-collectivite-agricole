package org.agricultural.federation.agriculturalfederation.service;

import java.util.ArrayList;
import java.util.List;

import org.agricultural.federation.agriculturalfederation.entity.CreateMember;
import org.agricultural.federation.agriculturalfederation.entity.Member;
import org.agricultural.federation.agriculturalfederation.entity.Referee;
import org.agricultural.federation.agriculturalfederation.exception.BadRequestException;
import org.agricultural.federation.agriculturalfederation.exception.NotFoundException;
import org.agricultural.federation.agriculturalfederation.repository.MemberRepository;
import org.springframework.stereotype.Service;
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
        List<Member> result = new ArrayList<>();
        for (CreateMember cm : newMembers) {
            result.add(processSingleMember(cm));
        }
        return result;
    }

    private Member processSingleMember(CreateMember cm) {
        validatePayments(cm);
        validateReferees(cm);
        
        Member saved = memberRepository.save(cm);
        memberRepository.saveMemberCollectivity(saved.getId(), cm.getCollectivityId());
        
        for (Referee ref : cm.getReferees()) {
            memberRepository.saveReferee(
                saved.getId(),
                ref.getMemberId(),
                cm.getCollectivityId(),
                ref.getRelationship()
            );
        }
        return saved;
    }

    private void validatePayments(CreateMember cm) {
        if (!cm.isRegistrationFeePaid()) {
            throw new BadRequestException("Registration fee not paid.");
        }
        if (!cm.isMembershipDuesPaid()) {
            throw new BadRequestException("Membership dues not paid.");
        }
    }

    private void validateReferees(CreateMember cm) {
        List<Referee> referees = cm.getReferees();

        if (referees == null || referees.size() < 2) {
            throw new BadRequestException(
                "At least 2 confirmed member referees are required.");
        }

        long fromSameCollectivity = 0;
        long fromOtherCollectivity = 0;

        for (Referee ref : referees) {

            if (!memberRepository.existsById(ref.getMemberId())) {
                throw new NotFoundException(
                    "Member.id=" + ref.getMemberId() + " is not found");
            }
            if (!memberRepository.isSeniorMember(ref.getMemberId())) {
                throw new BadRequestException(
                    "Referee Member.id=" + ref.getMemberId() 
                    + " is not a confirmed member.");
            }
            Integer refereeCollectivityId = 
                memberRepository.getCollectivityIdOfMember(ref.getMemberId());
            if (refereeCollectivityId != null && 
                refereeCollectivityId.equals(cm.getCollectivityId())) {
                fromSameCollectivity++;
            } else {
                fromOtherCollectivity++;
            }
        }

        if (fromSameCollectivity < fromOtherCollectivity) {
            throw new BadRequestException(
                "Number of referees from target collectivity must be >= " +
                "referees from other collectivities.");
        }
    }
}
