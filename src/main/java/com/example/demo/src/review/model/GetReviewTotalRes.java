package com.example.demo.src.review.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetReviewTotalRes {
    private float avgRate;
    private int cnt_1;
    private int cnt_2;
    private int cnt_3;
    private int cnt_4;
    private int cnt_5;
    private int cnt_reviews;
}
