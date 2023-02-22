package com.example.demo.src.scrap.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    private String mainImageLink;
    private String brandName;
    private String pageTitle;
    private int dcRate;
    private int price;
    private double avgRate;
    private int cntReview;
    private String shipMethod;
    private int isTodaysDeal;
}
