package com.example.demo.src.scrap.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PhotoForTotalScrap {
    private String category;
    private String photoLink;
    private Timestamp createAt;
}
