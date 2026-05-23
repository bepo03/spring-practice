package com.bepo.memberservice.member.dto;

public record MemberResponse(
        Long id,
        String name,
        String email,
        String port
) {
}
