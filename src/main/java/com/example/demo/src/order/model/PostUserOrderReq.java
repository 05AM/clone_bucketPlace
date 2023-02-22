package com.example.demo.src.order.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostUserOrderReq {
    private int userID;
    @NotBlank
    private String orderStatus;
    @NotBlank
    private String paymentMethod;
    private int usedPointAmount;
    private int couponDCAmount;
    @NotNull
    private int totalAmount;
    @NotBlank
    private String orderer;
    @NotBlank
    private String contact;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String memo;
    private List<Cart> cartList;
    private List<Integer> couponList;
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Cart {
        @NotNull
        private int cartID;
        @NotNull
        private int productPageID;
        @NotNull
        private int brandID;
        @NotBlank
        private String brandName;
        @NotBlank
        private String mainImageLink;
        @NotBlank
        private String pageTitle;
        @NotBlank
        private String shipMethod;
        @NotNull
        private int shipFee;
        @NotBlank
        private String productOption;
        @NotNull
        private int price;
        @NotNull
        private int quantity;
        @NotBlank
        private String cartCreateAt;
        @NotBlank
        private String cartUpdateAt;
        @NotBlank
        private String cartState;
    }
}
