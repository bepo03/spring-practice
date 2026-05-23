package com.bepo.orderservice.member.client;

import com.bepo.orderservice.member.dto.MemberResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "member-service")
public interface MemberFeignClient {

    @GetMapping("/api/members/{id}")
    MemberResponse findById(
            @PathVariable
            Long id,
            @RequestHeader(value = "X-Trace-Id", required = false)
            String traceId
    );
}
