package com.example.demo.src.follow;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.follow.model.GetFollowerUserRes;
import com.example.demo.src.like.LikeProvider;
import com.example.demo.src.like.LikeService;
import com.example.demo.src.like.model.GetLikeRes;
import com.example.demo.src.like.model.GetPhotoLikeUserRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;
import static com.example.demo.config.BaseResponseStatus.INVALID_USER_JWT;

@RestController
@RequestMapping("/app/follows")
public class FollowController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final FollowProvider followProvider;
    @Autowired
    private final FollowService followService;
    @Autowired
    private final JwtService jwtService;

    public FollowController(FollowProvider followProvider, FollowService followService, JwtService jwtService){
        this.followProvider = followProvider;
        this.followService = followService;
        this.jwtService = jwtService;
    }

    /**
     * 팔로워 유저 목록 API
     * [GET] /app/follows/follower-list/users/:userId
     */
    @ResponseBody
    @GetMapping("/follower-list/users/{userID}")
    public BaseResponse<List<GetFollowerUserRes>> getUserFollowers(@PathVariable("userID") int userID) {
        try{
            List<GetFollowerUserRes> getFollowerUserRes = followProvider.getUserFollowers(userID);
            return new BaseResponse<>(getFollowerUserRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 팔로잉 유저 목록 API
     * [GET] /app/follows/following-list/users/:userId
     */
    @ResponseBody
    @GetMapping("/following-list/users/{userID}")
    public BaseResponse<List<GetFollowerUserRes>> getUserFollowings(@PathVariable("userID") int userID) {
        try{
            List<GetFollowerUserRes> getFollowerUserRes = followProvider.getUserFollowings(userID);
            return new BaseResponse<>(getFollowerUserRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 팔로우 API (팔로우, 해제)
     * [POST] /app/follows/followers/:userId/followings/:userId
     */
    @ResponseBody
    @PostMapping("/followers/{followerID}/followings/{followingID}")
    public BaseResponse<String> photoLike(@PathVariable("followerID") int followerID, @PathVariable("followingID") int followingID) {
        try{
            int userIdxByJwt = jwtService.getUserIdx();
            if(followerID != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            int checkFollow = followProvider.checkFollow(followerID, followingID);
            if(checkFollow == 0) {
                followService.createFollow(followerID, followingID);
                return new BaseResponse<>("팔로우");
            }
            else{
                String followState = followProvider.getFollowState(followerID, followingID);
                if(followState.equals("INACTIVE")) {
                    followService.follow(followerID, followingID);
                    return new BaseResponse<>("팔로우");
                }
                else if (followState.equals("ACTIVE")) {
                    followService.unFollow(followerID, followingID);
                    return new BaseResponse<>("언팔로우");
                }
                else{
                    return new BaseResponse<>(DATABASE_ERROR);
                }
            }
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

}
