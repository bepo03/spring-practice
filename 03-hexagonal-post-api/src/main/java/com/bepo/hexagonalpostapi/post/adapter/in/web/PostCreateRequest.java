package com.bepo.hexagonalpostapi.post.adapter.in.web;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostCreateRequest {

    @NotBlank(message = "제목은 필수입니다")
    private String title;

    private String content;
}
