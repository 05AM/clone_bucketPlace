package com.example.demo.src.search.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetFurnitureSofasReq {
    private String categoryDetail;
    private String searchWord;
    private String order;
    private String[] color;
    private Integer isSpecialPrice;
    private Integer isRental;
    private Integer isCrossBorderShopping;
    private Integer isRefurbished;
    private Integer minPrice;
    private Integer maxPrice;
    private String[] shipMethod;


    private Integer[] capacity;
    private String[] material;
    private String[] shape;
    private String[] cushionFeeling;
    private Float minFullWidth;
    private Float maxFullWidth;
    private Float minFullDepth;
    private Float maxFullDepth;
    private Float minFullHeight;
    private Float maxFullHeight;
    private Integer hasLegs;
    private Integer hasHeadrest;
    private Integer hasElbowrest;
    private Integer hasStool;
    private Integer isAntipollution;
    private Integer isWaterproof;
    private Integer isCoverRemovable;
    private Integer[] recliningSeat;
    private Integer isAutomatic;
}
