package com.example.demo.src.point;


import com.example.demo.config.BaseException;
import com.example.demo.src.point.model.GetPointRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
@Transactional(readOnly = true)
public class PointProvider {

    private final PointDao pointDao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public PointProvider(PointDao pointDao, JwtService jwtService) {
        this.pointDao = pointDao;
        this.jwtService = jwtService;
    }

    public GetPointRes getUserPoints(int userID) throws BaseException {
        try {
            GetPointRes getPointRes = pointDao.getUserPoints(userID);
            return getPointRes;
        } catch (Exception exception) {
            logger.error("App - getUserPoints Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
