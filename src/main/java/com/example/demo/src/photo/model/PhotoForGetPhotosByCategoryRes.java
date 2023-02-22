package com.example.demo.src.photo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PhotoForGetPhotosByCategoryRes {
    private int photoID;
    private String content;
    private String photoLink;
    private int totalLikes;
    private int totalScraps;
    private int totalComments;

    public PhotoForGetPhotosByCategoryRes(int photoID, String content, String photoLink) {
        this.photoID = photoID;
        this.content = content;
        this.photoLink = photoLink;
    }
}
