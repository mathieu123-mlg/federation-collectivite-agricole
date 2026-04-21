package org.agricultural.federation.agriculturalfederation.service;

import org.agricultural.federation.agriculturalfederation.entity.Collectivity;
import org.agricultural.federation.agriculturalfederation.repository.CollectivityRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CollectivityService {
    private final CollectivityRepository collectivityRepository;

    public CollectivityService(CollectivityRepository collectivityRepository) {
        this.collectivityRepository = collectivityRepository;
    }

    public List<Collectivity> getAllCollectivity() {
        return collectivityRepository.getAllCollectivity();
    }
}
