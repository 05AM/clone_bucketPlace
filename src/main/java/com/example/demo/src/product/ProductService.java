package com.example.demo.src.product;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.product.model.PostProductRes;
import com.example.demo.src.product.model.PostProductSofaReq;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class ProductService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ProductDao productDao;
    private final ProductProvider productProvider;
    private final JwtService jwtService;


    @Autowired
    public ProductService(ProductDao productDao, ProductProvider productProvider, JwtService jwtService) {
        this.productDao = productDao;
        this.productProvider = productProvider;
        this.jwtService = jwtService;

    }

    public PostProductRes createProductSofa(PostProductSofaReq postProductSofaReq) {
        // 상품명 중복 체크
        int result = productProvider.checkProductSofaNameDuplication(postProductSofaReq);

        if(result == 1)
            throw new BaseException(BaseResponseStatus.POST_PRODUCT_EXIST_PRODUCT_NAME);

        // 삽입
        Integer productID = productDao.createProduct(postProductSofaReq);
        if(productID == null)
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        return new PostProductRes(productID);
    }
}
