package com.example.demo.src.photo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetPhotosByCategoryRes {
    private String profileImageLink;
    private String userNickname;
    private PhotoForGetPhotosByCategoryRes[] photos;
}
