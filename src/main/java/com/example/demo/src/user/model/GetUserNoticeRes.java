package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetUserNoticeRes {
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
