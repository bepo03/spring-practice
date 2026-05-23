package com.bepo.orderservice.member.dto;

public record MemberResponse(
        Long id,
        String name,
        String email,
        String port
) {
}
