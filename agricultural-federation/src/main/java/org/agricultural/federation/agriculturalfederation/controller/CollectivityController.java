package org.agricultural.federation.agriculturalfederation.controller;

import org.agricultural.federation.agriculturalfederation.entity.*;
import org.agricultural.federation.agriculturalfederation.exception.BadRequestException;
import org.agricultural.federation.agriculturalfederation.exception.NotFoundException;
import org.agricultural.federation.agriculturalfederation.exception.UnAuthorizedException;
import org.agricultural.federation.agriculturalfederation.service.CollectivityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;

@RestController
public class CollectivityController {
    private final CollectivityService collectivityService;

    public CollectivityController(CollectivityService collectivityService) {
        this.collectivityService = collectivityService;
    }

    @PostMapping("/collectivities")
    public ResponseEntity<?> createCollectivity(@RequestBody List<CreateCollectivity> newCollectivity) {
        try {
            List<Collectivity> list = collectivityService.createCollectivity(newCollectivity);
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

    @PutMapping("/collectivities/{id}/informations")
    public ResponseEntity<?> updateCollectivityIdentifier(
            @PathVariable String id,
            @RequestBody CollectivityIdentifier collectivityIdentifier
    ) {
        try {
            Collectivity collectivityWithIdentifier = collectivityService.updateCollectivityIdentifier(id, collectivityIdentifier);
            return ResponseEntity
                    .status(200)
                    .body(collectivityWithIdentifier);
        } catch (BadRequestException e) {
            return ResponseEntity
                    .status(400)
                    .body(e.getMessage());
        } catch (UnAuthorizedException e) {
            return ResponseEntity
                    .status(401)
                    .body(e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity
                    .status(404)
                    .body(e.getMessage());
        }
    }

    @GetMapping("/collectivities/{id}/transactions")
    public ResponseEntity<?> getCollectivityTransactions(
            @PathVariable String id,
            @RequestParam String from,
            @RequestParam String to
    ) {
        try {
            List<Transaction> transaction = collectivityService.getCollectivityTransactions(id, from, to);
            return ResponseEntity
                    .status(200)
                    .body(transaction);
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

    @GetMapping("/collectivities/{id}/membershipFees")
    public ResponseEntity<?> getMembershipFee(@PathVariable String id) {
        try {
            return ResponseEntity
                    .status(200)
                    .body(collectivityService.getMembershipFeeById(id));
        } catch (NotFoundException e) {
            return ResponseEntity
                    .status(404)
                    .body(e.getMessage());
        }
    }

    @GetMapping("/collectivities/{id}")
    public ResponseEntity<?> findCollectivityById(@PathVariable String id) {
        try {
            Collectivity collectivity = collectivityService.findCollectivityById(id);
            return ResponseEntity
                    .status(200)
                    .body(collectivity);
        } catch (NotFoundException e) {
            return ResponseEntity
                    .status(404)
                    .body(e.getMessage());
        }
    }

    @GetMapping("/collectivities/{id}/financialAccounts")
    public ResponseEntity<?> getFinancialAccounts(
            @PathVariable String id,
            @RequestParam Date at
    ) {
        try {
            List<FinancialAccount> account = collectivityService.getFinancialAccounts(id, at);
            return ResponseEntity
                    .status(200)
                    .body(account);
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
