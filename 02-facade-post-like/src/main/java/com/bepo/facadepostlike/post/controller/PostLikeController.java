package com.bepo.facadepostlike.post.controller;

import com.bepo.facadepostlike.post.dto.PostLikeRequest;
import com.bepo.facadepostlike.post.facade.PostLikeFacade;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PostLikeController {

    private final PostLikeFacade postLikeFacade;

    @PostMapping("/api/posts/{postId}/likes")
    public ResponseEntity<Void> like(
            @PathVariable
            Long postId,
            @Valid @RequestBody
            PostLikeRequest request
    ) {
        postLikeFacade.like(request.memberId(), postId);

        return ResponseEntity.ok().build();
    }
}
