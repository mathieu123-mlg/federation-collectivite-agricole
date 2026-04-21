package org.agricultural.federation.agriculturalfederation.controller;

import org.agricultural.federation.agriculturalfederation.service.CollectivityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CollectivityController {
    private final CollectivityService collectivityService;

    public CollectivityController(CollectivityService collectivityService) {
        this.collectivityService = collectivityService;
    }

    @GetMapping("/test")
    public ResponseEntity<?> test() {
        var list = collectivityService.getAllCollectivity();
        return ResponseEntity
                .status(200)
                .body(list);
    }
}
