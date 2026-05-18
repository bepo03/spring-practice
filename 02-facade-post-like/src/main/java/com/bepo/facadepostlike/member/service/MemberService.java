package com.bepo.facadepostlike.member.service;

import com.bepo.facadepostlike.member.entity.Member;
import com.bepo.facadepostlike.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public void validateActiveMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다"));

        if (!member.isActive()) {
            throw new IllegalArgumentException("비활성 회원입니다");
        }
    }
}
