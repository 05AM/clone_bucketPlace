package com.example.demo.src.coupon;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.coupon.model.PostCouponReq;
import com.example.demo.src.coupon.model.PostCouponRes;
import com.example.demo.src.coupon.model.PostUserCouponRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.UUID;

@Service
@Transactional()
public class CouponService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final CouponDao couponDao;
    private final CouponProvider couponProvider;
    private final JwtService jwtService;


    @Autowired
    public CouponService(CouponDao couponDao, CouponProvider couponProvider, JwtService jwtService) {
        this.couponDao = couponDao;
        this.couponProvider = couponProvider;
        this.jwtService = jwtService;

    }

    public PostCouponRes createCoupon(PostCouponReq postCouponReq) {
        // 36자릿수 문자열
        String couponCode = UUID.randomUUID().toString();
        postCouponReq.setCouponCode(couponCode);

        int result = couponDao.createCoupon(postCouponReq);
        PostCouponRes postCouponRes = new PostCouponRes(result);

        return postCouponRes;
    }

    public PostUserCouponRes createUserCoupon(int userID, int couponID) {
        // 쿠폰 중복 검사
        int result = couponProvider.checkUserCouponDuplicationByID(userID, couponID);

        if(result == 1) {
            throw new BaseException(BaseResponseStatus.POST_COUPON_EXISTS_COUPON);
        }

        int userCouponID = couponDao.createUserCoupon(userID, couponID);

        return new PostUserCouponRes(userCouponID);
    }

    public PostUserCouponRes createUserCoupon(int userID, String couponCode) {
        // 쿠폰 중복 검사
        int result = couponProvider.checkUserCouponDuplicationByCode(userID, couponCode);

        if(result == 1) {
            throw new BaseException(BaseResponseStatus.POST_COUPON_EXISTS_COUPON);
        }

        int userCouponID = couponDao.createUserCoupon(userID, couponCode);
        return new PostUserCouponRes(userCouponID);
    }

    public void modifyUserCoupon(int userCouponID) {
        int result = couponDao.modifyUserCoupon(userCouponID);

        if(result == 0) {
            throw new BaseException(BaseResponseStatus.MODIFY_COUPON_USER_FAIL);
        }
    }

    public void deleteUserCoupon(int userCouponID) {
        int result = couponDao.deleteUserCoupon(userCouponID);

        if(result == 0) {
            throw new BaseException(BaseResponseStatus.DELETE_COUPON_USER_FAIL);
        }
    }

    public void deleteCoupon(int couponID) {
        int result = couponDao.deleteCoupon(couponID);

        if(result == 0) {
            throw new BaseException(BaseResponseStatus.DELETE_COUPON_FAIL);
        }
    }


}
