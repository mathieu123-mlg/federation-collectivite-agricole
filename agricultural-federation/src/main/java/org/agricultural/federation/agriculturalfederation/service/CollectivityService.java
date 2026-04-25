package org.agricultural.federation.agriculturalfederation.service;

import org.agricultural.federation.agriculturalfederation.entity.*;
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
    private final CollectivityValidator collectivityValidator;

    public CollectivityService(CollectivityRepository collectivityRepository, CollectivityValidator collectivityValidator) {
        this.collectivityRepository = collectivityRepository;
        this.collectivityValidator = collectivityValidator;
    }

    public Collectivity findCollectivityById(String id) {
        return collectivityRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Collectivity.id={" + id + ") is not found"));
    }

    private static void validateStructure(String id, CollectivityStructure collectivityStructure) {
        Member president = collectivityStructure.getPresident();
        Member vice_president = collectivityStructure.getVicePresident();
        Member treasurer = collectivityStructure.getTreasurer();
        Member secretary = collectivityStructure.getSecretary();

        if (president == null || treasurer == null || secretary == null) {
            throw new NotFoundException("Collectivity.id={" + id + ") is not found");
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
//        return collectivityRepository.createCollectivity(newCollectivity);
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<FinancialAccount> getFinancialAccounts(String id, Date at) {
        findCollectivityById(id);
        return collectivityRepository.getFinancialAccounts(id, at);
    }
}