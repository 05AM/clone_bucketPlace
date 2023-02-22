package com.example.demo.src.cart;

import com.example.demo.config.BaseException;
import com.example.demo.src.cart.model.PatchCartReq;
import com.example.demo.src.cart.model.PostCartReq;
import com.example.demo.src.cart.model.PostCartRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
@Transactional()
public class CartService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final CartDao cartDao;
    private final CartProvider cartProvider;
    private final JwtService jwtService;


    @Autowired
    public CartService(CartDao cartDao, CartProvider cartProvider, JwtService jwtService) {
        this.cartDao = cartDao;
        this.cartProvider = cartProvider;
        this.jwtService = jwtService;

    }

    public PostCartRes createCart(PostCartReq postCartReq){
        // 장바구니 중복
        Integer cartID = cartProvider.checkDuplication(postCartReq);

        if(cartID != null) {
            // quantity 1개 더하기
            int result = cartDao.addCartQuantity(cartID, postCartReq.getQuantity());
            if(result == 0) {
                throw new BaseException(MODIFY_FAIL_CART_QUANTITY);
            }

            return new PostCartRes(cartID);
        } else {
            return new PostCartRes(cartDao.createCart(postCartReq));
        }
    }

    public void modifyCart(PatchCartReq patchCartReq) {
        int result = cartDao.modifyCartQuantity(patchCartReq);
        if(result == 0) {
            throw new BaseException(MODIFY_FAIL_CART);
        }
    }

    public void deleteCart(int cartID) {
        int result = cartDao.deleteCart(cartID);
        if(result == 0) {
            throw new BaseException(DELETE_FAIL_CART);
        }
    }
}
