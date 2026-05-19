package com.bepo.hexagonalpostapi.post.adapter.in.web;

import com.bepo.hexagonalpostapi.global.common.ApiResponse;
import com.bepo.hexagonalpostapi.post.application.port.in.CreatePostUseCase;
import com.bepo.hexagonalpostapi.post.domain.Post;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

    private final CreatePostUseCase createPostUseCase;

    @PostMapping
    public ResponseEntity<ApiResponse<PostResponse>> create(
            @RequestBody @Valid
            PostCreateRequest request
    ) {
        Post post = createPostUseCase.create(request.getTitle(), request.getContent());

        return ResponseEntity
                .created(URI.create("/api/posts/" + post.getId()))
                .body(ApiResponse.success(PostResponse.from(post)));
    }
}
