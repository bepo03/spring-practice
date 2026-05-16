package com.bepo.layeredpostapi.post.repository;

import com.bepo.layeredpostapi.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

    boolean existsByTitle(String title);
}
