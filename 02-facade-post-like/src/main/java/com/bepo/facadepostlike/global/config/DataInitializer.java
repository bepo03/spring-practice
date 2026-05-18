package com.bepo.facadepostlike.global.config;

import com.bepo.facadepostlike.member.entity.Member;
import com.bepo.facadepostlike.member.repository.MemberRepository;
import com.bepo.facadepostlike.post.entity.Post;
import com.bepo.facadepostlike.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    @Override
    public void run(String... args) throws Exception {
        Member activeMember = memberRepository.save(new Member("활성 회원", true));
        memberRepository.save(new Member("비활성 회원", false));

        postRepository.save(new Post(activeMember.getId(), "첫 번째 게시글", "Facade 패턴 실습 게시글 입니다."));
    }
}
