package com.example.demo.src.inquiry.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeleteInquiryReq {
    @NotNull
    private int userID;
    private int inquiryID;
}
