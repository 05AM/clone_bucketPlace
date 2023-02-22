package com.example.demo.src.user;


import com.example.demo.config.BaseException;
import com.example.demo.src.user.model.PatchUserReq;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
@Transactional(rollbackFor = Exception.class)
public class UserService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserDao userDao;
    private final UserProvider userProvider;
    private final JwtService jwtService;


    @Autowired
    public UserService(UserDao userDao, UserProvider userProvider, JwtService jwtService) {
        this.userDao = userDao;
        this.userProvider = userProvider;
        this.jwtService = jwtService;

    }

    public void modifyUserProfile(PatchUserReq patchUserReq) throws BaseException {
        try{
            int result = userDao.modifyUserProfile(patchUserReq);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_PROFILE);
            }
        } catch(Exception exception){
            logger.error("App - modifyUserProfile Service Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void modifyUserNotice(PatchUserReq patchUserReq) throws BaseException {
        try{
            int result = userDao.modifyUserNotice(patchUserReq);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_NOTICE);
            }
        } catch(Exception exception){
            logger.error("App - modifyUserNotice Service Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void leaveUser(int userID) throws BaseException {
        try{
            int result = userDao.leaveUser(userID);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_LEAVEUSER);
            }
        } catch (Exception exception){
            logger.error("App - leaveUser Service Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
