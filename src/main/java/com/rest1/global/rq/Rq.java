package com.rest1.global.rq;

//요청 & 응답처리 전문가

import com.rest1.domain.member.member.entity.Member;
import com.rest1.domain.member.member.service.MemberService;
import com.rest1.global.exception.ServiceException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Rq {

    private final MemberService memberService;
    private final HttpServletRequest request;
    private final HttpServletResponse response;

    public Member getActor() {

        String apiKey = null;
        String authorization = request.getHeader("Authorization");

        if(authorization != null && !authorization.isEmpty()) { //헤더에 apikey있을때

            if (!authorization.startsWith("Bearer ")) {
                throw new ServiceException("401-2", "헤더의 인증 정보 형식이 올바르지 않습니다.");
            }

            apiKey = authorization.replace("Bearer ", "");

        } else { //헤더에 apikey없을떄 -> cookie

            Cookie[] cookies = request.getCookies();

            if(cookies == null) {
                throw new ServiceException("401-1", "인증 정보가 없습니다.");
            }

            for(Cookie cookie : cookies) {
                if(cookie.getName().equals("apiKey")) {
                    apiKey = cookie.getValue();
                    break;
                }
            }
        }

        Member actor = memberService.findByApiKey(apiKey)
                .orElseThrow(() -> new ServiceException("401-3", "API 키가 올바르지 않습니다."));

        return actor;
    }

    public void addCookie(String name, String value) {

        Cookie cookie = new Cookie(name, value);
        cookie.setDomain("localhost");
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        response.addCookie(cookie);

    }

    public void deleteCookie(String name) {

        Cookie cookie = new Cookie(name, "");
        cookie.setHttpOnly(true);
        cookie.setDomain("localhost");
        cookie.setPath("/");
        cookie.setMaxAge(0);

        response.addCookie(cookie);

    }
}