package org.agricultural.federation.agriculturalfederation.controller;

import java.util.List;

import org.agricultural.federation.agriculturalfederation.entity.Collectivity;
import org.agricultural.federation.agriculturalfederation.entity.CollectivityIdentifier;
import org.agricultural.federation.agriculturalfederation.entity.CreateCollectivity;
import org.agricultural.federation.agriculturalfederation.exception.BadRequestException;
import org.agricultural.federation.agriculturalfederation.exception.NotFoundException;
import org.agricultural.federation.agriculturalfederation.service.CollectivityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CollectivityController {

    private final CollectivityService collectivityService;

    public CollectivityController(CollectivityService collectivityService) {
        this.collectivityService = collectivityService;
    }

    @PostMapping("/collectivities")
    public ResponseEntity<?> createCollectivity(@RequestBody List<CreateCollectivity> newCollectivity) {
        try {
            var list = collectivityService.createCollectivity(newCollectivity);
            return ResponseEntity
                    .status(201)
                    .body(list);
        } catch (BadRequestException e) {
            return ResponseEntity
                    .status(400)
                    .body(e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity
                    .status(404)
                    .body(e.getMessage());
        }
    }

    @PutMapping("/collectivities/{collectivityId}/assign-identity")
    public ResponseEntity<?> assignCollectivityIdentifier(
            @PathVariable Integer collectivityId,
            @RequestBody CollectivityIdentifier collectivityIdentifier
    ) {
        try {
            Integer number = collectivityIdentifier.getNumber();
            String name = collectivityIdentifier.getName();
            Collectivity collectivityWithIdentifier = collectivityService.assignCollectivityIdentifier(collectivityId, number, name);
            return ResponseEntity
                    .status(200)
                    .body(collectivityWithIdentifier);
        } catch (BadRequestException e) {
            return ResponseEntity
                    .status(400)
                    .body(e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity
                    .status(404)
                    .body(e.getMessage());
        }
    }
}
