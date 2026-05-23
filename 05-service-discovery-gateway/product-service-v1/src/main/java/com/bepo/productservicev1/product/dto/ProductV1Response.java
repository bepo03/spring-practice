package com.bepo.productservicev1.product.dto;

public record ProductV1Response(
        Long id,
        String name,
        int price,
        String version
) {
}
