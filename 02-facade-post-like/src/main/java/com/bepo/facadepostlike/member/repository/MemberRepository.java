package com.bepo.facadepostlike.member.repository;

import com.bepo.facadepostlike.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
