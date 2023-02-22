package com.example.demo.src.review;

import com.example.demo.config.BaseResponse;
import com.example.demo.src.review.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.INVALID_USER_JWT;

@RestController
@RequestMapping("/app/reviews")
public class ReviewController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final ReviewProvider reviewProvider;
    @Autowired
    private final ReviewService reviewService;
    @Autowired
    private final JwtService jwtService;


    public ReviewController(ReviewProvider reviewProvider, ReviewService reviewService, JwtService jwtService){
        this.reviewProvider = reviewProvider;
        this.reviewService = reviewService;
        this.jwtService = jwtService;
    }

    //유저 리뷰 목록 가져오기
    @ResponseBody
    @GetMapping("/users/{userID}")
    public BaseResponse<List<GetReviewRes>> getUserReviews(@PathVariable int userID) {
        List<GetReviewRes> getReviewRes = reviewProvider.getUserReviews(userID);

        return new BaseResponse<>(getReviewRes);
    }


    //상품 판매 페이지 리뷰 목록 가져오기
    @ResponseBody
    @GetMapping("/product-pages/{productPageID}")
    public BaseResponse<List<GetReviewRes>> getProductPageReviews(@PathVariable int productPageID) {
        List<GetReviewRes> getReviewRes = reviewProvider.getProductPageReviews(productPageID);

        return new BaseResponse<>(getReviewRes);
    }


    //브랜드 리뷰 목록 가져오기
    @ResponseBody
    @GetMapping("/brands/{brandID}")
    public BaseResponse<List<GetReviewRes>> getBrandReviews(@PathVariable int brandID) {
        List<GetReviewRes> getReviewRes = reviewProvider.getBrandReviews(brandID);

        return new BaseResponse<>(getReviewRes);
    }

    //리뷰 생성
    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostReviewRes> createReview(
            @RequestBody @Validated PostReviewReq postReviewReq
    ){
        int userIdxByJwt = jwtService.getUserIdx();
        if(postReviewReq.getUserID() != userIdxByJwt){
            return new BaseResponse<>(INVALID_USER_JWT);
        }

        PostReviewRes postReviewRes = reviewService.createReview(postReviewReq);

        return new BaseResponse<>(postReviewRes);
    }

    //리뷰 수정
    @ResponseBody
    @PatchMapping("/{reviewID}")
    public BaseResponse<String> modifyReview(
            @RequestBody @Validated PatchReviewReq patchReviewReq,
            @PathVariable int reviewID) {
        int userIdxByJwt = jwtService.getUserIdx();
        if(patchReviewReq.getUserID() != userIdxByJwt){
            return new BaseResponse<>(INVALID_USER_JWT);
        }

        patchReviewReq.setReviewID(reviewID);
        reviewService.modifyReview(patchReviewReq);

        return new BaseResponse<>("Modify Review Success");
    }

    //리뷰 삭제
    @ResponseBody
    @DeleteMapping("/{reviewID}")
    public BaseResponse<String> deleteReview(
            @PathVariable int reviewID,
            @RequestBody DeleteReviewReq deleteReviewReq) {

        int userIdxByJwt = jwtService.getUserIdx();
        if(deleteReviewReq.getUserID() != userIdxByJwt){
            return new BaseResponse<>(INVALID_USER_JWT);
        }

        deleteReviewReq.setReviewID(reviewID);

        reviewService.deleteReview(deleteReviewReq);

        return new BaseResponse<>("Delete Review Success");
    }

    // 브랜드 리뷰 통계
    @ResponseBody
    @GetMapping("/total/brands/{brandID}")
    public BaseResponse<GetReviewTotalRes> getBrandReviewsTotal(@PathVariable int brandID) {
        GetReviewTotalRes getBrandReviewTotalRes = reviewProvider.getBrandReviewsTotal(brandID);

        return new BaseResponse<>(getBrandReviewTotalRes);
    }

    // 상품 페이지 리뷰 통계
    @ResponseBody
    @GetMapping("/total/product-pages/{productPageID}")
    public BaseResponse<GetReviewTotalRes> getProductPageReviewsTotal(@PathVariable int productPageID) {
        GetReviewTotalRes getReviewTotalRes = reviewProvider.getProductPageReviewsTotal(productPageID);

        return new BaseResponse<>(getReviewTotalRes);
    }
}
