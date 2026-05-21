package com.bepo.orderservice.member.client;

import com.bepo.orderservice.member.dto.MemberResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "member-service",
        url = "${member-service.url}"
)
public interface MemberFeignClient {

    @GetMapping("/api/members/{id}")
    MemberResponse getMember(@PathVariable Long id);
}
