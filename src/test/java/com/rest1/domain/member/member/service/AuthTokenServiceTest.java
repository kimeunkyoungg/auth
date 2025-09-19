package com.rest1.domain.member.member.service;


import com.rest1.domain.member.member.entity.Member;
import com.rest1.domain.member.member.repository.MemberRepository;
import com.rest1.standard.ut.Ut;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class AuthTokenServiceTest {

    @Autowired
    private AuthTokenService authTokenService;

    // 토큰 만료기간: 1년
    private long expireSeconds = 1000L * 60 * 60 * 24 * 365;

    private String secretPattern = "abcdefghijklmnopqrstuvwxyz1234567890abcdefghijklmnopqrstuvwxyz1234567890";
    @Autowired
    private MemberRepository memberRepository;


    @Test
    @DisplayName("authTokenService 존재여부")
    void t1(){
        assertThat(authTokenService).isNotNull();
    }

    @Test
    @DisplayName("jjwt 최신 방식으로 JWT 생성, {name=\"Paul\", age=23}")
    void t2() {
        // 토큰 만료기간: 1년
        SecretKey secretKey = Keys.hmacShaKeyFor(secretPattern.getBytes(StandardCharsets.UTF_8));

        // 발행 시간과 만료 시간 설정
        Date issuedAt = new Date();
        Date expiration = new Date(issuedAt.getTime() + expireSeconds);

        Map<String, Object> payload = Map.of("name", "Paul", "age", 23);

        String jwt = Jwts.builder()
                .claims(payload) // 내용
                .issuedAt(issuedAt) // 생성날짜
                .expiration(expiration) // 만료날짜
                .signWith(secretKey) // 키 서명
                .compact();

        Map<String, Object> parsedPayload = (Map<String, Object>) Jwts
                .parser()
                .verifyWith(secretKey)
                .build()
                .parse(jwt)
                .getPayload();

        assertThat(parsedPayload)
                .containsAllEntriesOf(payload);

        assertThat(jwt).isNotBlank();

        System.out.println("jwt = " + jwt);
    }

    //Ut를 통해 추상화함
    //Test2와 동일한 작업
    @Test
    @DisplayName("Ut.jwt.toString 를 통해서 JWT 생성, {name=\"Paul\", age=23}")
    void t3() {

        Map<String, Object> payload =  Map.of("name", "Paul", "age", 23);
        String jwt = Ut.jwt.toString(
                secretPattern,
                expireSeconds,
                payload
        );

        assertThat(jwt).isNotBlank();

        boolean validResult = Ut.jwt.isValid(jwt, secretPattern); // 유효한 토큰인지 먼저 확인하는 과정이 필요하다.
        assertThat(validResult).isTrue();

        Map<String, Object> parsedPayload = Ut.jwt.payloadOrNull(jwt,secretPattern); //역직렬화. map이 나오도록

        assertThat(parsedPayload)
                .containsAllEntriesOf(payload);

        System.out.println("jwt = " + jwt);
    }

    @Test
    @DisplayName("AuthTokenService를 통해서 accessToken 생성")
    void t4() {

        Member member1 = memberRepository.findByUsername("user3").get();
        String accessToken = authTokenService.genAccessToken(member1);
        assertThat(accessToken).isNotBlank();

        Map<String, Object> payload = authTokenService.payloadOrNull(accessToken);

        assertThat(payload).containsAllEntriesOf(
                Map.of(
                        "id", member1.getId(),
                        "username", member1.getUsername()
                )
        );

        System.out.println("accessToken = " + accessToken);
    }

}
