package com.example.demo.src.user;


import com.example.demo.config.BaseException;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
@Transactional(readOnly = true)
public class UserProvider {

    private final UserDao userDao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public UserProvider(UserDao userDao, JwtService jwtService) {
        this.userDao = userDao;
        this.jwtService = jwtService;
    }

    public GetUserRes getUserProfile(int userID) throws BaseException {
        try {
            GetUserRes getUserRes = userDao.getUserProfile(userID);
            return getUserRes;
        } catch (Exception exception) {
            logger.error("App - getUserProfile Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public GetUserMyShoppingRes getUserMyShopping(int userID) throws BaseException {
        try {
            GetUserMyShoppingRes getUserMyShoppingRes = userDao.getUserMyShopping(userID);
            return getUserMyShoppingRes;
        } catch (Exception exception) {
            logger.error("App - getUserMyShopping Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public GetUserProfileModifyRes getUserProfileModify(int userID) throws BaseException {
        try {
            GetUserProfileModifyRes getUserProfileModifyRes = userDao.getUserProfileModify(userID);
            return getUserProfileModifyRes;
        } catch (Exception exception) {
            logger.error("App - getUserProfile Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public GetUserNoticeRes getUserNotice(int userID) throws BaseException {
        try {
            GetUserNoticeRes getUserNoticeRes = userDao.getUserNotice(userID);
            return getUserNoticeRes;
        } catch (Exception exception) {
            logger.error("App - getUserNotice Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkUserNickname(String nickname) throws BaseException {
        try{
            return userDao.checkUserNickname(nickname);
        } catch (Exception exception){
            logger.error("App - checkUserNickname Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
