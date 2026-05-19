package com.bepo.hexagonalpostapi.post.application.port.out;

import com.bepo.hexagonalpostapi.post.domain.Post;

public interface PostRepository {

    boolean existsByTitle(String title);

    Post save(Post post);
}
