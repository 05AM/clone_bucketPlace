package com.example.demo.src.product;

import com.example.demo.config.BaseResponse;
import com.example.demo.src.product.model.PostProductRes;
import com.example.demo.src.product.model.PostProductSofaReq;
import com.example.demo.src.productPage.model.PostProductPageRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/app/products")
public class ProductController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final ProductProvider productProvider;
    @Autowired
    private final ProductService productService;
    @Autowired
    private final JwtService jwtService;


    public ProductController(ProductProvider productProvider, ProductService productService, JwtService jwtService) {
        this.productProvider = productProvider;
        this.productService = productService;
        this.jwtService = jwtService;
    }

    // 상품 생성
    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostProductRes> createProductSofa(
            @RequestBody @Validated PostProductSofaReq postProductSofaReq
            ) {
        PostProductRes postProductRes =  productService.createProductSofa(postProductSofaReq);

        return new BaseResponse<>(postProductRes);
    }
}
