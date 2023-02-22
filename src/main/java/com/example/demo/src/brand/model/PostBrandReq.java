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
public class PostBrandReq {
    @NotBlank
    private String brandName;
    @NotBlank
    private String businessName;
    @NotBlank
    String representativeName;
    @NotBlank
    private String businessLocation;
    @NotBlank
    private String inquiryPhoneNum;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String registrationNum;

}
