package com.example.demo.src.photo;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.like.model.GetPhotoLikeUserRes;
import com.example.demo.src.photo.model.*;
import com.example.demo.src.scrap.model.PostScrapFolderReq;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@RestController
@RequestMapping("/app/photos")
public class PhotoController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final PhotoProvider photoProvider;
    @Autowired
    private final PhotoService photoService;
    @Autowired
    private final JwtService jwtService;

    public PhotoController(PhotoProvider photoProvider, PhotoService photoService, JwtService jwtService){
        this.photoProvider = photoProvider;
        this.photoService = photoService;
        this.jwtService = jwtService;
    }

    /**
     * 카테고리별 사진 조회 API
     * [GET] /app/photos/users/:userId?photoCategory={photoCategory}
     */
    @ResponseBody
    @GetMapping("/users/{userID}")
    public BaseResponse<GetPhotosByCategoryRes> getPhotosByCategory(@PathVariable("userID") int userID, @RequestParam("photoCategory") String photoCategory) {
        try{
            GetPhotosByCategoryRes getPhotosByCategoryRes = photoProvider.getPhotosByCategory(userID, photoCategory);
            return new BaseResponse<>(getPhotosByCategoryRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 특정 사진 조회 API
     * [GET] /app/photos/photoId/users/:userId
     */
    @ResponseBody
    @GetMapping("/{photoID}/users/{userID}")
    public BaseResponse<GetPhotoInfoRes> getPhotoInfo(@PathVariable("photoID") int photoID, @PathVariable("userID") int userID){
        try{
            GetPhotoInfoRes getPhotoInfoRes = photoProvider.getPhotoInfo(photoID, userID);
            return new BaseResponse<>(getPhotoInfoRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 사진 올리기 API
     * [POST] /app/photos/users/:userId
     */
    @ResponseBody
    @PostMapping("/users/{userID}")
    public BaseResponse<String> createPhoto(@PathVariable("userID") int userID, @RequestBody PostCreatePhotoReq postCreatePhotoReq) {
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            if (userID != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            if (postCreatePhotoReq.getPhotoLink() == null) {
                return new BaseResponse<>(POST_PHOTO_EMPTY_PHOTOLINK);
            }
            if (postCreatePhotoReq.getCategoryPhoto() == null) {
                return new BaseResponse<>(POST_PHOTO_EMPTY_CATEGORYPHOTO);
            }
            photoService.createPhoto(userID, postCreatePhotoReq);
            return new BaseResponse<>();
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 사진 올리기 API
     * [POST] /app/photos/users/:userId
     */
    @ResponseBody
    @PostMapping("/{photoID}/comments/users/{userID}")
    public BaseResponse<String> createPhotoComment(@PathVariable("userID") int userID, @PathVariable("photoID") int photoID , @RequestBody PostPhotoCommentReq postPhotoCommentReq) {
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            if (userID != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            if (postPhotoCommentReq.getContent() == null) {
                return new BaseResponse<>(POST_PHOTO_COMMENT_EMPTY_CONTENT);
            }
            photoService.createPhotoComment(photoID, userID, postPhotoCommentReq);
            return new BaseResponse<>();
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 사진 댓글 유저 목록 API
     * [GET] /app/photos/{photoId}/comments-users
     */
    @ResponseBody
    @GetMapping("/{photoID}/comments-users")
    public BaseResponse<List<GetPhotoCommentUserRes>> getPhotoCommentUsers(@PathVariable("photoID") int photoID) {
        try{
            List<GetPhotoCommentUserRes> getPhotoCommentUserRes = photoProvider.getPhotoCommentUsers(photoID);
            return new BaseResponse<>(getPhotoCommentUserRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 사진 댓글 목록 API
     * [GET] /app/photos/{photoId}/comments
     *
     */
    @ResponseBody
    @GetMapping("/{photoID}/comments")
    public BaseResponse<List<GetPhotoCommentRes>> getPhotoComments(@PathVariable("photoID") int photoID) {
        try{
            List<GetPhotoCommentRes> getPhotoCommentRes = photoProvider.getPhotoComments(photoID);
            return new BaseResponse<>(getPhotoCommentRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 인기 사진 top10 API
     * [GET] /app/photos/top10
     *
     */
    @ResponseBody
    @GetMapping("/top10")
    public BaseResponse<List<GetTop10PhotosRes>> getTop10Photos() {
        try{
            List<GetTop10PhotosRes> getTop10PhotosRes = photoProvider.getTop10Photos();
            return new BaseResponse<>(getTop10PhotosRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
