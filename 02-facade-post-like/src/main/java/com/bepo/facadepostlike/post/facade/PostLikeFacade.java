package com.bepo.facadepostlike.post.facade;

import com.bepo.facadepostlike.like.service.LikeService;
import com.bepo.facadepostlike.member.service.MemberService;
import com.bepo.facadepostlike.notification.service.NotificationService;
import com.bepo.facadepostlike.post.entity.Post;
import com.bepo.facadepostlike.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostLikeFacade {

    private final MemberService memberService;
    private final PostService postService;
    private final LikeService likeService;
    private final NotificationService notificationService;

    @Transactional
    public void like(Long memberId, Long postId) {
        memberService.validateActiveMember(memberId);

        Post post = postService.getPost(postId);

        likeService.addLike(memberId, postId);

        notificationService.sendLikeNotification(post.getMemberId(), postId);
    }
}
