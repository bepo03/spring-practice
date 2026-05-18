package com.bepo.facadepostlike.notification.service;

import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    public void sendLikeNotification(Long receiverId, Long postId) {
        System.out.println("좋아요 알림 발송: receiverId=" + receiverId + ", postId=" + postId);
    }
}
