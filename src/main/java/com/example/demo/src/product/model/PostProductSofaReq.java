package com.example.demo.src.product.model;

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
public class PostProductSofaReq {
    @NotNull
    private int brandID;
    @NotBlank
    private String productSofaName;
    @NotBlank
    private String categorySofa;
    @NotNull
    private int capacity;
    @NotBlank
    private String material;
    private String shape;
    @NotBlank
    private String cushionFeeling;
    @NotNull
    private float fullWidth;
    @NotNull
    private float fullDepth;
    @NotNull
    private float fullHeight;
    private int hasLegs;
    private int hasHeadrest;
    private int isAntipollution;
    private int isWaterproof;
    private int hasElbowrest;
    private int hasStool;
    private int isCoverRemovable;
    private int recliningSeat;
    private int isAutomatic;
}
