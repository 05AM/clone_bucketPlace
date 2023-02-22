package com.example.demo.src.review.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetReviewRes {
    private int reviewID;
    private int userID;
    private int orderListID;
    private int productPageID;
    private int brandID;
    private float rate;
    private String reviewImageLink;
    private String content;
    private int itHelps;
    private String createAt;
    private String updateAt;
    private String state;
}
