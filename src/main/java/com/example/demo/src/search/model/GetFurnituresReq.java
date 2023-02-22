package com.example.demo.src.search.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetFurnituresReq {
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
}
