package com.example.demo.src.photo;


import com.example.demo.config.BaseException;
import com.example.demo.src.like.model.GetPhotoLikeUserRes;
import com.example.demo.src.photo.model.*;
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
public class PhotoProvider {

    private final PhotoDao photoDao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public PhotoProvider(PhotoDao photoDao, JwtService jwtService) {
        this.photoDao = photoDao;
        this.jwtService = jwtService;
    }

    public GetPhotosByCategoryRes getPhotosByCategory(int userID, String photoCategory) throws BaseException {
        try {
            GetPhotosByCategoryRes getPhotosByCategoryRes;
            if(photoCategory.equals("전체"))
                getPhotosByCategoryRes = photoDao.getPhotosAll(userID);
            else
                getPhotosByCategoryRes = photoDao.getPhotosByCategory(userID, photoCategory);

            return getPhotosByCategoryRes;
        } catch (Exception exception) {
            logger.error("App - getPhotosByCategory Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public GetPhotoInfoRes getPhotoInfo(int photoID, int userID) throws BaseException {
        try {
            GetPhotoInfoRes getPhotoInfoRes = photoDao.getPhotoInfo(photoID, userID);
            return getPhotoInfoRes;
        } catch (Exception exception){
            logger.error("App - getPhotoInfo Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetPhotoCommentUserRes> getPhotoCommentUsers(int photoID) throws BaseException {
        try {
            List<GetPhotoCommentUserRes> getPhotoCommentUserRes = photoDao.getPhotoCommentUsers(photoID);
            return getPhotoCommentUserRes;
        } catch (Exception exception) {
            logger.error("App - getPhotoCommentUsers Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetPhotoCommentRes> getPhotoComments(int photoID) throws BaseException {
        try {
            List<GetPhotoCommentRes> getPhotoCommentRes = photoDao.getPhotoComments(photoID);
            return getPhotoCommentRes;
        } catch (Exception exception) {
            logger.error("App - getPhotoComments Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetTop10PhotosRes> getTop10Photos() throws BaseException {
        try {
            List<GetTop10PhotosRes> getTop10PhotosRes = photoDao.getTop10Photos();
            return getTop10PhotosRes;
        } catch (Exception exception) {
            logger.error("App - getTop10Photos Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
