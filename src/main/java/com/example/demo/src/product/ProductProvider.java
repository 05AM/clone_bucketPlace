package com.example.demo.src.product;

import com.example.demo.src.product.model.PostProductSofaReq;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductProvider {
    private final ProductDao productDao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public ProductProvider(ProductDao productDao, JwtService jwtService) {
        this.productDao = productDao;
        this.jwtService = jwtService;
    }

    public int checkProductSofaNameDuplication(PostProductSofaReq postProductSofaReq) {
        return productDao.checkProductSofaNameDuplication(postProductSofaReq);
    }
}
