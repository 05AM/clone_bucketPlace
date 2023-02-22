package com.example.demo.src.coupon.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetCouponApplicableProduct {
    private int couponID;
    private int productPageID;
    private String couponTitle;
    private int couponDcRate;
    private int couponDcAmount;
    private String endDate;
    private String mainImageLink;
    private String brandName;
    private String pageTitle;
    private int productPrice;
    private int productDcRate;
    private int productDcPrice;
    private float avgRate;
    private int cntReview;
    private int isSpecialPrice;
    private int isTodaysDeal;
}
