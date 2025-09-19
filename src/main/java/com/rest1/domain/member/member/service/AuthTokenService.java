package com.rest1.domain.member.member.service;

import com.rest1.domain.member.member.entity.Member;
import com.rest1.standard.ut.Ut;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthTokenService{

    @Value("${custom.jwt.secretPattern}")
    private String secretPattern;
    @Value("${custom.jwt.expireSeconds}")
    private long expireSeconds;

    //토큰 생성. 토큰 전문가
    public String genAccessToken(Member member) {

        return Ut.jwt.toString(
                secretPattern,
                expireSeconds,
                Map.of("id", member.getId(), "username", member.getUsername())
        );
    }


    public Map<String, Object> payloadOrNull(String jwt){
        Map<String, Object> payload = Ut.jwt.payloadOrNull(jwt, secretPattern);

        if(payload == null){
            return null;
        }

        //payload로만 반환을 하면 값이 변환이 될 수도 있고,
        //payload 안에는 내가 필요한 id, name 제외하고 다른 데이터들도 많음
        //원하는 데이터만 받기 위해 전처리 과정을 해준다.
        int id = (int)payload.get("id");
        String username = (String)payload.get("username");

        return Map.of("id", (long)id, "username", username);
    }
}
