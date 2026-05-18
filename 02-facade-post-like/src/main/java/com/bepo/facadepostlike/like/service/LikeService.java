package com.bepo.facadepostlike.like.service;

import com.bepo.facadepostlike.like.entity.PostLike;
import com.bepo.facadepostlike.like.repository.PostLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final PostLikeRepository postLikeRepository;

    public void addLike(Long memberId, Long postId) {
        if (postLikeRepository.existsByMemberIdAndPostId(memberId, postId)) {
            throw new IllegalArgumentException("이미 좋아요를 눌렀습니다");
        }

        postLikeRepository.save(new PostLike(memberId, postId));
    }
}
