package com.example.demo.src.review;

import com.example.demo.src.review.model.GetReviewRes;
import com.example.demo.src.review.model.GetReviewTotalRes;
import com.example.demo.src.review.model.PostReviewReq;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewProvider {
    private final ReviewDao reviewDao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public ReviewProvider(ReviewDao reviewDao, JwtService jwtService) {
        this.reviewDao = reviewDao;
        this.jwtService = jwtService;
    }

    public int checkReviewDuplication(PostReviewReq postReviewReq) {
        return reviewDao.checkReviewDuplication(postReviewReq.getOrderListID());
    }

    public List<GetReviewRes> getUserReviews(int userID) {
        return reviewDao.getUserReviews(userID);
    }

    public List<GetReviewRes> getProductPageReviews(int productPageID) {
        return reviewDao.getProductPageReviews(productPageID);
    }

    public List<GetReviewRes> getBrandReviews(int brandID) {
        return reviewDao.getBrandReviews(brandID);
    }

    public GetReviewTotalRes getBrandReviewsTotal(int brandID) {
        return reviewDao.getBrandReviewsTotal(brandID);
    }

    public GetReviewTotalRes getProductPageReviewsTotal(int productPageID) {
        return reviewDao.getProductPageReviewsTotal(productPageID);
    }
}
