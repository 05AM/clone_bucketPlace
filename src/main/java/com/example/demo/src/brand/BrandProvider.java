package com.example.demo.src.brand;

import com.example.demo.config.BaseException;
import com.example.demo.src.brand.model.GetBrandRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class BrandProvider {
    private final BrandDao brandDao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public BrandProvider(BrandDao brandDao, JwtService jwtService) {
        this.brandDao = brandDao;
        this.jwtService = jwtService;
    }

    public GetBrandRes getBrand(int brandID) throws BaseException{
        return brandDao.getBrand(brandID);
    }

    public List<GetBrandRes> getBrands(String state) {
        return brandDao.getBrands(state);
    }
}
