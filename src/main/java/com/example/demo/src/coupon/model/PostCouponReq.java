package com.example.demo.src.coupon.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostCouponReq {
    private String couponCode;
    @NotBlank
    private String couponTitle;
    private int dcRate;
    private int dcAmount;
    private int minOrderAmount;
    @NotBlank
    private String openDate;
    @NotBlank
    private String endDate;
    @NotNull
    private int availablePeriod;
    private Integer brandID;
    private Integer[] applicableProductPageID;
}
