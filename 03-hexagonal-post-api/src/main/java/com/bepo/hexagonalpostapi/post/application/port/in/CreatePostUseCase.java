package com.bepo.hexagonalpostapi.post.application.port.in;

import com.bepo.hexagonalpostapi.post.domain.Post;

public interface CreatePostUseCase {

    Post create(String title, String content);
}
