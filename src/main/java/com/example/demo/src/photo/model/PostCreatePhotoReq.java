package com.example.demo.src.photo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostCreatePhotoReq {
    private String spaceSize;
    private String dwellingType;
    private String style;
    private String photoLink;
    private String categoryPhoto;
    private String content;
    private String[] keyword;
}
