package com.bepo.productservicev2.product.controller;

import com.bepo.productservicev2.product.dto.ProductV2Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2/products")
public class ProductV2Controller {

    @GetMapping("/{id}")
    public ProductV2Response findById(
            @PathVariable
            Long id
    ) {
        return new ProductV2Response(
                id,
                "product-v2-" + id,
                10_000,
                "v2"
        );
    }
}
