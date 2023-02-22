package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetUserProfileModifyRes {
    private String userNickname;
    private String profileImageLink;
    private String profileBackgroundImageLink;
    private String myURL;
    private String introduction;
}
