package com.bepo.layeredpostapi.post.service;

import com.bepo.layeredpostapi.post.dto.PostCreateRequest;
import com.bepo.layeredpostapi.post.dto.PostResponse;
import com.bepo.layeredpostapi.post.entity.Post;
import com.bepo.layeredpostapi.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    @Transactional
    public PostResponse create(PostCreateRequest request) {

        if (postRepository.existsByTitle(request.getTitle())) {
            throw new IllegalArgumentException("중복된 제목입니다.");
        }

        Post post = Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .build();

        Post savePost = postRepository.save(post);

        return PostResponse.builder()
                .id(savePost.getId())
                .title(savePost.getTitle())
                .content(savePost.getContent())
                .build();
    }
}
