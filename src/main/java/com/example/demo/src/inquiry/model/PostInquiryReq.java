package com.example.demo.src.inquiry.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.procedure.spi.ParameterRegistrationImplementor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostInquiryReq {
    @NotNull
    private int userID;
    @NotNull
    private int productPageID;
    @NotBlank
    private String type;
    @NotBlank
    private String category;
    @NotBlank
    private String productOption;
    @NotBlank
    private String inquiryContent;
    @NotNull
    private int isSecret;
}
