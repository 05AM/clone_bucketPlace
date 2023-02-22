package com.example.demo.src.follow;

import com.example.demo.config.BaseException;

import com.example.demo.src.follow.model.GetFollowerUserRes;
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
public class FollowProvider {

    private final FollowDao followDao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public FollowProvider(FollowDao followDao, JwtService jwtService) {
        this.followDao = followDao;
        this.jwtService = jwtService;
    }

    public List<GetFollowerUserRes> getUserFollowers(int userID) throws BaseException {
        try {
            List<GetFollowerUserRes> getFollowerUserRes = followDao.getUserFollowers(userID);
            return getFollowerUserRes;
        } catch (Exception exception) {
            logger.error("App - getUserFollowers Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetFollowerUserRes> getUserFollowings(int userID) throws BaseException {
        try {
            List<GetFollowerUserRes> getFollowerUserRes = followDao.getUserFollowings(userID);
            return getFollowerUserRes;
        } catch (Exception exception) {
            logger.error("App - getUserFollowings Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkFollow(int followerID, int followingID) throws BaseException{
        try{
            return followDao.checkFollow(followerID, followingID);
        } catch (Exception exception){
            logger.error("App - checkFollow Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public String getFollowState(int followerID, int followingID) throws BaseException{
        try {
            return followDao.getFollowState(followerID, followingID);
        } catch (Exception exception){
            logger.error("App - getFollowState Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
