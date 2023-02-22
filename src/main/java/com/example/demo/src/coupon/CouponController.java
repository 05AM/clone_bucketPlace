package com.example.demo.src.coupon;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.coupon.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.validation.constraints.Size;
import java.util.List;

@RestController
@RequestMapping("/app/coupons")
public class CouponController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final CouponProvider couponProvider;
    @Autowired
    private final CouponService couponService;
    @Autowired
    private final JwtService jwtService;


    public CouponController(CouponProvider couponProvider, CouponService couponService, JwtService jwtService){
        this.couponProvider = couponProvider;
        this.couponService = couponService;
        this.jwtService = jwtService;
    }
    //쿠폰 생성
    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostCouponRes> createCoupon(@RequestBody @Validated PostCouponReq postCouponReq) {
        if(postCouponReq.getDcRate() == 0 && postCouponReq.getDcAmount() == 0) {
            // 할인율, 할인금액 둘 다 null일 경우 예외반환
            return new BaseResponse<>(new BaseException(BaseResponseStatus.REQUEST_ERROR).getStatus());
        } else if(postCouponReq.getDcRate() != 0 && postCouponReq.getDcAmount() != 0) {
            // 할인율, 할인금액이 둘 다 null이 아닐 경우 예외반환
            return new BaseResponse<>(new BaseException(BaseResponseStatus.REQUEST_ERROR).getStatus());
        }
        PostCouponRes postCouponRes = couponService.createCoupon(postCouponReq);

        return new BaseResponse<>(postCouponRes);
    }

    //전체 쿠폰 목록 가져오기
    @ResponseBody
    @GetMapping("")
    public BaseResponse<List<GetCouponRes>> getCoupons(
            @RequestParam(required = false) String state,
            @RequestParam(required = false) Integer userID
    ) {
        if (state == null) {
            state = "ACTIVE";
        }

        List<GetCouponRes> getCouponRes = couponProvider.getCoupons(userID, state);

        return new BaseResponse<>(getCouponRes);
    }

    // 쿠폰 적용 가능한 상품 목록 보기
    @ResponseBody
    @GetMapping("/{couponID}/applicable-products")
    public BaseResponse<List<GetCouponApplicableProduct>> getCouponApplicableProducts(@PathVariable int couponID) {
        List<GetCouponApplicableProduct> getGetCouponApplicableProducts = couponProvider.getCouponApplicableProducts(couponID);

        return new BaseResponse<>(getGetCouponApplicableProducts);
    }

    // 유저 쿠폰 생성 by couponID
    @ResponseBody
    @PostMapping("/user-coupons/users/{userID}")
    public BaseResponse<PostUserCouponRes> createUserCoupon(
            @PathVariable int userID,
            @RequestParam(required = false) Integer couponID,
            @RequestParam(required = false) @Validated @Size(min = 36, max = 36) String couponCode) {
        int userIdxByJwt = jwtService.getUserIdx();
        if(userID != userIdxByJwt){
            return new BaseResponse<>(BaseResponseStatus.INVALID_USER_JWT);
        }

        PostUserCouponRes postUserCouponRes;

        if(couponID != null && couponCode == null) {
            // couponID로 요청이 들어온 경우
            postUserCouponRes = couponService.createUserCoupon(userID, couponID);
        } else if(couponID == null && couponCode != null) {
            // couponCode로 요청이 들어온 경우
            postUserCouponRes = couponService.createUserCoupon(userID, couponCode);
        }
        else {
            return new BaseResponse<>(new BaseException(BaseResponseStatus.REQUEST_ERROR).getStatus());
        }

        return new BaseResponse<>(postUserCouponRes);
    }

    //유저 쿠폰 목록 가져오기
    @ResponseBody
    @GetMapping("/user-coupons/users/{userID}")
    public BaseResponse<List<GetUserCouponRes>> getUserCoupons(
            @RequestParam(required = false) String state,
            @PathVariable("userID") int userID) {
        if (state == null) {
            state = "ACTIVE";
        }

        List<GetUserCouponRes> getUserCouponRes = couponProvider.getUserCoupons(userID, state);

        return new BaseResponse<>(getUserCouponRes);
    }

    // 유저 쿠폰 삭제
    // 쿠폰 기한 만료시
    @ResponseBody
    @DeleteMapping("/user-coupons/{userCouponID}")
    public BaseResponse<String> deleteUserCoupon(@PathVariable int userCouponID) {
        couponService.deleteUserCoupon(userCouponID);

        return new BaseResponse<>("User Coupon Delete success");
    }

    // 유저 쿠폰 수정
    // 쿠폰 사용시
    @ResponseBody
    @PatchMapping("/user-coupons/{userCouponID}")
    public BaseResponse<String> modifyUserCoupon(@PathVariable int userCouponID) {
        couponService.modifyUserCoupon(userCouponID);

        return new BaseResponse<>("User Coupon Modify success");
    }

    //쿠폰 삭제
    @ResponseBody
    @DeleteMapping("/{couponID}")
    public BaseResponse<String> deleteCoupon(@PathVariable int couponID) {
        couponService.deleteCoupon(couponID);

        return new BaseResponse<>("Coupon Delete success");
    }


//    //쿠폰 수정
//    @ResponseBody
//    @PatchMapping("/{couponID}")

}
