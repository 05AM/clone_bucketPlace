package com.example.demo.src.order.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetUserAddressRes {
    private int userAddressID;
    private String addressName;
    private String recipientName;
    private String recipientContact;
    private String mailingAddress;
    private String address;
    private String detailedAddress;
    private int isDefault;
}
