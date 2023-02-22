package com.example.demo.src.search;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.productPage.model.GetProductPageRes;
import com.example.demo.src.search.model.GetFurnitureSofasReq;
import com.example.demo.src.search.model.GetFurnituresReq;
import com.example.demo.src.search.model.GetPopularSearchWordsRes;
import com.example.demo.src.search.model.GetUnifiedSearchRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/app/search")
public class SearchController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final SearchProvider searchProvider;
    @Autowired
    private final SearchService searchService;
    @Autowired
    private final JwtService jwtService;


    public SearchController(SearchProvider searchProvider, SearchService searchService, JwtService jwtService) {
        this.searchProvider = searchProvider;
        this.searchService = searchService;
        this.jwtService = jwtService;
    }

    //통합 검색
    @ResponseBody
    @GetMapping("")
    public BaseResponse<GetUnifiedSearchRes> getUnifiedSearch(
            @RequestParam String searchWord,
            @RequestParam(required = false) String[] hashTag
    ) {
        GetUnifiedSearchRes getUnifiedSearchRes = searchProvider.getUnifiedSearch(searchWord, hashTag);
        // 검색결과가 존재하지 않을때
        if(getUnifiedSearchRes.getProductPageList() == null && getUnifiedSearchRes.getPhotoList() == null)
            return new BaseResponse<>(new BaseException(BaseResponseStatus.GET_SEARCH_RESULT_FAIL).getStatus());

        return new BaseResponse<>(getUnifiedSearchRes);
    }

    //대분류 검색(가구)
    @ResponseBody
    @GetMapping("/furnitures")
    public BaseResponse<List<GetProductPageRes>> getFurniturePPs(
            @RequestParam(required = false) String searchWord,
            @RequestParam(required = false) String order,
            @RequestParam(required = false) String[] color,
            @RequestParam(required = false) Integer isSpecialPrice,
            @RequestParam(required = false) Integer isRental,
            @RequestParam(required = false) Integer isCrossBorderShopping,
            @RequestParam(required = false) Integer isRefurbished,
            @RequestParam(required = false) Integer minPrice,
            @RequestParam(required = false) Integer maxPrice,
            @RequestParam(required = false) String[] shipMethod
    ){
        // 정렬순서
        if(order == null)
            order = "인기순";
        // 최소금액 > 최대금액
        if(maxPrice != null && minPrice != null){
            if(minPrice > maxPrice)
                throw new BaseException(BaseResponseStatus.GET_SEARCH_MIN_BIGGER);
        }

        GetFurnituresReq getFurnituresReq;

        getFurnituresReq = new GetFurnituresReq(searchWord, order, color, isSpecialPrice, isRental,
                isCrossBorderShopping, isRefurbished, minPrice, maxPrice, shipMethod);

        List<GetProductPageRes> getProductPageRes = searchProvider.getFurniturePPs(getFurnituresReq);

        // 가져온 결과가 없을 때
        if(getProductPageRes == null)
            throw new BaseException(BaseResponseStatus.GET_SEARCH_RESULT_FAIL);

        return new BaseResponse<>(getProductPageRes);
    }

    // 소파 검색
    @ResponseBody
    @GetMapping("/furnitures/sofas")
    public BaseResponse<List<GetProductPageRes>> getFurnitureSofaPPs(
            @RequestParam(required = false) String categoryDetail,
            @RequestParam(required = false) String searchWord,
            @RequestParam(required = false) String order,
            @RequestParam(required = false) String[] color,
            @RequestParam(required = false) Integer isSpecialPrice,
            @RequestParam(required = false) Integer isRental,
            @RequestParam(required = false) Integer isCrossBorderShopping,
            @RequestParam(required = false) Integer isRefurbished,
            @RequestParam(required = false) Integer minPrice,
            @RequestParam(required = false) Integer maxPrice,
            @RequestParam(required = false) String[] shipMethod,

            @RequestParam(required = false) Integer[] capacity,
            @RequestParam(required = false) String[] material,
            @RequestParam(required = false) String[] shape,
            @RequestParam(required = false) String[] cushionFeeling,
            @RequestParam(required = false) Float minFullWidth,
            @RequestParam(required = false) Float maxFullWidth,
            @RequestParam(required = false) Float minFullDepth,
            @RequestParam(required = false) Float maxFullDepth,
            @RequestParam(required = false) Float minFullHeight,
            @RequestParam(required = false) Float maxFullHeight,
            @RequestParam(required = false) Integer hasLegs,
            @RequestParam(required = false) Integer hasHeadrest,
            @RequestParam(required = false) Integer isAntipollution,
            @RequestParam(required = false) Integer isWaterproof,
            @RequestParam(required = false) Integer hasElbowrest,
            @RequestParam(required = false) Integer hasStool,
            @RequestParam(required = false) Integer isCoverRemovable,
            @RequestParam(required = false) Integer[] recliningSeat,
            @RequestParam(required = false) Integer isAutomatic
    ){
        // 정렬순서
        if(order == null)
            order = "인기순";
        // 카테고리
        if(categoryDetail == null)
            categoryDetail = "소파";

        // 최소금액 > 최대금액
        if(maxPrice != null && minPrice != null){
            if(minPrice > maxPrice)
                throw new BaseException(BaseResponseStatus.GET_SEARCH_MIN_BIGGER);
        }

        GetFurnitureSofasReq getFurnitureSofasReq = new GetFurnitureSofasReq(categoryDetail, searchWord, order,
                color, isSpecialPrice, isRental, isCrossBorderShopping, isRefurbished, minPrice, maxPrice,
                shipMethod, capacity, material, shape, cushionFeeling, minFullWidth, maxFullWidth, minFullDepth,
                maxFullDepth, minFullHeight, maxFullHeight, hasLegs, hasHeadrest, hasElbowrest, hasStool,
                isAntipollution, isWaterproof, isCoverRemovable, recliningSeat, isAutomatic);

        List<GetProductPageRes> getProductPageRes = searchProvider.getFurnitureSofaPPs(getFurnitureSofasReq);

        // 가져온 결과가 없을 때
        if(getProductPageRes == null)
            throw new BaseException(BaseResponseStatus.GET_SEARCH_RESULT_FAIL);

        return new BaseResponse<>(getProductPageRes);
    }

    //인기 검색어 불러오기(1~10위)
    @ResponseBody
    @GetMapping("/popular-search-words")
    public BaseResponse<List<GetPopularSearchWordsRes>> getPopularSearchWords(){
        List<GetPopularSearchWordsRes> getPopularSearchWordsRes = searchProvider.getPopularSearchWords();

        return new BaseResponse<>(getPopularSearchWordsRes);
    }

}
