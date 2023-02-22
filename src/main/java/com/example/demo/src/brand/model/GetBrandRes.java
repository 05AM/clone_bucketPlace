package com.example.demo.src.brand.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetBrandRes {
    private int brandID;
    private String brandName;
    private String businessName;
    private float avgRate;
    private int cntReview;
    private String representativeName;
    private String businessLocation;
    private String inquiryPhoneNum;
    private String email;
    private String registrationNum;
    private String created;
    private String updated;
    private String status;
}
