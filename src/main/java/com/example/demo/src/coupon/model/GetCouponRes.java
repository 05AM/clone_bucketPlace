package com.example.demo.src.coupon.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetCouponRes {
    private int couponID;
    private String couponCode;
    private String couponTitle;
    private int dcRate;
    private int dcAmount;
    private int minOrderAmount;
    private String openDate;
    private String endDate;
    private int availablePeriod;
    private String createAt;
    private String updateAt;
    private String state;
    private Integer brandID;
}
