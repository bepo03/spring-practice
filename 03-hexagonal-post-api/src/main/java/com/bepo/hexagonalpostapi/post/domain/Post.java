package com.bepo.hexagonalpostapi.post.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Post {

    private final Long id;
    private final String title;
    private final String content;

    public static Post create(String title, String content) {
        return Post.builder()
                .title(title)
                .content(content)
                .build();
    }

    public Post withId(Long id) {
        return Post.builder()
                .id(id)
                .title(this.title)
                .content(this.content)
                .build();
    }
}
