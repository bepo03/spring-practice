package com.bepo.productservice.product.dto;

public record ProductResponse(
        Long id,
        String name,
        int price
) {
}
