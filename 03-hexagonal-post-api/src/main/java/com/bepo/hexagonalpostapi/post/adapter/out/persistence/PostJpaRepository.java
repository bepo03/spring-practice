package com.bepo.hexagonalpostapi.post.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PostJpaRepository extends JpaRepository<PostJpaEntity, Long> {

    boolean existsByTitle(String title);
}
