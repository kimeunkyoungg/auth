package com.rest1.domain.member.member.entity;


import com.rest1.global.jpa.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@Getter
@Entity
public class Member extends BaseEntity {

    @Column(unique = true)
    private String username;

    private String password;
    private String nickname;

    @Column(unique = true)
    private String apiKey; //인증데이터를 위한 apiKey 도입

    public Member(String username, String password, String nickname) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.apiKey = UUID.randomUUID().toString(); //난수값
    }

    public Member(long id, String usernamem, String nickname) {
        this.setId(id);
        this.username = username;
        this.nickname = nickname;
    }

    public String getName(){
        return nickname;
    }

    public void updateApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
}