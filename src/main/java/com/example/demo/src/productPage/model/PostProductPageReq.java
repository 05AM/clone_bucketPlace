package com.example.demo.src.productPage.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostProductPageReq {
    @NotBlank
    private String categoryMain;
    @NotBlank
    private String categoryMid;
    private int productID;
    @NotBlank
    private String mainImageLink;
    @NotNull
    private List<String> bannerImageLink;
    @NotBlank
    private String pageTitle;
    @NotNull
    private int price;
    private int dcRate;
    private String dcEndDate;
    private float pointRate;
    @NotBlank
    private String productInfoImageLink;
    private int isSpecialPrice;
    private int isTodaysDeal;
    private int isRental;
    private int isCrossBorderShopping;
    private int isRefurbished;
    @NotBlank
    private String shipMethod;
    private int shipFee;
    @NotBlank
    private String paymentMethod;
    private String additionalShipFee;
    private int isIsolatedAreaAvailable;
    private int isJejuAreaAvailable;
    private String additionalEtcAreaFee;
    private String proratedShipFee;
    private int returnShipFee;
    private int exchangeShipFee;
    @NotBlank
    private String returnAddress;
    @NotNull
    private int brandID;
}
