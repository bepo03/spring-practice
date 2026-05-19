package com.bepo.hexagonalpostapi.post.adapter.in.web;

import com.bepo.hexagonalpostapi.post.domain.Post;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonPropertyOrder({"id", "title", "content"})
public class PostResponse {

    private Long id;
    private String title;
    private String content;

    public static PostResponse from(Post post) {
        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .build();
    }
}
