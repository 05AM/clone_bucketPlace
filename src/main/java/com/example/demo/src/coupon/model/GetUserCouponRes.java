package com.example.demo.src.coupon.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetUserCouponRes {
    private int userCouponID;
    private int couponID;
    private String couponTitle;
    private int dcRate;
    private int dcAmount;
    private int minOrderAmount;
    private String expirationDate;
    private String createAt;
    private String updateAt;
    private String state;
    private Integer brandID;
}
