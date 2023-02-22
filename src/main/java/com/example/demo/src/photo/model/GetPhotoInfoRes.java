package com.example.demo.src.photo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetPhotoInfoRes {
    private String profileImageLink;
    private String userNickname;
    private String introduction;
    private PhotoForGetPhotoInfo photo;
}
