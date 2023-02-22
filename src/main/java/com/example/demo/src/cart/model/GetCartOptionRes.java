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
public class GetCartOptionRes {
    private int optionID;
    private int optionDetailID;
    private int isSelective;
    private String title;
    private String titleDetail;
    private int price;
    private String state;
    private String stateDetail;
}
