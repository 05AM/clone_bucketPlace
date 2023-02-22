package com.example.demo.src.user;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.example.demo.config.BaseResponseStatus.INVALID_USER_JWT;

@RestController
@RequestMapping("/app/users")
public class UserController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final UserProvider userProvider;
    @Autowired
    private final UserService userService;
    @Autowired
    private final JwtService jwtService;




    public UserController(UserProvider userProvider, UserService userService, JwtService jwtService){
        this.userProvider = userProvider;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    /**
     * 특정 회원 조회 API (프로필)
     * [GET] /app/users/:userId/profile
     */
    @ResponseBody
    @GetMapping("/{userID}/profile") // (GET) 127.0.0.1:9000/app/users/:userId/profile
    public BaseResponse<GetUserRes> getUserProfile(@PathVariable("userID") int userID) {
        try{
            GetUserRes getUserRes = userProvider.getUserProfile(userID);
            return new BaseResponse<>(getUserRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 특정 회원 조회 API (나의 쇼핑)
     * [GET] /app/users/:userId/my-shopping
     */
    @ResponseBody
    @GetMapping("/{userID}/my-shopping") // (GET) 127.0.0.1:9000/app/users/:userId/my-shopping
    public BaseResponse<GetUserMyShoppingRes> getUserMyShopping(@PathVariable("userID") int userID) {
        try{
            GetUserMyShoppingRes getUserMyShoppingRes = userProvider.getUserMyShopping(userID);
            return new BaseResponse<>(getUserMyShoppingRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 특정 회원 조회 API (프로필 수정)
     * [GET] /app/users/:userId/profile-modify
     */
    @ResponseBody
    @GetMapping("/{userID}/profile-modify") // (GET) 127.0.0.1:9000/app/users/:userId/profile
    public BaseResponse<GetUserProfileModifyRes> getUserProfileModify(@PathVariable("userID") int userID) {
        try{
            GetUserProfileModifyRes getUserProfileModifyRes = userProvider.getUserProfileModify(userID);
            return new BaseResponse<>(getUserProfileModifyRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 특정 회원 조회 API (알림 설정)
     * [GET] /app/users/:userId/notices
     */
    @ResponseBody
    @GetMapping("/{userID}/notices") // (GET) 127.0.0.1:9000/app/users/:userId/notice
    public BaseResponse<GetUserNoticeRes> getUserNotice(@PathVariable("userID") int userID) {
        try{
            GetUserNoticeRes getUserNoticeRes = userProvider.getUserNotice(userID);
            return new BaseResponse<>(getUserNoticeRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 회원 정보 수정 (프로필) API
     * [PATCH] /app/users/:userIdx/profile
     */
    @ResponseBody
    @PatchMapping("/{userID}/profile")
    public BaseResponse<String> modifyProfile(@PathVariable("userID") int userID, @RequestBody User user){
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            if(userID != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            GetUserProfileModifyRes getUserProfileModifyRes = userProvider.getUserProfileModify(userID);
            Object[] objects = new Object[5];
            if(user.getUserNickname() == null) objects[0] = getUserProfileModifyRes.getUserNickname();
            else{
                if(userProvider.checkUserNickname(user.getUserNickname()) == 1){
                    return new BaseResponse(false, user.getUserNickname() + "은(는) 이미 존재합니다.", 3017);
                }
                objects[0] = user.getUserNickname();
            }
            objects[1] = (user.getProfileImageLink() == null) ? getUserProfileModifyRes.getProfileImageLink() : user.getProfileImageLink();
            objects[2] = (user.getProfileBackgroundImageLink() == null) ? getUserProfileModifyRes.getProfileBackgroundImageLink() : user.getProfileBackgroundImageLink();
            objects[3] = (user.getMyURL() == null) ? getUserProfileModifyRes.getMyURL() : user.getMyURL();
            objects[4] = (user.getIntroduction() == null) ? getUserProfileModifyRes.getIntroduction() : user.getIntroduction();

            PatchUserReq patchUserReq = new PatchUserReq(userID, (String) objects[0], (String) objects[1], (String) objects[2], (String) objects[3], (String) objects[4]);
            userService.modifyUserProfile(patchUserReq);
        return new BaseResponse<>();
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 회원 정보 수정 (알림 설정) API
     * [PATCH] /app/users/:userIdx/notices
     */
    @ResponseBody
    @PatchMapping("/{userID}/notices")
    public BaseResponse<String> modifyNotice(@PathVariable("userID") int userID, @RequestBody User user){
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            if(userID != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            GetUserNoticeRes getUserNoticeRes = userProvider.getUserNotice(userID);
            Object[] objects = new Object[10];
            objects[0] = (user.getIsEmailNoticeAgree() == null) ? Integer.valueOf(getUserNoticeRes.getIsEmailNoticeAgree()) : Integer.valueOf(user.getIsEmailNoticeAgree());
            objects[1] = (user.getIsSMSNoticeAgree() == null) ? Integer.valueOf(getUserNoticeRes.getIsSMSNoticeAgree()) : Integer.valueOf(user.getIsSMSNoticeAgree());
            objects[2] = (user.getIsAppPushNoticeAgree() == null) ? Integer.valueOf(getUserNoticeRes.getIsAppPushNoticeAgree()) : Integer.valueOf(user.getIsAppPushNoticeAgree());
            objects[3] = (user.getIsLikeNoticeAgree() == null) ? Integer.valueOf(getUserNoticeRes.getIsLikeNoticeAgree()) : Integer.valueOf(user.getIsLikeNoticeAgree());
            objects[4] = (user.getIsScrapNoticeAgree() == null) ? Integer.valueOf(getUserNoticeRes.getIsScrapNoticeAgree()) : Integer.valueOf(user.getIsScrapNoticeAgree());
            objects[5] = (user.getIsCommentNoticeAgree() == null) ? Integer.valueOf(getUserNoticeRes.getIsCommentNoticeAgree()) : Integer.valueOf(user.getIsCommentNoticeAgree());
            objects[6] = (user.getIsMentionNoticeAgree() == null) ? Integer.valueOf(getUserNoticeRes.getIsMentionNoticeAgree()) : Integer.valueOf(user.getIsMentionNoticeAgree());
            objects[7] = (user.getIsCommentLikeNoticeAgree() == null) ? Integer.valueOf(getUserNoticeRes.getIsCommentLikeNoticeAgree()) : Integer.valueOf(user.getIsCommentLikeNoticeAgree());
            objects[8] = (user.getIsFollowerNoticeAgree() == null) ? Integer.valueOf(getUserNoticeRes.getIsFollowerNoticeAgree()) : Integer.valueOf(user.getIsFollowerNoticeAgree());
            objects[9] = (user.getIsFollowingNoticeAgree() == null) ? Integer.valueOf(getUserNoticeRes.getIsFollowingNoticeAgree()) : Integer.valueOf(user.getIsFollowingNoticeAgree());

            PatchUserReq patchUserReq = new PatchUserReq(userID, (int) objects[0], (int) objects[1], (int) objects[2], (int) objects[3], (int) objects[4], (int) objects[5], (int) objects[6], (int) objects[7], (int) objects[8], (int) objects[9]);
            userService.modifyUserNotice(patchUserReq);
            return new BaseResponse<>();
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 회원탈퇴 API
     * [PATCH] app/users/:userIdx/deletion
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/{userID}/deletion")
    public BaseResponse<String> leaveUser(@PathVariable("userID") int userID){
        try{
            int userIdxByJwt = jwtService.getUserIdx();
            if(userID != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            userService.leaveUser(userID);
            return new BaseResponse<>();
        }catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
