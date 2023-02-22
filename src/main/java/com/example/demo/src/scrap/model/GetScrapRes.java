package com.example.demo.src.scrap.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetScrapRes {
    private String userNickname;
    private int totalScrap;
    private int totalProduct;
    private int totalPhoto;
    private Object[] scraps;
}
