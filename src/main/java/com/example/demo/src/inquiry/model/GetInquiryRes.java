package com.example.demo.src.inquiry.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetInquiryRes {
    private int inquiryID;
    private int userID;
    private int productPageID;
    private String category;
    private String type;
    private String inquiryContent;
    private int isSecret;
    private String isAnswered;
    private String createAt;
    private String updateAt;
    private String state;
    private String answerContent;
    private String answerCreateAt;
    private String answerUpdateAt;
    private String answerState;
}
