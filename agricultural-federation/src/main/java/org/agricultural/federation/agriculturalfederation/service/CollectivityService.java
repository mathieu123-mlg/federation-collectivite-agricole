package org.agricultural.federation.agriculturalfederation.service;

import org.agricultural.federation.agriculturalfederation.entity.Collectivity;
import org.agricultural.federation.agriculturalfederation.entity.CollectivityIdentifier;
import org.agricultural.federation.agriculturalfederation.entity.CollectivityStructure;
import org.agricultural.federation.agriculturalfederation.entity.CreateCollectivity;
import org.agricultural.federation.agriculturalfederation.entity.CreateMembershipFee;
import org.agricultural.federation.agriculturalfederation.entity.FinancialAccount;
import org.agricultural.federation.agriculturalfederation.entity.Member;
import org.agricultural.federation.agriculturalfederation.entity.MemberOccupation;
import org.agricultural.federation.agriculturalfederation.entity.MembershipFee;
import org.agricultural.federation.agriculturalfederation.entity.Transaction;
import org.agricultural.federation.agriculturalfederation.exception.BadRequestException;
import org.agricultural.federation.agriculturalfederation.exception.NotFoundException;
import org.agricultural.federation.agriculturalfederation.exception.UnAuthorizedException;
import org.agricultural.federation.agriculturalfederation.repository.CollectivityRepository;
import org.agricultural.federation.agriculturalfederation.validator.CollectivityValidator;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.Instant;
import java.util.List;

@Service
public class CollectivityService {
    private final CollectivityRepository collectivityRepository;

    public CollectivityService(CollectivityRepository collectivityRepository) {
        this.collectivityRepository = collectivityRepository;
    }

    public Collectivity findCollectivityById(String id) {
        return collectivityRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Collectivity.id={" + id + ") is not found"));
    }

    public MembershipFee getMembershipFeeById(String id) {
        return collectivityRepository.findMembershipFeeById(id)
                .orElseThrow(() -> new NotFoundException("MembershipFee.id={" + id + ") is not found"));
    }

    public List<Transaction> getCollectivityTransactions(String id, String from, String to) {
        if (from == null || to == null) {
            throw new BadRequestException("Either mandatory query parameter `from` or `to` is not provided.");
        }
        if (Instant.parse(from).isAfter(Instant.parse(to))) {
            throw new BadRequestException("Query Parameter `from` must be before `to`.");
        }
        if (collectivityRepository.findById(id).isPresent()) {
            return collectivityRepository.getCollectivityTransactions(id, Instant.parse(from), Instant.parse(to));
        } else {
            throw new NotFoundException("Collectivity.id={" + id + ") is not found");
        }
    }

    public Collectivity updateCollectivityIdentifier(String id, CollectivityIdentifier collectivityIdentifier) {
        if (collectivityIdentifier.getNumber() == null) {
            throw new BadRequestException("CollectivityIdentifier.number is required.");
        }
        if (collectivityIdentifier.getName() == null || collectivityIdentifier.getName().isEmpty()) {
            throw new BadRequestException("CollectivityIdentifier.name is required.");
        }
        if (collectivityIdentifier.getNumber() <= 0) {
            throw new BadRequestException("CollectivityIdentifier.number must be greater than 0.");
        }
        Collectivity collectivity = collectivityRepository.findById(id).orElse(null);
        if (collectivity != null) {
            Instant updatedAt = collectivity.getUpdatedAt();
            if (updatedAt != null) {
                throw new UnAuthorizedException("Cannot update Collectivity identifier because it has already been updated at ." + updatedAt);
            }
            try {
                collectivityRepository.updateCollectivityIdentifier(id, collectivityIdentifier);
            } catch (RuntimeException e) {
                throw new BadRequestException("Either given number or name is already used by other collectivity.");
            }
            return findCollectivityById(id);
        } else {
            throw new NotFoundException("Collectivity.id={" + id + ") is not found");
        }
    }

    public List<Collectivity> createCollectivity(List<CreateCollectivity> newCollectivity) {
        for (CreateCollectivity collectivity : newCollectivity) {
            if (!collectivity.isFederationApproval()) {
                throw new UnAuthorizedException("CreateCollectivity.federationApproval is not authorized.");
            }
            validateStructure(collectivity.getStructure());
            List<String> memberIdentifiers = collectivity.getMembers();
            if (memberIdentifiers.size() < 10) {
                throw new BadRequestException("Cannot create collectivity with members less than 10.");
            }
        }
        List<Collectivity> collectivityList = collectivityRepository.createCollectivity(newCollectivity);
        for (Collectivity collectivity : collectivityList) {
            List<Member> referees = collectivityRepository.getCollectivityMembersById(collectivity.getId());
            collectivity.setMembers(referees);
        }
        return collectivityList;
    }

    public List<FinancialAccount> getFinancialAccounts(String id, Date at) {
        findCollectivityById(id);
        return collectivityRepository.getFinancialAccounts(id, at);
    }

    public List<MembershipFee> createMembershipFee(String id, List<CreateMembershipFee> createMembershipFees) {
        findCollectivityById(id);
        for (CreateMembershipFee createMembershipFee : createMembershipFees) {
/*
            if (createMembershipFee.getEligibleFrom().toInstant().isAfter(Instant.now())) {
                throw new BadRequestException("MembershipFee.eligibleFrom must be before current_date.");
            }
*/
            if (createMembershipFee.getAmount() < 0) {
                throw new BadRequestException("MembershipFee.amount must be higher than 0.");
            }
            if (createMembershipFee.getFrequency().name().isBlank()) {
                throw new BadRequestException("MembershipFee.frequency is required.");
            }
            if (createMembershipFee.getLabel().isBlank()) {
                throw new BadRequestException("MembershipFee.label is required.");
            }
        }
        return collectivityRepository.createMembershipFee(id, createMembershipFees);
    }

    private static void validateStructure(CollectivityStructure collectivityStructure) {
        Member president = collectivityStructure.getPresident();
        Member vice_president = collectivityStructure.getVicePresident();
        Member treasurer = collectivityStructure.getTreasurer();
        Member secretary = collectivityStructure.getSecretary();

        if (president == null || treasurer == null || secretary == null) {
            throw new NotFoundException("Cannot create collectivity without PRESIDENT, TREASURER and SECRETARY");
        }

        if (!president.getOccupation().equals(MemberOccupation.PRESIDENT)) {
            throw new IllegalArgumentException("President occupation is required for President");
        }
        if (vice_president != null) {
            if (!vice_president.getOccupation().equals(MemberOccupation.VICE_PRESIDENT)) {
                throw new IllegalArgumentException("Vice president occupation is required for Vice president");
            }
        }
        if (!treasurer.getOccupation().equals(MemberOccupation.TREASURER)) {
            throw new IllegalArgumentException("Treasurer occupation is required for Treasurer");
        }
        if (!secretary.getOccupation().equals(MemberOccupation.SECRETARY)) {
            throw new IllegalArgumentException("Secretary occupation is required for Secretary");
        }
    }
}