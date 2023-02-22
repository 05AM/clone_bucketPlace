package com.example.demo.src.follow;


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
public class FollowService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final FollowDao followDao;
    private final JwtService jwtService;


    @Autowired
    public FollowService(FollowDao followDao, JwtService jwtService) {
        this.followDao = followDao;
        this.jwtService = jwtService;
    }

    public void createFollow(int followerID, int followingID) throws BaseException {
        try{
            int followId = followDao.createFollow(followerID, followingID);
            if(followId == 0){
                throw new BaseException(CREATE_FAIL_LIKE);
            }
        } catch (Exception exception) {
            logger.error("App - createFollow Service Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void follow(int followerID, int followingID) throws BaseException {
        try{
            int result = followDao.follow(followerID, followingID);
            if(result == 0){
                throw new BaseException(CREATE_FAIL_FOLLOW);
            }
        } catch(Exception exception){
            logger.error("App - follow Service Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void unFollow(int followerID, int followingID) throws BaseException {
        try{
            int result = followDao.unFollow(followerID, followingID);
            if(result == 0){
                throw new BaseException(CANCEL_FAIL_FOLLOW);
            }
        } catch(Exception exception){
            logger.error("App - unFollow Service Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
