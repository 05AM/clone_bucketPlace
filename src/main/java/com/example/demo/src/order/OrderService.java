package com.example.demo.src.order;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.order.model.PostUserAddressReq;
import com.example.demo.src.order.model.PostUserAddressRes;
import com.example.demo.src.order.model.PostUserOrderReq;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional()
public class OrderService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final OrderDao orderDao;
    private final OrderProvider orderProvider;
    private final JwtService jwtService;


    @Autowired
    public OrderService(OrderDao orderDao, OrderProvider orderProvider, JwtService jwtService) {
        this.orderDao = orderDao;
        this.orderProvider = orderProvider;
        this.jwtService = jwtService;

    }

    public PostUserAddressRes createUserAddress(PostUserAddressReq postUserAddressReq) {
        // 주소지 이름 중복 체크
        int result = orderProvider.checkAddressNameDuplication(postUserAddressReq.getUserID(), postUserAddressReq.getAddressName());
        if(result == 1)
            throw new BaseException(BaseResponseStatus.POST_ORDER_EXISTS_ADDRESS_NAME);

        int userAddressID = orderDao.createUserAddress(postUserAddressReq);

        return new PostUserAddressRes(userAddressID);
    }

    public Object[] createUserOrders(PostUserOrderReq postUserOrderReq) {
        return orderDao.createUserOrders(postUserOrderReq);
    }
}
