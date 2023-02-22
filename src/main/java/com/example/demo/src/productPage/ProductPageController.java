package com.example.demo.src.productPage;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.productPage.model.GetProductOptionRes;
import com.example.demo.src.productPage.model.GetProductPageRes;
import com.example.demo.src.productPage.model.PostProductPageReq;
import com.example.demo.src.productPage.model.PostProductPageRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.REQUEST_ERROR;

@RestController
@RequestMapping("/app/product-pages")
public class ProductPageController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final ProductPageProvider productPageProvider;
    @Autowired
    private final ProductPageService productPageService;
    @Autowired
    private final JwtService jwtService;


    public ProductPageController(ProductPageProvider productPageProvider, ProductPageService productPageService, JwtService jwtService) {
        this.productPageProvider = productPageProvider;
        this.productPageService = productPageService;
        this.jwtService = jwtService;
    }

    // 상품 페이지 생성
    @ResponseBody
    @PostMapping
    public BaseResponse<PostProductPageRes> createProductPage(
            @RequestBody @Validated PostProductPageReq postProductPageReq
    ){
        PostProductPageRes postProductPageRes = productPageService.createProductPage(postProductPageReq);

        return new BaseResponse<>(postProductPageRes);
    }

    // 상품 페이지 목록 가져오기
    @ResponseBody
    @GetMapping("")
    public BaseResponse<List<GetProductPageRes>> getProductPagesSofa(
            @RequestParam(required = false) String state,
            @RequestParam(required = false) String categoryMain,
            @RequestParam(required = false) String categoryMid,
            @RequestParam(required = false) String categoryDetail
    ){
        List<GetProductPageRes> getProductPageRes;

        if (state == null) {
            state = "ACTIVE";
        }

        if(categoryMain == null && categoryMid == null && categoryDetail == null) {
            // 모든 상품 페이지 가져오기
            getProductPageRes = productPageProvider.getProductPages(state);
        }
        else if(categoryMain != null && categoryMid == null && categoryDetail == null) {
            // 대분류로 가져오기
            getProductPageRes = productPageProvider.getProductPages(state, "categoryMain", categoryMain);

        } else if (categoryMain == null && categoryMid != null && categoryDetail == null){
            // 중분류로 가져오기
            getProductPageRes = productPageProvider.getProductPages(state, "categoryMid", categoryMid);
        } else if (categoryMain == null && categoryMid == null && categoryDetail != null){
            // 소분류로 가져오기
            getProductPageRes = productPageProvider.getProductPages(state, "categoryDetail", categoryDetail);
        }
        else {
            return new BaseResponse<>(new BaseException(REQUEST_ERROR).getStatus());
        }

        return new BaseResponse<>(getProductPageRes);
    }

    // 소분류당 베스트 상품 top 10
    @ResponseBody
    @GetMapping("/bests")
    public BaseResponse<List<GetProductPageRes>> getProductPagesTop10(
            @RequestParam String categoryDetail
    ){
        List<GetProductPageRes> getProductPageRes = productPageProvider.getProductPagesTop10(categoryDetail);

        return new BaseResponse<>(getProductPageRes);
    }

    // 특정 상품 페이지 가져오기
    @ResponseBody
    @GetMapping("/{productPageID}")
    public BaseResponse<GetProductPageRes> getProductPage(@PathVariable int productPageID){
        GetProductPageRes getProductPageRes = productPageProvider.getProductPage(productPageID);

        return new BaseResponse<>(getProductPageRes);
    }

    // 특정 브랜드 상품 페이지 목록 가져오기
    @ResponseBody
    @GetMapping("/brands/{brandID}")
    public BaseResponse<List<GetProductPageRes>> getProductPagesByBrand(@PathVariable int brandID){
        List<GetProductPageRes> getProductPageRes = productPageProvider.getProductPagesByBrand(brandID);

        return new BaseResponse<>(getProductPageRes);
    }

    // 상품 옵션 가져오기
    @ResponseBody
    @GetMapping("/{productPageID}/options")
    public BaseResponse<List<GetProductOptionRes>> getProductPageOptions(@PathVariable("productPageID") int productPageID) {

        List<GetProductOptionRes> getProductOptionRes = productPageProvider.getProductOptions(productPageID);

        return new BaseResponse<>(getProductOptionRes);
    }

    // 장바구니에서 할인중인 상품 페이지 가져오기
    @ResponseBody
    @GetMapping("/cart-discounted-products")
    public BaseResponse<List<GetProductPageRes>> getDCProductPagesFromCart(
            @RequestParam int userID
    ){
        List<GetProductPageRes> getProductPageRes = productPageProvider.getDCProductPagesFromCart(userID);

        return new BaseResponse<>(getProductPageRes);
    }
}

