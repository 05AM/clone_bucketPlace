package com.example.demo.src.like;


import com.example.demo.config.BaseException;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.demo.config.BaseResponseStatus.*;


@Service
@Transactional(rollbackFor = Exception.class)
public class LikeService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final LikeDao likeDao;
    private final JwtService jwtService;


    @Autowired
    public LikeService(LikeDao likeDao, JwtService jwtService) {
        this.likeDao = likeDao;
        this.jwtService = jwtService;

    }

    public void createLike(int userID, int photoID) throws BaseException {
        try{
            int likeId = likeDao.createLike(userID, photoID);
            if(likeId == 0){
                throw new BaseException(CREATE_FAIL_LIKE);
            }
        } catch (Exception exception) {
            logger.error("App - createLike Service Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void cancelLike(int userID, int photoID) throws BaseException {
        try{
            int result = likeDao.cancelLike(userID, photoID);
            if(result == 0){
                throw new BaseException(CANCEL_FAIL_LIKE);
            }
        } catch(Exception exception){
            logger.error("App - cancelLike Service Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void doLike(int userID, int photoID) throws BaseException {
        try{
            int result = likeDao.doLike(userID, photoID);
            if(result == 0){
                throw new BaseException(CREATE_FAIL_LIKE);
            }
        } catch(Exception exception){
            logger.error("App - doLike Service Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void createCommentLike(int userID, int photoCommentID) throws BaseException {
        try{
            int likeId = likeDao.createCommentLike(userID, photoCommentID);
            if(likeId == 0){
                throw new BaseException(CREATE_FAIL_COMMENT_LIKE);
            }
        } catch (Exception exception) {
            logger.error("App - createCommentLike Service Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void cancelCommentLike(int userID, int photoCommentID) throws BaseException {
        try{
            int result = likeDao.cancelCommentLike(userID, photoCommentID);
            if(result == 0){
                throw new BaseException(CANCEL_FAIL_COMMENT_LIKE);
            }
        } catch(Exception exception){
            logger.error("App - cancelCommentLike Service Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void doCommentLike(int userID, int photoCommentID) throws BaseException {
        try{
            int result = likeDao.doCommentLike(userID, photoCommentID);
            if(result == 0){
                throw new BaseException(CREATE_FAIL_COMMENT_LIKE);
            }
        } catch(Exception exception){
            logger.error("App - doCommentLike Service Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
