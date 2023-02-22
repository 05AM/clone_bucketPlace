package com.example.demo.src.photo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetPhotoCommentRes {
    private int commentID;
    private String profileImageLink;
    private String userNickname;
    private String content;
    private String createAt;
    private int totalLikes;

    public GetPhotoCommentRes(int commentID, String profileImageLink, String userNickname, String content, String createAt) {
        this.commentID = commentID;
        this.profileImageLink = profileImageLink;
        this.userNickname = userNickname;
        this.content = content;
        this.createAt = createAt;
    }
}
