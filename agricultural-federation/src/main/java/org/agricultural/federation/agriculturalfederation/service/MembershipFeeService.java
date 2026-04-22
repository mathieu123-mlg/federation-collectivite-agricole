package org.agricultural.federation.agriculturalfederation.service;

import java.util.ArrayList;
import java.util.List;

import org.agricultural.federation.agriculturalfederation.entity.CreateMembershipFee;
import org.agricultural.federation.agriculturalfederation.entity.MembershipFee;
import org.agricultural.federation.agriculturalfederation.exception.BadRequestException;
import org.agricultural.federation.agriculturalfederation.exception.NotFoundException;
import org.agricultural.federation.agriculturalfederation.repository.CollectivityRepository;
import org.agricultural.federation.agriculturalfederation.repository.MembershipFeeRepository;

public class MembershipFeeService {

    private final MembershipFeeRepository membershipFeeRepository;
    private final CollectivityRepository collectivityRepository;

    public MembershipFeeService(MembershipFeeRepository membershipFeeRepository,
            CollectivityRepository collectivityRepository) {
        this.membershipFeeRepository = membershipFeeRepository;
        this.collectivityRepository = collectivityRepository;
    }

    public List<MembershipFee> getByCollectivityId(Integer collectivityId) {
        if (!collectivityRepository.existsById(collectivityId)) {
            throw new NotFoundException(
                    "Collectivity.id=" + collectivityId + " is not found");
        }
        return membershipFeeRepository.findByCollectivityId(collectivityId);
    }

    public List<MembershipFee> createMembershipFees(
            Integer collectivityId, List<CreateMembershipFee> fees) {
        if (!collectivityRepository.existsById(collectivityId)) {
            throw new NotFoundException(
                    "Collectivity.id=" + collectivityId + " is not found");
        }
        List<String> validFrequencies
                = List.of("WEEKLY", "MONTHLY", "ANNUALLY", "PUNCTUALLY");
        List<MembershipFee> result = new ArrayList<>();
        for (CreateMembershipFee fee : fees) {
            if (!validFrequencies.contains(fee.getFrequency())) {
                throw new BadRequestException(
                        "Unrecognized frequency: " + fee.getFrequency());
            }
            if (fee.getAmount() == null || fee.getAmount() < 0) {
                throw new BadRequestException("Amount must be >= 0");
            }
            result.add(membershipFeeRepository.save(collectivityId, fee));
        }
        return result;
    }
}
