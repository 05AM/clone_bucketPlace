package com.example.demo.src.productPage;

import com.example.demo.src.cart.model.GetCartOptionRes;
import com.example.demo.src.productPage.model.GetProductOptionRes;
import com.example.demo.src.productPage.model.GetProductPageRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductPageProvider {
    private final ProductPageDao productPageDao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public ProductPageProvider(ProductPageDao productPageDao, JwtService jwtService) {
        this.productPageDao = productPageDao;
        this.jwtService = jwtService;
    }

    public List<GetProductPageRes> getProductPages(String state) {
        List<GetProductPageRes> getProductPageRes = productPageDao.getProductPages(state);
        return getProductPageRes;
    }

    public List<GetProductPageRes> getProductPages(String state, String category, String categoryName) {
        List<GetProductPageRes> getProductPageRes = productPageDao.getProductPages(state, category, categoryName);
        return getProductPageRes;
    }

    public GetProductPageRes getProductPage(int productPageID) {
        GetProductPageRes getProductPageRes = productPageDao.getProductPage(productPageID);
        return getProductPageRes;
    }

    public List<GetProductPageRes> getProductPagesByBrand(int brandID) {
        List<GetProductPageRes> getProductPageRes = productPageDao.getProductPagesByBrand(brandID);
        return getProductPageRes;
    }

    public List<GetProductOptionRes> getProductOptions(int productPageID) {
        return productPageDao.getProductOptions(productPageID);
    }

    public List<GetProductPageRes> getProductPagesTop10(String categoryDetail) {
        return productPageDao.getProductPagesTop10(categoryDetail);
    }

    public List<GetProductPageRes> getDCProductPagesFromCart(int userID) {
        return productPageDao.getDCProductPagesFromCart(userID);
    }
}