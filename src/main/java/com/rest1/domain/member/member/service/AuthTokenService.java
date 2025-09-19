package com.rest1.domain.member.member.service;

import com.rest1.domain.member.member.entity.Member;
import com.rest1.standard.ut.Ut;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthTokenService{

    private long expireSeconds = 1000L * 60 * 60 * 24 * 365;
    private String secretPattern = "abcdefghijklmnopqrstuvwxyz1234567890abcdefghijklmnopqrstuvwxyz1234567890";


    //토큰 생성. 토큰 전문가
    public String genAccessToken(Member member) {

        return Ut.jwt.toString(
                secretPattern,
                expireSeconds,
                Map.of("id", member.getId(), "username", member.getName())
        );
    }
}
