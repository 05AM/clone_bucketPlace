package com.example.demo.src.review;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.review.model.DeleteReviewReq;
import com.example.demo.src.review.model.PatchReviewReq;
import com.example.demo.src.review.model.PostReviewReq;
import com.example.demo.src.review.model.PostReviewRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class ReviewService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ReviewDao reviewDao;
    private final ReviewProvider reviewProvider;
    private final JwtService jwtService;


    @Autowired
    public ReviewService(ReviewDao reviewDao, ReviewProvider reviewProvider, JwtService jwtService) {
        this.reviewDao = reviewDao;
        this.reviewProvider = reviewProvider;
        this.jwtService = jwtService;

    }

    public PostReviewRes createReview(PostReviewReq postReviewReq) {
        //주문번호 중복 체크 : 이미 작성된 리뷰가 있는지 검사
        int result = reviewProvider.checkReviewDuplication(postReviewReq);

        if(result == 1) {
            throw new BaseException(BaseResponseStatus.POST_REVIEW_FAIL);
        }

        int reviewID = reviewDao.createReview(postReviewReq);

        return new PostReviewRes(reviewID);
    }

    public void modifyReview(PatchReviewReq patchReviewReq) {
        int result = reviewDao.modifyReview(patchReviewReq);

        if(result == 0)
            throw new BaseException(BaseResponseStatus.MODIFY_REVIEW_FAIL);
    }

    public void deleteReview(DeleteReviewReq deleteReviewReq) {
        int result = reviewDao.deleteReview(deleteReviewReq);

        if(result == 0)
            throw new BaseException(BaseResponseStatus.DELETE_REVIEW_FAIL);
    }
}
