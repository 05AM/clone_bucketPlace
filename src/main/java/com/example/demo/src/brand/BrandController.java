package com.example.demo.src.brand;

import com.example.demo.config.BaseResponse;
import com.example.demo.src.brand.model.GetBrandRes;
import com.example.demo.src.brand.model.PostBrandReq;
import com.example.demo.src.brand.model.PostBrandRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/app/brands")
public class BrandController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final BrandProvider brandProvider;
    @Autowired
    private final BrandService brandService;
    @Autowired
    private final JwtService jwtService;


    public BrandController(BrandProvider brandProvider, BrandService brandService, JwtService jwtService){
        this.brandProvider = brandProvider;
        this.brandService = brandService;
        this.jwtService = jwtService;
    }

    // 특정 인덱스 브랜드 가져오기
    @ResponseBody
    @GetMapping("/{brandId}")
    public BaseResponse<GetBrandRes> getBrand(@PathVariable("brandId") int brandId) {
        GetBrandRes getBrandRes = brandProvider.getBrand(brandId);
        return new BaseResponse<>(getBrandRes);
    }

    // 브랜드 목록 가져오기
    @ResponseBody
    @GetMapping("")
    public BaseResponse<List<GetBrandRes>> getBrand(
            @RequestParam(required = false) String state
    ) {
        if(state == null){
            state = "ACTIVE";
        }
        List<GetBrandRes> getBrandRes = brandProvider.getBrands(state);

        return new BaseResponse<>(getBrandRes);
    }

    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostBrandRes> createBrand(
            @RequestBody @Validated PostBrandReq postBrandReq
    ) {
        PostBrandRes postBrandRes = brandService.createBrand(postBrandReq);

        return new BaseResponse<>(postBrandRes);
    }
}
