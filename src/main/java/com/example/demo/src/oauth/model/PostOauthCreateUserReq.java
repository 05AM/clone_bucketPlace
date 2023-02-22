package com.example.demo.src.oauth.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostOauthCreateUserReq {
    /*private int isOlderFourteen;
    private int ToS;*/
    private String email;
    private String password;
    private String passwordCheck;
    private String userNickname;
    private int isPersonalInfoAgree;
    private int isMarketingAgree;
    private int isNoticeAgree;
}
