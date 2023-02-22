package com.example.demo.src.order;

import com.example.demo.src.order.model.GetOrderPageRes;
import com.example.demo.src.order.model.GetUserAddressRes;
import com.example.demo.src.order.model.GetUserOrderRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderProvider {
    private final OrderDao orderDao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public OrderProvider(OrderDao orderDao, JwtService jwtService) {
        this.orderDao = orderDao;
        this.jwtService = jwtService;
    }

    public GetOrderPageRes getOrderPage(int userID, Integer[] cartID) {
        return orderDao.getOrderPage(userID, cartID);
    }

    public List<GetUserAddressRes> getUserAddress(int userID) {
        return orderDao.getUserAddress(userID);
    }

    public List<GetUserOrderRes> getUserOrders(int userID, String status) {
        return orderDao.getUserOrders(userID, status);
    }

    public int checkAddressNameDuplication(int userID, String addressName) {
        return orderDao.checkAddressNameDuplication(userID, addressName);
    }
}
