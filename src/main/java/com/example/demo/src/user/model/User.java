package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private int userIdx;
    private String userNickname;
    private String profileImageLink;
    private String profileBackgroundImageLink;
    private int totalPoint;
    private int totalCoupon;
    private String myURL;
    private String introduction;
    private String email;
    private String password;
    private int cntFollower;
    private int cntFollowing;
    private String isEmailNoticeAgree;
    private String isSMSNoticeAgree;
    private String isAppPushNoticeAgree;
    private String isLikeNoticeAgree;
    private String isScrapNoticeAgree;
    private String isCommentNoticeAgree;
    private String isMentionNoticeAgree;
    private String isCommentLikeNoticeAgree;
    private String isFollowerNoticeAgree;
    private String isFollowingNoticeAgree;
}
