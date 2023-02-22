package com.example.demo.src.productPage.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetProductPageRes {
    private int productPageID;
    private Integer productID;
    private int brandID;
    private String brandName;
    private String categoryMain;
    private String categoryMid;
    private String pageTitle;
    private String mainImageLink;
    private List<String> bannerImageLink;
    private float avgRate;
    private int cntReview;
    private int price;
    private int dcRate;
    private int dcPrice;
    private String dcEndDate;
    private int isSpecialPrice;
    private int isTodaysDeal;
    private int isRental;
    private int isCrossBorderShopping;
    private int isRefurbished;
    private int pointRate;
    private String productInfoImageLink;
    private String shipMethod;
    private int shipFee;
    private String paymentMethod;
    private String additionalShipFee;
    private int isIsolatedAreaAvailable;
    private int isJejuAreaAvailable;
    private String proratedShipFee;
    private String additionalEtcAreaFee;
    private int returnShipFee;
    private int exchangeShipFee;
    private String returnAddress;
    private String createAt;
    private String updateAt;
    private String state;

    public GetProductPageRes(int productPageID, Integer productID, int brandID, String brandName, String categoryMain, String categoryMid, String pageTitle, String mainImageLink, float avgRate, int cntReview, int price, int dcRate, int dcPrice, String dcEndDate, int isSpecialPrice, int isTodaysDeal, int isRental, int isCrossBorderShopping, int isRefurbished, int pointRate, String productInfoImageLink, String shipMethod, int shipFee, String paymentMethod, String additionalShipFee, int isIsolatedAreaAvailable, int isJejuAreaAvailable, String proratedShipFee, String additionalEtcAreaFee, int returnShipFee, int exchangeShipFee, String returnAddress, String createAt, String updateAt, String state) {
        this.productPageID = productPageID;
        this.productID = productID;
        this.brandID = brandID;
        this.brandName = brandName;
        this.categoryMain = categoryMain;
        this.categoryMid = categoryMid;
        this.pageTitle = pageTitle;
        this.mainImageLink = mainImageLink;
        this.avgRate = avgRate;
        this.cntReview = cntReview;
        this.price = price;
        this.dcRate = dcRate;
        this.dcPrice = dcPrice;
        this.dcEndDate = dcEndDate;
        this.isSpecialPrice = isSpecialPrice;
        this.isTodaysDeal = isTodaysDeal;
        this.isRental = isRental;
        this.isCrossBorderShopping = isCrossBorderShopping;
        this.isRefurbished = isRefurbished;
        this.pointRate = pointRate;
        this.productInfoImageLink = productInfoImageLink;
        this.shipMethod = shipMethod;
        this.shipFee = shipFee;
        this.paymentMethod = paymentMethod;
        this.additionalShipFee = additionalShipFee;
        this.isIsolatedAreaAvailable = isIsolatedAreaAvailable;
        this.isJejuAreaAvailable = isJejuAreaAvailable;
        this.proratedShipFee = proratedShipFee;
        this.additionalEtcAreaFee = additionalEtcAreaFee;
        this.returnShipFee = returnShipFee;
        this.exchangeShipFee = exchangeShipFee;
        this.returnAddress = returnAddress;
        this.createAt = createAt;
        this.updateAt = updateAt;
        this.state = state;
    }
}
