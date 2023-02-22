package com.example.demo.src.cart.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostCartReq {
    @NotNull
    private int userID;
    @NotNull
    private int productPageID;
    @NotNull
    private int brandID;
    @NotBlank
    private String productOption;
    @NotBlank
    private String brandName;
    @NotNull
    @Positive
    private int quantity;
}
