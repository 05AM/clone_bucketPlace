package com.example.demo.src.brand;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.brand.model.PostBrandReq;
import com.example.demo.src.brand.model.PostBrandRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional()
public class BrandService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final BrandDao brandDao;
    private final BrandProvider brandProvider;
    private final JwtService jwtService;


    @Autowired
    public BrandService(BrandDao brandDao, BrandProvider brandProvider, JwtService jwtService) {
        this.brandDao = brandDao;
        this.brandProvider = brandProvider;
        this.jwtService = jwtService;

    }

    public PostBrandRes createBrand(PostBrandReq postBrandReq) {
        // 사업자 등록번호 중복 체크
        int result = brandDao.checkRegistNumDuplication(postBrandReq);
        if(result == 1) {
            throw new BaseException(BaseResponseStatus.POST_BRAND_EXIST_REGIST_NUM);
        }
        // 상호명 중복 체크
        result = brandDao.checkbusinessNameDuplication(postBrandReq);
        if(result == 1) {
            throw new BaseException(BaseResponseStatus.POST_BRAND_EXIST_BUSINESS_NAME);
        }

        Integer brandID = brandDao.createBrand(postBrandReq);
        if(brandID == null){
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }

        return new PostBrandRes(brandID);
    }
}
