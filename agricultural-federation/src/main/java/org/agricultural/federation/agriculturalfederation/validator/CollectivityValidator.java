package org.agricultural.federation.agriculturalfederation.validator;

import org.agricultural.federation.agriculturalfederation.entity.CollectivityStructure;
import org.agricultural.federation.agriculturalfederation.entity.CreateCollectivity;
import org.agricultural.federation.agriculturalfederation.exception.BadRequestException;
import org.agricultural.federation.agriculturalfederation.exception.NotFoundException;
import org.agricultural.federation.agriculturalfederation.repository.CollectivityRepository;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.util.HashSet;
import java.util.Set;

@Component
public class CollectivityValidator {
    private final CollectivityRepository collectivityRepository;

    public CollectivityValidator(CollectivityRepository collectivityRepository) {
        this.collectivityRepository = collectivityRepository;
    }

    public void validateCreateCollectivity(CreateCollectivity cc) {
        if (!cc.isFederationApproval()) {
            throw new BadRequestException("Collectivity without federation approval");
        }
        if (cc.getStructure() == null) {
            throw new BadRequestException("Structure missing");
        }
        if (cc.getMembers() == null || cc.getMembers().size() < 10) {
            throw new BadRequestException("Minimum 10 members required");
        }
        validateStructureMembers(cc.getStructure());
    }

    public void validateStructureMembers(CollectivityStructure structure) {
        if (structure.getPresident() == null || structure.getVicePresident() == null ||
            structure.getTreasurer() == null || structure.getSecretary() == null) {
            throw new BadRequestException("Complete structure required");
        }
    }

    public void validateAllMembersExist(CreateCollectivity cc) {
        Set<Integer> allIds = collectAllMemberIds(cc);
        for (Integer id : allIds) {
            if (!collectivityRepository.existsMemberById(id)) {
                throw new NotFoundException("Member not found with ID: " + id);
            }
        }
    }

    private Set<Integer> collectAllMemberIds(CreateCollectivity cc) {
        Set<Integer> ids = new HashSet<>(cc.getMembers());
        CollectivityStructure s = cc.getStructure();
        ids.add(s.getPresident());
        ids.add(s.getVicePresident());
        ids.add(s.getTreasurer());
        ids.add(s.getSecretary());
        return ids;
    }

    public void validateSeniorMembersCount(CreateCollectivity cc) {
        long seniorCount = cc.getMembers().stream()
                .filter(this::hasMinimumSixMonthsSeniority)
                .count();
        if (seniorCount < 5) {
            throw new BadRequestException("Need 5+ members with 6+ months seniority");
        }
    }

    private boolean hasMinimumSixMonthsSeniority(Integer memberId) {
        return collectivityRepository.findMemberAdhesionDate(memberId)
                .map(date -> Period.between(date, LocalDate.now()).toTotalMonths() >= 6)
                .orElse(false);
    }

    public void validateCollectivityTransaction(Integer id, Instant from, Instant to) {
        if (id == null) {
            throw new BadRequestException("CollectivityTransaction.id cannot be null");
        }
        if (from == null) {
            throw new BadRequestException("CollectivityTransaction.id cannot be null");
        }
        if (to == null) {
            throw new BadRequestException("CollectivityTransaction.id cannot be null");
        }
        if (from.isAfter(to)) {
            throw new BadRequestException("CollectivityTransaction.from must before to");
        }
    }
}
