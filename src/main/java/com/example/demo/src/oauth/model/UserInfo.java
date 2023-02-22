package com.example.demo.src.oauth.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {
    private int userID;
    private String email;
    private String password;
    private String nickname;

    public UserInfo(int userID, String password) {
        this.userID = userID;
        this.password = password;
    }

    public UserInfo(String email, String nickname) {
        this.email = email;
        this.nickname = nickname;
    }
}
