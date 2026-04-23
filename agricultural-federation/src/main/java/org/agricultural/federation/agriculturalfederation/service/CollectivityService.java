package org.agricultural.federation.agriculturalfederation.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.agricultural.federation.agriculturalfederation.entity.Collectivity;
import org.agricultural.federation.agriculturalfederation.entity.CollectivityStructure;
import org.agricultural.federation.agriculturalfederation.entity.CreateCollectivity;
import org.agricultural.federation.agriculturalfederation.exception.BadRequestException;
import org.agricultural.federation.agriculturalfederation.exception.NotFoundException;
import org.agricultural.federation.agriculturalfederation.repository.CollectivityRepository;
import org.agricultural.federation.agriculturalfederation.validator.CollectivityValidator;
import org.springframework.stereotype.Service;

@Service
public class CollectivityService {
    private final CollectivityRepository collectivityRepository;
    private final CollectivityValidator collectivityValidator;

    public CollectivityService(CollectivityRepository collectivityRepository, CollectivityValidator collectivityValidator) {
        this.collectivityRepository = collectivityRepository;
        this.collectivityValidator = collectivityValidator;
    }

    public List<Collectivity> createCollectivity(List<CreateCollectivity> newCollectivities) {
        if (newCollectivities == null || newCollectivities.isEmpty()) {
            throw new BadRequestException("Request body cannot be empty");
        }

        List<Collectivity> result = new ArrayList<>();
        for (CreateCollectivity cc : newCollectivities) {
            result.add(processSingleCollectivity(cc));
        }
        return result;
    }

    private Collectivity processSingleCollectivity(CreateCollectivity cc) {
        collectivityValidator.validateCreateCollectivity(cc);
        collectivityValidator.validateAllMembersExist(cc);
        collectivityValidator.validateSeniorMembersCount(cc);

        Collectivity collectivity = buildCollectivityEntity(cc);
        Collectivity saved = saveCollectivity(collectivity);
        saveCollectivityRelations(saved.getId(), cc);

        return enrichCollectivityWithDetails(saved.getId());
    }

    private Collectivity buildCollectivityEntity(CreateCollectivity cc) {
        Collectivity c = new Collectivity();
        c.setLocation(cc.getLocation());
        c.setSpeciality("General Agriculture");
        c.setCreationDate(Instant.now());
        c.setFederationApproval(cc.isFederationApproval());
        return c;
    }

    public Collectivity assignCollectivityIdentifier(Integer collectivityId, Integer number, String name) {
        collectivityRepository.assignCollectivityIdentifier(collectivityId, number, name);

        return collectivityRepository.findCollectivityById(collectivityId).orElseThrow(
                () -> new NotFoundException("Collectivity.id={"+collectivityId+") is not found or already updated")
        );
    }

    private Collectivity saveCollectivity(Collectivity collectivity) {
        return collectivityRepository.save(collectivity)
                .orElseThrow(() -> new RuntimeException("Failed to save collectivity"));
    }

    private void saveCollectivityRelations(Integer collectivityId, CreateCollectivity cc) {
        saveMemberRelations(collectivityId, cc.getMembers());
        saveMandateAndRoles(collectivityId, cc.getStructure());
    }

    private void saveMemberRelations(Integer collectivityId, List<Integer> members) {
        members.forEach(m -> collectivityRepository.saveMemberCollectivity(collectivityId, m));
    }

    private void saveMandateAndRoles(Integer collectivityId, CollectivityStructure structure) {
        Instant startDate = Instant.now();
        Instant endDate = startDate.plusSeconds(365L * 24 * 60 * 60);
        Integer mandateId = collectivityRepository.saveMandate(collectivityId, startDate, endDate);

        saveRole(structure.getPresident(), mandateId, "PRESIDENT");
        saveRole(structure.getVicePresident(), mandateId, "VICE_PRESIDENT");
        saveRole(structure.getTreasurer(), mandateId, "TREASURER");
        saveRole(structure.getSecretary(), mandateId, "SECRETARY");
    }

    private void saveRole(Integer memberId, Integer mandateId, String role) {
        collectivityRepository.saveMemberRole(memberId, mandateId, role);
    }

    private Collectivity enrichCollectivityWithDetails(Integer id) {
        return collectivityRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new NotFoundException("Created collectivity not found"));
    }
    public Collectivity getCollectivityById(Integer id) {
        return collectivityRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new NotFoundException("Collectivity.id=" + id + " is not found"));
    }
}