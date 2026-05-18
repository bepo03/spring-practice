package com.bepo.facadepostlike.post.repository;

import com.bepo.facadepostlike.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
