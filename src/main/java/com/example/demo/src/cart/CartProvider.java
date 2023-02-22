package com.example.demo.src.cart;

import com.example.demo.config.BaseException;
import com.example.demo.src.cart.model.GetCartOptionRes;
import com.example.demo.src.cart.model.GetCartRes;
import com.example.demo.src.cart.model.PostCartReq;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional()
public class CartProvider {
    private final CartDao cartDao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public CartProvider(CartDao cartDao, JwtService jwtService) {
        this.cartDao = cartDao;
        this.jwtService = jwtService;
    }

    public GetCartRes getCart(int cartID) throws BaseException {
        return cartDao.getCart(cartID);
    }

    public List<GetCartRes> getUserCart(int userID, String state) {
        return cartDao.getUserCart(userID, state);
    }

    public List<GetCartOptionRes> getProductOptions(int cartID) {
        return cartDao.getProductOptions(cartID);
    }

    public Integer checkDuplication(PostCartReq postCartReq) {

        return cartDao.checkDuplication(postCartReq);
    }
}
