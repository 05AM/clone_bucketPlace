package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetUserMyShoppingRes {
    private int totalCoupon;
    private int totalPoint;
    private String gradeName;
    private int waitingForDeposit;
    private int paymentComplete;
    private int readyForDelivery;
    private int deliveryInProgress;
    private int deliveryComplete;
    private int writeReview;
    private int totalScrapbook;
    private int totalInquiry;
    private int totalReview;
}
