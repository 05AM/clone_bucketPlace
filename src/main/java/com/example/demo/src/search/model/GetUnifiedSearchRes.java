package com.example.demo.src.search.model;

import com.example.demo.src.productPage.model.GetProductPageRes;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetUnifiedSearchRes {
    private List<GetProductPageRes> productPageList;
    private List<GetPhotoSearchRes> photoList;
}
