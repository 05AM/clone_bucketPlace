package com.example.demo.src.oauth;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.oauth.model.PostLoginReq;
import com.example.demo.src.oauth.model.PostOauthRes;
import com.example.demo.src.oauth.model.UserInfo;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.SHA256;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
@Transactional(readOnly = true)
public class OauthProvider {

    private final JwtService jwtService;
    private final OauthDao oauthDao;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public OauthProvider(OauthDao oauthDao, JwtService jwtService){
        this.oauthDao = oauthDao;
        this.jwtService = jwtService;
    }

    public PostOauthRes loginGoogle(String email) throws BaseException {
        int userId = oauthDao.getUserId(email);
        String  jwt = jwtService.createJwt(userId);
        return new PostOauthRes(userId, jwt);
    }

    public PostOauthRes loginKakao(String email) {
        int userId = oauthDao.getUserId(email);
        String  jwt = jwtService.createJwt(userId);
        return new PostOauthRes(userId, jwt);
    }

    public UserInfo getUserInfo(int userID) throws BaseException {
        return oauthDao.getUserInfo(userID);
    }

    public int checkDuplicatedNickName(String nickname) throws BaseException {
        return oauthDao.checkDuplicatedNickName(nickname);
    }

    public PostOauthRes login(PostLoginReq postLoginReq) throws BaseException {
        if(checkEmail(postLoginReq.getEmail()) == 0){
            throw new BaseException(NOT_EXIST_EMAIL);
        }
        UserInfo user;
        String encryptPwd;
        try{
            user = oauthDao.getPwd(postLoginReq.getEmail());
            encryptPwd = new SHA256().encrypt(postLoginReq.getPassword());
        } catch (Exception exception){
            logger.error("App - login Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
        if(user.getPassword().equals(encryptPwd)){
            int userID = user.getUserID();
            String jwt = jwtService.createJwt(userID);
            return new PostOauthRes(userID, jwt);
        }
        else{
            throw new BaseException(FAILED_TO_LOGIN);
        }
    }

    public int checkEmail(String email) throws BaseException{
        try{
            return oauthDao.checkEmail(email);
        } catch (Exception exception){
            logger.error("App - checkEmail Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
