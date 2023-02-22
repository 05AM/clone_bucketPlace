package com.example.demo.src.review.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostReviewReq {
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
    @Size(min = 20, max = 500)
    private String content;
}
