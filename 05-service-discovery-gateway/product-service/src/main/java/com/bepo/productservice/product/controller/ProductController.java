package com.bepo.productservice.product.controller;

import com.bepo.productservice.product.dto.ProductResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/products")
public class ProductController {

    @GetMapping("/{id}")
    public ProductResponse findById(
            @PathVariable
            Long id,
            @RequestHeader(value = "X-Trace-Id", required = false)
            String traceId
    ) {
        log.info("[traceId={}] findById id={}", traceId, id);

        return new ProductResponse(
                id,
                "product-" + id,
                10_000
        );
    }
}
