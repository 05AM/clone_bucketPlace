package com.example.demo.src.order.model;

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
public class PostUserAddressReq {
    private int userID;
    @NotBlank
    private String addressName;
    @NotBlank
    private String recipientName;
    @NotBlank
    private String recipientContact;
    @NotBlank
    private String mailingAddress;
    @NotBlank
    private String address;
    @NotBlank
    private String detailedAddress;
    @NotNull
    private int isDefault;
}
