package com.bepo.productservice.product.controller;

import com.bepo.productservice.product.dto.ProductResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @GetMapping("/{id}")
    public ProductResponse findById(
            @PathVariable
            Long id
    ) {
        return new ProductResponse(
                id,
                "product-" + id,
                10_000
        );
    }
}
