package com.bepo.layeredpostapi.post.controller;

import com.bepo.layeredpostapi.global.common.ApiResponse;
import com.bepo.layeredpostapi.post.dto.PostCreateRequest;
import com.bepo.layeredpostapi.post.dto.PostResponse;
import com.bepo.layeredpostapi.post.service.PostService;
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

    private final PostService postService;

    @PostMapping
    public ResponseEntity<ApiResponse<PostResponse>> create(
            @RequestBody @Valid
            PostCreateRequest request
    ) {
        PostResponse response = postService.create(request);

        return ResponseEntity
                .created(URI.create("/api/posts/" + response.getId()))
                .body(ApiResponse.success(response));
    }
}
