package com.example.demo.src.order;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.order.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/app/orders")
public class OrderController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final OrderProvider orderProvider;
    @Autowired
    private final OrderService orderService;
    @Autowired
    private final JwtService jwtService;


    public OrderController(OrderProvider orderProvider, OrderService orderService, JwtService jwtService){
        this.orderProvider = orderProvider;
        this.orderService = orderService;
        this.jwtService = jwtService;
    }

    // 주문 페이지 불러오기
    @ResponseBody
    @GetMapping("/order-page/users/{userID}")
    public BaseResponse<GetOrderPageRes> getOrderPage(
            @PathVariable int userID,
            @RequestParam("cartID") Integer[] cartID
    ) {
        int userIdxByJwt = jwtService.getUserIdx();
        if(userID != userIdxByJwt){
            return new BaseResponse<>(BaseResponseStatus.INVALID_USER_JWT);
        }

        // 선택된 장바구니가 없을 경우
        if(cartID == null) {
            return new BaseResponse<>(new BaseException(BaseResponseStatus.REQUEST_ERROR).getStatus());
        } else {
            GetOrderPageRes getOrderPageRes = orderProvider.getOrderPage(userID, cartID);
            return new BaseResponse<>(getOrderPageRes);
        }
    }

    // 주문 페이지 배송지 목록 불러오기
    @ResponseBody
    @GetMapping("/order-page/ship-address/users/{userID}")
    public BaseResponse<List<GetUserAddressRes>> getUserAddress(@PathVariable int userID){
        int userIdxByJwt = jwtService.getUserIdx();
        if(userID != userIdxByJwt){
            return new BaseResponse<>(BaseResponseStatus.INVALID_USER_JWT);
        }
        List<GetUserAddressRes> getUserAddress = orderProvider.getUserAddress(userID);

        return new BaseResponse<>(getUserAddress);
    }

    // 배송지 등록
    @ResponseBody
    @PostMapping("/order-page/ship-address/users/{userID}")
    public BaseResponse<PostUserAddressRes> postUserAddress(
            @PathVariable int userID,
            @Validated @RequestBody PostUserAddressReq postUserAddressReq) {
        //jwt에서 idx 추출.
        int userIdxByJwt = jwtService.getUserIdx();
        //userIdx와 접근한 유저가 같은지 확인
        if(userID != userIdxByJwt){
            return new BaseResponse<>(BaseResponseStatus.INVALID_USER_JWT);
        }

        postUserAddressReq.setUserID(userID);
        PostUserAddressRes postUserAddressRes = orderService.createUserAddress(postUserAddressReq);

        return new BaseResponse<>(postUserAddressRes);
    }

    // 주문목록 생성
    @ResponseBody
    @PostMapping("/users/{userID}")
    public BaseResponse<PostUserOrderRes> createUserOrder(
            @PathVariable int userID,
            @RequestBody @Validated PostUserOrderReq postUserOrderReq) {
        int userIdxByJwt = jwtService.getUserIdx();
        if(userID != userIdxByJwt){
            return new BaseResponse<>(BaseResponseStatus.INVALID_USER_JWT);
        }
        postUserOrderReq.setUserID(userID);

        Object[] orderID = orderService.createUserOrders(postUserOrderReq);
        PostUserOrderRes postUserOrderRes = new PostUserOrderRes(orderID);

        return new BaseResponse<>(postUserOrderRes);
    }

    // 특정 유저 전체 및 상태별 주문 목록 가져오기
    @ResponseBody
    @GetMapping("/users/{userID}")
    public BaseResponse<List<GetUserOrderRes>> getUserOrders (
            @PathVariable int userID,
            @RequestParam(required = false) String status
    ) {
        List<GetUserOrderRes> getUserOrderRes = orderProvider.getUserOrders(userID, status);

        return new BaseResponse<>(getUserOrderRes);
    }
}
