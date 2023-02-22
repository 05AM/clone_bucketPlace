package com.example.demo.src.like;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.like.model.GetLikeRes;
import com.example.demo.src.like.model.GetPhotoLikeUserRes;
import com.example.demo.src.scrap.model.GetScrapRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;
import static com.example.demo.config.BaseResponseStatus.INVALID_USER_JWT;

@RestController
@RequestMapping("/app/likes")
public class LikeController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final LikeProvider likeProvider;
    @Autowired
    private final LikeService likeService;
    @Autowired
    private final JwtService jwtService;

    public LikeController(LikeProvider likeProvider, LikeService likeService, JwtService jwtService){
        this.likeProvider = likeProvider;
        this.likeService = likeService;
        this.jwtService = jwtService;
    }

    /**
     * 전체 좋아요 조회 API
     * [GET] /app/likes/users/:userId
     */
    @ResponseBody
    @GetMapping("/users/{userID}")
    public BaseResponse<GetLikeRes> getUserLikes(@PathVariable("userID") int userID) {
        try{
            GetLikeRes getLikeRes = likeProvider.getUserLikes(userID);
            return new BaseResponse<>(getLikeRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 사진 좋아요 API
     * [POST] /app/likes/users/:userId/photos/:photoId
     */
    @ResponseBody
    @PostMapping("/users/{userID}/photos/{photoID}")
    public BaseResponse<String> photoLike(@PathVariable("userID") int userID, @PathVariable("photoID") int photoID) {
        try{
            int userIdxByJwt = jwtService.getUserIdx();
            if(userID != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            int checkLike = likeProvider.checkLike(userID, photoID);
            if(checkLike == 0) {
                likeService.createLike(userID, photoID);
                return new BaseResponse<>("좋아요 생성");
            }
            else{
                String likeState = likeProvider.getLikeState(userID, photoID);
                if(likeState.equals("INACTIVE")) {
                    likeService.doLike(userID, photoID);
                    return new BaseResponse<>("좋아요 생성");
                }
                else if (likeState.equals("ACTIVE")) {
                    likeService.cancelLike(userID, photoID);
                    return new BaseResponse<>("좋아요 취소");
                }
                else{
                    return new BaseResponse<>(DATABASE_ERROR);
                }
            }
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 사진 댓글 좋아요 API
     * [POST] /app/likes/users/:userId/photo-comments/:photoCommentId
     */
    @ResponseBody
    @PostMapping("/users/{userID}/photo-comments/{photoCommentID}")
    public BaseResponse<String> photoCommentLike(@PathVariable("userID") int userID, @PathVariable("photoCommentID") int photoCommentID) {
        try{
            int userIdxByJwt = jwtService.getUserIdx();
            if(userID != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            int checkCommentLike = likeProvider.checkCommentLike(userID, photoCommentID);
            if(checkCommentLike == 0) {
                likeService.createCommentLike(userID, photoCommentID);
                return new BaseResponse<>("댓글 좋아요 생성");
            }
            else{
                String commentlikeState = likeProvider.getCommentLikeState(userID, photoCommentID);
                if(commentlikeState.equals("INACTIVE")) {
                    likeService.doCommentLike(userID, photoCommentID);
                    return new BaseResponse<>("댓글 좋아요 생성");
                }
                else if (commentlikeState.equals("ACTIVE")) {
                    likeService.cancelCommentLike(userID, photoCommentID);
                    return new BaseResponse<>("댓글 좋아요 취소");
                }
                else{
                    return new BaseResponse<>(DATABASE_ERROR);
                }
            }
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 사진 좋아요 유저 목록 API
     * [GET] /app/likes/photos/:photoId
     */
    @ResponseBody
    @GetMapping("/photos/{photoID}")
    public BaseResponse<List<GetPhotoLikeUserRes>> getPhotoLikeUsers(@PathVariable("photoID") int photoID) {
        try{
            List<GetPhotoLikeUserRes> getPhotoLikeUserRes = likeProvider.getPhotoLikeUsers(photoID);
            return new BaseResponse<>(getPhotoLikeUserRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 사진 댓글 좋아요 유저 목록 API
     * [GET] /app/likes/photo-comments/:photoCommentId
     */
    @ResponseBody
    @GetMapping("/photo-comments/{photoCommentID}")
    public BaseResponse<List<GetPhotoLikeUserRes>> getPhotoCommentLikeUsers(@PathVariable("photoCommentID") int photoCommentID) {
        try{
            List<GetPhotoLikeUserRes> getPhotoLikeUserRes = likeProvider.getPhotoCommentLikeUsers(photoCommentID);
            return new BaseResponse<>(getPhotoLikeUserRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

}
