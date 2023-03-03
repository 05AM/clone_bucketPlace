package com.example.demo.src.oauth;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.oauth.OauthProvider;
import com.example.demo.src.oauth.OauthService;
import com.example.demo.src.oauth.model.PostLoginReq;
import com.example.demo.src.oauth.model.PostOauthCreateUserReq;
import com.example.demo.src.oauth.model.PostOauthRes;
import com.example.demo.src.oauth.model.UserInfo;
import com.example.demo.src.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexNickName;
import static com.example.demo.utils.ValidationRegex.isRegexPassword;

@RestController
@RequestMapping("/app/oauth")
public class OauthController {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final com.example.demo.src.oauth.OauthService oauthService;
    @Autowired
    private final com.example.demo.src.oauth.OauthProvider oauthProvider;

    private String apiResult = null;

    public OauthController(OauthService oauthService, OauthProvider oauthProvider){
        this.oauthService = oauthService;
        this.oauthProvider = oauthProvider;
    }

    @ResponseBody
    @GetMapping("/facebook/callback")
    public BaseResponse<PostOauthRes> facebookCallback(@RequestParam("code") String code, HttpSession httpSession){
        System.out.println("code = " + code);
        String accessToken = oauthService.getFaceBookAccessToken(code);
        PostOauthRes userInfo = oauthService.getFaceBookUserInfo(accessToken);
        return new BaseResponse<>(userInfo);
    }

    @ResponseBody
    @GetMapping("/google/callback")
    public BaseResponse<PostOauthRes> googleCallback(@RequestParam("code") String code, HttpSession httpSession){
        System.out.println("code = " + code);
        String accessToken = oauthService.getGoogleAccessToken(code);
        PostOauthRes userInfo = oauthService.getGoogleUserInfo(accessToken);
        return new BaseResponse<>(userInfo);
    }

    @ResponseBody
    @GetMapping("/kakao/callback")
    public BaseResponse<PostOauthRes> kakaoCallback(@RequestParam("code") String code){
        System.out.println("code = " + code);
        String accessToken = oauthService.getKakaoAccessToken(code);
        PostOauthRes userInfo = oauthService.getKakaoUserInfo(accessToken);
        return new BaseResponse<>(userInfo);
    }

    @ResponseBody
    @PostMapping("/sign-up/social")
    public BaseResponse<String> createSocialUser(@RequestHeader int userID, @RequestBody PostOauthCreateUserReq postOauthCreateUserReq) throws BaseException{
        UserInfo user = oauthProvider.getUserInfo(userID);
        String userNickname = user.getNickname();
        if(postOauthCreateUserReq.getUserNickname() != null){
            if(!isRegexNickName(postOauthCreateUserReq.getUserNickname())){
                return new BaseResponse<>(POST_USERS_INVALID_NICKNAME);
            }
        }
        else{
            if(user.getNickname() == null || !isRegexNickName(user.getNickname())){
                return new BaseResponse<>(POST_USERS_INVALID_NICKNAME);
            }
        }
        oauthService.createSocialUser(postOauthCreateUserReq, userID, userNickname);
        return new BaseResponse<>();
    }

    @ResponseBody
    @PostMapping("/sign-up")
    public BaseResponse<String> createNormalUser(@RequestBody PostOauthCreateUserReq postOauthCreateUserReq) throws BaseException {
        if(postOauthCreateUserReq.getEmail() == null){
            return new BaseResponse<>(POST_USERS_EMPTY_EMAIL);
        }
        if(postOauthCreateUserReq.getPassword() == null || !isRegexPassword(postOauthCreateUserReq.getPassword())){
            return new BaseResponse<>(POST_USERS_INVALID_PASSWORD);
        }
        if(postOauthCreateUserReq.getPasswordCheck() == null){
            return new BaseResponse<>(POST_USERS_EMPTY_PASSWORDCHECK);
        }
        if(!postOauthCreateUserReq.getPassword().equals(postOauthCreateUserReq.getPasswordCheck())){
            return new BaseResponse<>(NOT_SAME_PASSWORD);
        }
        if(postOauthCreateUserReq.getUserNickname() == null || !isRegexNickName(postOauthCreateUserReq.getUserNickname())){
            return new BaseResponse<>(POST_USERS_INVALID_NICKNAME);
        }
        if(oauthProvider.checkDuplicatedNickName(postOauthCreateUserReq.getUserNickname()) == 1){
            return new BaseResponse<>(POST_USERS_EXISTS_NICKNAME);
        }
        oauthService.createNormalUser(postOauthCreateUserReq);;
        return new BaseResponse<>();
    }

    @ResponseBody
    @PostMapping("/login")
    public BaseResponse<PostOauthRes> login(@RequestBody PostLoginReq postLoginReq){
        if(postLoginReq.getEmail() == null){
            return new BaseResponse<>(POST_USERS_EMPTY_EMAIL);
        }
        if(postLoginReq.getPassword() == null){
            return new BaseResponse<>(POST_USERS_EMPTY_PASSWORD);
        }
        PostOauthRes postOauthRes = oauthProvider.login(postLoginReq);
        return new BaseResponse<>(postOauthRes);
    }

}