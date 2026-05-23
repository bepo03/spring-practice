package com.bepo.memberservice.member.controller;

import com.bepo.memberservice.member.dto.MemberResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    @GetMapping("/{id}")
    public MemberResponse findById(
            @PathVariable
            Long id,
            HttpServletRequest request
    ) {
        return new MemberResponse(
                id,
                "member-" + id,
                "member" + id + "@test.com",
                String.valueOf(request.getLocalPort())
        );
    }
}
