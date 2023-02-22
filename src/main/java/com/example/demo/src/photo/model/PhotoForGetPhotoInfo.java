package com.example.demo.src.photo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PhotoForGetPhotoInfo {
    private String spaceSize;
    private String dwellingType;
    private String style;
    private String photoLink;
    private String content;
    private PhotoHashTag[] photoHashTags;
    private int totalLikes;
    private int totalScraps;
    private int totalComments;

    public PhotoForGetPhotoInfo(String spaceSize, String dwellingType, String style, String photoLink, String content, PhotoHashTag[] photoHashTags) {
        this.spaceSize = spaceSize;
        this.dwellingType = dwellingType;
        this.style = style;
        this.photoLink = photoLink;
        this.content = content;
        this.photoHashTags = photoHashTags;
    }
}
