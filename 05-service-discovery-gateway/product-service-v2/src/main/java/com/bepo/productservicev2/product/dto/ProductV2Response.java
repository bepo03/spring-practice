package com.bepo.productservicev2.product.dto;

public record ProductV2Response(
        Long id,
        String name,
        int price,
        String version
) {
}
