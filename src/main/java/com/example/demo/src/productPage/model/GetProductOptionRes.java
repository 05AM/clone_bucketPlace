package com.example.demo.src.productPage.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetProductOptionRes {
    private int optionID;
    private int optionDetailID;
    private int isSelective;
    private String title;
    private String titleDetail;
    private int price;
    private String state;
    private String stateDetail;
}