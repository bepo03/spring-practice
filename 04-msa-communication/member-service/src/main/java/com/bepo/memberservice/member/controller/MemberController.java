package com.bepo.memberservice.member.controller;

import com.bepo.memberservice.member.dto.MemberResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    @GetMapping("/{id}")
    public MemberResponse getMember(
            @PathVariable
            Long id
    ) throws InterruptedException {
//        Thread.sleep(10_000);

        return new MemberResponse(
                id,
                "member-" + id,
                "member" + id + "@test.com"
        );
    }
}
