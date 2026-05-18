package com.bepo.facadepostlike.post.dto;

import jakarta.validation.constraints.NotNull;

public record PostLikeRequest(
        @NotNull(message = "회원 ID는 필수입니다")
        Long memberId
) {
}
