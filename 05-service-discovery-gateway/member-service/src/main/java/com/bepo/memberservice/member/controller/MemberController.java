package com.bepo.memberservice.member.controller;

import com.bepo.memberservice.member.dto.MemberResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/members")
public class MemberController {

    @GetMapping("/{id}")
    public MemberResponse findById(
            @PathVariable
            Long id,
            @RequestHeader(value = "X-Trace-Id", required = false)
            String traceId,
            HttpServletRequest request
    ) {
        log.info("[traceId={}] findById id={}", traceId, id);

        return new MemberResponse(
                id,
                "member-" + id,
                "member" + id + "@test.com",
                String.valueOf(request.getLocalPort())
        );
    }
}
