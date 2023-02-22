package com.example.demo.src.coupon;

import com.example.demo.src.coupon.model.GetCouponApplicableProduct;
import com.example.demo.src.coupon.model.GetCouponRes;
import com.example.demo.src.coupon.model.GetUserCouponRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CouponProvider {
    private final CouponDao couponDao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public CouponProvider(CouponDao couponDao, JwtService jwtService) {
        this.couponDao = couponDao;
        this.jwtService = jwtService;
    }

    public List<GetCouponRes> getCoupons(Integer userID, String state) {
        return couponDao.getCoupons(userID, state);
    }

    public List<GetCouponApplicableProduct> getCouponApplicableProducts(int couponID) {
        return couponDao.getCouponApplicableProducts(couponID);
    }

    public int checkUserCouponDuplicationByID(int userID, int couponID) {
        return couponDao.checkUserCouponDuplication(userID, couponID);
    }

    public int checkUserCouponDuplicationByCode(int userID, String couponCode) {
        return couponDao.checkUserCouponDuplication(userID, couponCode);
    }

    public List<GetUserCouponRes> getUserCoupons(int userID, String state) {
        return couponDao.getUserCoupons(userID, state);
    }
}
