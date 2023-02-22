package com.example.demo.src.photo;


import com.example.demo.config.BaseException;
import com.example.demo.src.photo.model.PostCreatePhotoReq;
import com.example.demo.src.photo.model.PostPhotoCommentReq;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;


@Service
@Transactional(rollbackFor = Exception.class)
public class PhotoService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final PhotoDao photoDao;
    private final JwtService jwtService;


    @Autowired
    public PhotoService(PhotoDao photoDao, JwtService jwtService) {
        this.photoDao = photoDao;
        this.jwtService = jwtService;
    }

    public void createPhoto(int userID, PostCreatePhotoReq postCreatePhotoReq){
        try{
            photoDao.createPhoto(userID, postCreatePhotoReq);
        } catch (Exception exception) {
            logger.error("App - createPhoto Service Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void createPhotoComment(int photoID, int userID, PostPhotoCommentReq postPhotoCommentReq){
        try{
            photoDao.createPhotoComment(photoID, userID, postPhotoCommentReq);
        } catch (Exception exception) {
            logger.error("App - createPhotoComment Service Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
