package com.bepo.orderservice.member.client;

import com.bepo.orderservice.member.dto.MemberResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class MemberRestClient {

    private final RestClient restClient;

    public MemberResponse getMember(Long id) {
        return restClient.get()
                .uri("/api/members/{id}", id)
                .retrieve()
                .body(MemberResponse.class);
    }
}
