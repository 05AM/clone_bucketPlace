package com.example.demo.src.order.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetUserOrderRes {
    private int orderListID;
    private int userID;
    private int productPageID;
    private int brandID;
    private String orderStatus;
    private String pageTitle;
    private int productPrice;
    private String productOption;
    private int quantity;
    private int usedPointAmount;
    private int couponDCAmount;
    private int prepaidShipFee;
    private int totalAmount;
    private String paymentMethod;
    private String shipMethod;
    private String shipCompletedDate;
    private String orderer;
    private String contact;
    private String email;
    private String createAt;
    private String updateAt;
    private String state;
}
