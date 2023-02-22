package com.example.demo.src.like;


import com.example.demo.config.BaseException;
import com.example.demo.src.like.model.GetLikeRes;
import com.example.demo.src.like.model.GetPhotoLikeUserRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
@Transactional(readOnly = true)
public class LikeProvider {

    private final LikeDao likeDao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public LikeProvider(LikeDao likeDao, JwtService jwtService) {
        this.likeDao = likeDao;
        this.jwtService = jwtService;
    }

    public GetLikeRes getUserLikes(int userID) throws BaseException {
        try {
            GetLikeRes getLikeRes = likeDao.getUserLikes(userID);
            return getLikeRes;
        } catch (Exception exception) {
            logger.error("App - getUserLikes Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkLike(int userID, int photoID) throws BaseException{
        try{
            return likeDao.checkLike(userID, photoID);
        } catch (Exception exception){
            logger.error("App - checkLike Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public String getLikeState(int userID, int photoID) throws BaseException{
        try {
            return likeDao.getLikeState(userID, photoID);
        } catch (Exception exception){
            logger.error("App - getLikeState Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkCommentLike(int userID, int photoCommentID) throws BaseException{
        try{
            return likeDao.checkCommentLike(userID, photoCommentID);
        } catch (Exception exception){
            logger.error("App - checkCommentLike Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public String getCommentLikeState(int userID, int photoCommentID) throws BaseException{
        try {
            return likeDao.getCommentLikeState(userID, photoCommentID);
        } catch (Exception exception){
            logger.error("App - getCommentLikeState Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetPhotoLikeUserRes> getPhotoLikeUsers(int photoID) throws BaseException {
        try {
            List<GetPhotoLikeUserRes> getPhotoLikeUserRes = likeDao.getPhotoLikeUsers(photoID);
            return getPhotoLikeUserRes;
        } catch (Exception exception) {
            logger.error("App - getPhotoLikeUsers Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetPhotoLikeUserRes> getPhotoCommentLikeUsers(int photoCommentID) throws BaseException {
        try {
            List<GetPhotoLikeUserRes> getPhotoLikeUserRes = likeDao.getPhotoCommentLikeUsers(photoCommentID);
            return getPhotoLikeUserRes;
        } catch (Exception exception) {
            logger.error("App - getPhotoCommentLikeUsers Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
