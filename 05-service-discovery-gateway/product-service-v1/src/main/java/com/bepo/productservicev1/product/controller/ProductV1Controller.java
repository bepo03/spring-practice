package com.bepo.productservicev1.product.controller;

import com.bepo.productservicev1.product.dto.ProductV1Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/products")
public class ProductV1Controller {

    @GetMapping("/{id}")
    public ProductV1Response findById(
            @PathVariable
            Long id
    ) {
        return new ProductV1Response(
                id,
                "product-v1-" + id,
                10_000,
                "v1"
        );
    }
}
