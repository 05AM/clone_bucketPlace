package com.example.demo.src.follow;


import com.example.demo.src.follow.model.GetFollowerUserRes;
import com.example.demo.src.like.model.GetLikeRes;
import com.example.demo.src.like.model.GetPhotoLikeUserRes;
import com.example.demo.src.like.model.Photo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository
public class FollowDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetFollowerUserRes> getUserFollowers(int userID){
        String getFollowerUserQuery = "select profileImageLink, userNickname, introduction from user where userID in (select followerID from user_follow where followingID = ?);";
        return this.jdbcTemplate.query(getFollowerUserQuery,
                (rs, rowNum) -> new GetFollowerUserRes(
                        rs.getString("profileImageLink"),
                        rs.getString("userNickname"),
                        rs.getString("introduction")),
                userID);
    }

    public List<GetFollowerUserRes> getUserFollowings(int userID){
        String getFollowerUserQuery = "select profileImageLink, userNickname, introduction from user where userID in (select followingID from user_follow where followerID = ?);";
        return this.jdbcTemplate.query(getFollowerUserQuery,
                (rs, rowNum) -> new GetFollowerUserRes(
                        rs.getString("profileImageLink"),
                        rs.getString("userNickname"),
                        rs.getString("introduction")),
                userID);
    }

    public int checkFollow(int followerID, int followingID){
        String checkFollowQuery = "select exists(select followID from user_follow where followerID = ? and followingID = ?);";
        return this.jdbcTemplate.queryForObject(checkFollowQuery,
                int.class,
                followerID, followingID);
    }

    public int createFollow(int followerID, int followingID){
        String createFollowQuery = "insert into user_follow (followerID, followingID) VALUES (?,?);";
        Object[] createFollowParams = new Object[]{followerID, followingID};
        this.jdbcTemplate.update(createFollowQuery, createFollowParams);

        String lastInserIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInserIdQuery, int.class);
    }

    public String getFollowState(int followerID, int followingID){
        String getFollowStateQuery = "select state from user_follow where followerID = ? and followingID = ?;";
        return this.jdbcTemplate.queryForObject(getFollowStateQuery, String.class, followerID, followingID);
    }

    public int follow(int followerID, int followingID){
        String followQuery = "update user_follow set state = 'ACTIVE' where followerID = ? and followingID = ?;";
        Object[] followParams = new Object[]{followerID, followingID};
        return this.jdbcTemplate.update(followQuery, followParams);
    }

    public int unFollow(int followerID, int followingID){
        String unFollowQuery = "update user_follow set state = 'INACTIVE' where followerID = ? and followingID = ?;";
        Object[] unFollowParams = new Object[]{followerID, followingID};
        return this.jdbcTemplate.update(unFollowQuery, unFollowParams);
    }
}
