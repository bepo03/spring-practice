package com.bepo.hexagonalpostapi.post.adapter.out.persistence;

import com.bepo.hexagonalpostapi.post.application.port.out.PostRepository;
import com.bepo.hexagonalpostapi.post.domain.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PostPersistenceAdapter implements PostRepository {

    private final PostJpaRepository postJpaRepository;

    @Override
    public boolean existsByTitle(String title) {
        return postJpaRepository.existsByTitle(title);
    }

    @Override
    public Post save(Post post) {
        PostJpaEntity postJpaEntity = PostJpaEntity.from(post);
        PostJpaEntity savedPostJpaEntity = postJpaRepository.save(postJpaEntity);

        return savedPostJpaEntity.toDomain();
    }
}
