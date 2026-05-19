package com.bepo.hexagonalpostapi.post.application.service;

import com.bepo.hexagonalpostapi.post.application.port.in.CreatePostUseCase;
import com.bepo.hexagonalpostapi.post.application.port.out.PostRepository;
import com.bepo.hexagonalpostapi.post.domain.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreatePostService implements CreatePostUseCase {

    private final PostRepository postRepository;

    @Override
    @Transactional
    public Post create(String title, String content) {
        if (postRepository.existsByTitle(title)) {
            throw new IllegalArgumentException("중복된 제목입니다");
        }

        Post post = Post.create(title, content);

        return postRepository.save(post);
    }
}
