package com.example.demo.src.cart;

import com.example.demo.config.BaseResponse;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.cart.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/app/carts")
public class CartController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final CartProvider cartProvider;
    @Autowired
    private final CartService cartService;
    @Autowired
    private final JwtService jwtService;


    public CartController(CartProvider cartProvider, CartService cartService, JwtService jwtService) {
        this.cartProvider = cartProvider;
        this.cartService = cartService;
        this.jwtService = jwtService;
    }

    // 특정 인덱스의 장바구니 가져오기
    @ResponseBody
    @GetMapping("/{cartID}")
    public BaseResponse<GetCartRes> getCart(@PathVariable("cartID") int cartID) {
        GetCartRes getCartRes = cartProvider.getCart(cartID);
        return new BaseResponse<>(getCartRes);
    }


    // 유저 장바구니 목록 불러오기
    @ResponseBody
    @GetMapping("/users/{userID}")
    public BaseResponse<List<GetCartRes>> getCarts(
            @PathVariable("userID") int userID,
            @RequestParam(required = false) String state) {

        List<GetCartRes> getCartRes;

        if (state == null) {
            state = "ACTIVE";
        }

        getCartRes = cartProvider.getUserCart(userID, state);

        return new BaseResponse<>(getCartRes);
    }

    // 특정 장바구니의 옵션 불러오기
    @ResponseBody
    @GetMapping("/{cartID}/options")
    public BaseResponse<List<GetCartOptionRes>> getCartOptions(@PathVariable("cartID") int cartID) {

        List<GetCartOptionRes> getCartOptionRes = cartProvider.getProductOptions(cartID);

        return new BaseResponse<>(getCartOptionRes);
    }

    // 장바구니 생성
    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostCartRes> createCart(
            @RequestBody @Validated PostCartReq postCartReq
    ) {
        int userIdxByJwt = jwtService.getUserIdx();
        if(postCartReq.getUserID() != userIdxByJwt){
            return new BaseResponse<>(BaseResponseStatus.INVALID_USER_JWT);
        }

        PostCartRes postCartRes = cartService.createCart(postCartReq);
        return new BaseResponse<>(postCartRes);
    }

    // 장바구니 수정
    @ResponseBody
    @PatchMapping("/{cartID}")
    public BaseResponse<String> modifyCart(
            @PathVariable int cartID,
            @RequestBody @Validated PatchCartReq patchCartReq) {
        int userIdxByJwt = jwtService.getUserIdx();
        if(patchCartReq.getUserID() != userIdxByJwt){
            return new BaseResponse<>(BaseResponseStatus.INVALID_USER_JWT);
        }
        patchCartReq.setCartID(cartID);
        cartService.modifyCart(patchCartReq);

        String result = "modify success";
        return new BaseResponse<>(result);
    }

    // 장바구니 삭제
    @ResponseBody
    @DeleteMapping("/{cartID}")
    public BaseResponse<String> deleteCart(
            @PathVariable int cartID,
            @RequestBody @Validated DeleteCartReq deleteCartReq
    ) {
        int userIdxByJwt = jwtService.getUserIdx();
        if(deleteCartReq.getUserID() != userIdxByJwt){
            return new BaseResponse<>(BaseResponseStatus.INVALID_USER_JWT);
        }
        deleteCartReq.setCartID(cartID);

        cartService.deleteCart(cartID);

        String result = "delete success";
        return new BaseResponse<>(result);
    }
}
