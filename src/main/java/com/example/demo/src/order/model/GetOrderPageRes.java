package com.example.demo.src.order.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetOrderPageRes {
    private String addressName;
    private String recipientName;
    private String recipientContact;
    private String mailingAddress;
    private String address;
    private String detailedAddress;
    private String ordererName;
    private String ordererPhoneNum;
    private String email;
    private int point;
    List<Cart> cartList;
    List<Coupon> couponList;

    public GetOrderPageRes(String addressName, String recipientName, String recipientContact, String mailingAddress, String address, String detailedAddress, String ordererName, String ordererPhoneNum, String email, int point) {
        this.addressName = addressName;
        this.recipientName = recipientName;
        this.recipientContact = recipientContact;
        this.mailingAddress = mailingAddress;
        this.address = address;
        this.detailedAddress = detailedAddress;
        this.ordererName = ordererName;
        this.ordererPhoneNum = ordererPhoneNum;
        this.email = email;
        this.point = point;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Cart {
        private int cartID;
        private int productPageID;
        private int brandID;
        private String brandName;
        private String mainImageLink;
        private String pageTitle;
        private String shipMethod;
        private int shipFee;
        private String productOption;
        private int price;
        private int quantity;
        private String cartCreateAt;
        private String cartUpdateAt;
        private String cartState;
    }
    @Getter
    @Setter
    @AllArgsConstructor
    public static class Coupon {
        private int userCouponID;
        private int couponID;
        private String couponTitle;
        private int dcRate;
        private int dcAmount;
        private int minOrderAmount;
        private String lastTime;
        private String couponCreateAt;
        private String couponUpdateAt;
        private String couponState;
    }
}
