package com.example.demo.src.productPage;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.productPage.model.PostProductPageReq;
import com.example.demo.src.productPage.model.PostProductPageRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class ProductPageService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ProductPageDao productPageDao;
    private final ProductPageProvider productPageProvider;
    private final JwtService jwtService;


    @Autowired
    public ProductPageService(ProductPageDao productPageDao, ProductPageProvider productPageProvider, JwtService jwtService) {
        this.productPageDao = productPageDao;
        this.productPageProvider = productPageProvider;
        this.jwtService = jwtService;

    }

    public PostProductPageRes createProductPage(PostProductPageReq postProductPageReq) {
        // 상품 제목 중복 검사
        int result = productPageDao.checkTitleDuplication(postProductPageReq);
        if(result == 1)
            throw new BaseException(BaseResponseStatus.POST_PRODUCT_PAGE_EXIST_TITLE);

        // 생성
        Integer productPageID = productPageDao.createProductPage(postProductPageReq);
        if(productPageID == null)
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);

        return new PostProductPageRes(productPageID);
    }

    // 할인 기간이 지난 상품 페이지 할인 정보 초기화
    @Scheduled(cron = "1 0 0 * * *")
    public void checkProductPageDCEndDate(){
        productPageDao.checkProductPageDCEndDate();
    }

}
