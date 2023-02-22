package com.example.demo.src.point;


import com.example.demo.config.BaseException;
import com.example.demo.src.photo.model.PostPhotoCommentReq;
import com.example.demo.src.point.model.PostPointReq;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;


@Service
@Transactional(rollbackFor = Exception.class)
public class PointService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final PointDao pointDao;
    private final JwtService jwtService;


    @Autowired
    public PointService(PointDao pointDao, JwtService jwtService) {
        this.pointDao = pointDao;
        this.jwtService = jwtService;
    }

    public void createPoint(int userID, PostPointReq postPointReq){
        try{
            pointDao.createPoint(userID, postPointReq);
        } catch (Exception exception) {
            logger.error("App - createPoint Service Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
