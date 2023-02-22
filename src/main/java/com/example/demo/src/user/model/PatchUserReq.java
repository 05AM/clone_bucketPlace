package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatchUserReq {
    private int userID;
    private String userNickname;
    private String profileImageLink;
    private String profileBackgroundImageLink;
    private String myURL;
    private String introduction;
    private int isEmailNoticeAgree;
    private int isSMSNoticeAgree;
    private int isAppPushNoticeAgree;
    private int isLikeNoticeAgree;
    private int isScrapNoticeAgree;
    private int isCommentNoticeAgree;
    private int isMentionNoticeAgree;
    private int isCommentLikeNoticeAgree;
    private int isFollowerNoticeAgree;
    private int isFollowingNoticeAgree;

    public PatchUserReq(int userID, String userNickname, String profileImageLink, String profileBackgroundImageLink, String myURL, String introduction) {
        this.userID = userID;
        this.userNickname = userNickname;
        this.profileImageLink = profileImageLink;
        this.profileBackgroundImageLink = profileBackgroundImageLink;
        this.myURL = myURL;
        this.introduction = introduction;
    }

    public PatchUserReq(int userID, int isEmailNoticeAgree, int isSMSNoticeAgree, int isAppPushNoticeAgree, int isLikeNoticeAgree, int isScrapNoticeAgree, int isCommentNoticeAgree, int isMentionNoticeAgree, int isCommentLikeNoticeAgree, int isFollowerNoticeAgree, int isFollowingNoticeAgree) {
        this.userID = userID;
        this.isEmailNoticeAgree = isEmailNoticeAgree;
        this.isSMSNoticeAgree = isSMSNoticeAgree;
        this.isAppPushNoticeAgree = isAppPushNoticeAgree;
        this.isLikeNoticeAgree = isLikeNoticeAgree;
        this.isScrapNoticeAgree = isScrapNoticeAgree;
        this.isCommentNoticeAgree = isCommentNoticeAgree;
        this.isMentionNoticeAgree = isMentionNoticeAgree;
        this.isCommentLikeNoticeAgree = isCommentLikeNoticeAgree;
        this.isFollowerNoticeAgree = isFollowerNoticeAgree;
        this.isFollowingNoticeAgree = isFollowingNoticeAgree;
    }
}
