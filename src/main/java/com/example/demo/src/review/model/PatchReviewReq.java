package com.example.demo.src.review.model;

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
public class PatchReviewReq {
    private int reviewID;
    @NotNull
    private int userID;
    @NotNull
    private int orderListID;
    @NotNull
    private int productPageID;
    @NotNull
    private int brandID;
    @NotNull
    private float rate;
    private String reviewImageLink;
    @NotBlank
    private String content;
    @NotNull
    private int itHelps;
    @NotBlank
    private String createAt;
    @NotBlank
    private String updateAt;
    @NotBlank
    private String state;
}
