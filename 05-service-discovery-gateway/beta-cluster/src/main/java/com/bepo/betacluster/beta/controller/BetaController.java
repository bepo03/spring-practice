package com.bepo.betacluster.beta.controller;

import com.bepo.betacluster.beta.dto.BetaResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BetaController {

    @GetMapping({
            "/api/v1/products/{id}",
            "/api/v2/products/{id}",
            "/api/products/{id}",
            "/api/members/{id}"
    })
    public BetaResponse findById(
            @PathVariable
            Long id
    ) {
        return new BetaResponse(
                id,
                "beta-" + id,
                "beta-cluster"
        );
    }
}