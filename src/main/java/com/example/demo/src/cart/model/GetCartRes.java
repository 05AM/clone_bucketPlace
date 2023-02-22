package com.example.demo.src.cart.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetCartRes {
    private int cartID;
    private int productPageID;
    private int userID;
    private int brandID;
    private String brandName;
    private String mainImageLink;
    private String pageTitle;
    private int price;
    private String shipMethod;
    private int shipFee;
    private String paymentMethod;
    private String productOption;
    private int quantity;
    private String createAt;
    private String updateAt;
    private String state;
}
