package com.example.demo.src.like;


import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.like.model.GetLikeRes;
import com.example.demo.src.like.model.GetPhotoLikeUserRes;
import com.example.demo.src.like.model.Photo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;
import static com.example.demo.config.BaseResponseStatus.INVALID_USER_JWT;

@Repository
public class LikeDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public GetLikeRes getUserLikes(int userID){
        String checkPhotoQuery = "select exists(select photoLink from photo left outer join photo_like pl on photo.photoID = pl.photoID where pl.userID = ? AND pl.state = 'ACTIVE');";
        List<Photo> photos;
       if(this.jdbcTemplate.queryForObject(checkPhotoQuery, int.class, userID) == 0){
           Photo photo = new Photo("콘텐츠가 없습니다");
           photos = new ArrayList<>();
           photos.add(photo);
       }
       else{
           String getPhotoQuery = "select photoLink from photo left outer join photo_like pl on photo.photoID = pl.photoID where pl.userID = ? AND pl.state = 'ACTIVE' order by pl.createAt desc;";
           photos = jdbcTemplate.query(getPhotoQuery,
                   (rs, rowNum) -> new Photo(
                           rs.getString("photoLink")),
                   userID);
       }

        String getLikeQuery = "select count(distinct photoLink) as totalPhoto from photo left outer join photo_like pl on photo.photoID = pl.photoID where pl.userID = ? AND pl.state = 'ACTIVE';";

        return this.jdbcTemplate.queryForObject(getLikeQuery,
                (rs, rowNum) -> new GetLikeRes(
                        rs.getInt("totalPhoto"),
                        photos.toArray(new Photo[photos.size()])),
                userID);
    }

    public int checkLike(int userID, int photoID){
        String checkLikeQuery = "select exists(select likeID from photo_like where userID = ? and photoID = ?);";
        return this.jdbcTemplate.queryForObject(checkLikeQuery,
                int.class,
                userID, photoID);
    }

    public String getLikeState(int userID, int photoID){
        String getLikeStateQuery = "select state from photo_like where userID = ? and photoID = ?;";
        return this.jdbcTemplate.queryForObject(getLikeStateQuery, String.class, userID, photoID);
    }

    public int createLike(int userID, int photoID){
        String createLikeQuery = "insert into photo_like (userID, photoID) VALUES (?,?);";
        Object[] createLikeParams = new Object[]{userID, photoID};
        this.jdbcTemplate.update(createLikeQuery, createLikeParams);

        String lastInserIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInserIdQuery, int.class);
    }

    public int cancelLike(int userID, int photoID){
        String cancelLikeQuery = "update photo_like set state = 'INACTIVE' where userId = ? and photoID = ?;";
        Object[] cancelLikeParams = new Object[]{userID, photoID};
        return this.jdbcTemplate.update(cancelLikeQuery, cancelLikeParams);
    }

    public int doLike(int userID, int photoID){
        String doLikeQuery = "update photo_like set state = 'ACTIVE' where userId = ? and photoID = ?;";
        Object[] doLikeParams = new Object[]{userID, photoID};
        return this.jdbcTemplate.update(doLikeQuery, doLikeParams);
    }

    public int checkCommentLike(int userID, int photoCommentID){
        String checkCommentLikeQuery = "select exists(select likeID from photo_comment_like where userID = ? and commentID = ?);";
        return this.jdbcTemplate.queryForObject(checkCommentLikeQuery, int.class, userID, photoCommentID);
    }

    public String getCommentLikeState(int userID, int photoCommentID){
        String getCommentLikeStateQuery = "select state from photo_comment_like where userID = ? and commentID = ?;";
        return this.jdbcTemplate.queryForObject(getCommentLikeStateQuery, String.class, userID, photoCommentID);
    }

    public int createCommentLike(int userID, int photoCommentID){
        String createCommentLikeQuery = "insert into photo_comment_like (userID, commentID) VALUES (?,?);";
        Object[] createCommentLikeParams = new Object[]{userID, photoCommentID};
        this.jdbcTemplate.update(createCommentLikeQuery, createCommentLikeParams);

        String lastInserIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInserIdQuery, int.class);
    }

    public int cancelCommentLike(int userID, int photoCommentID){
        String cancelCommentLikeQuery = "update photo_comment_like set state = 'INACTIVE' where userID = ? and commentID = ?;";
        Object[] cancelCommentLikeParams = new Object[]{userID, photoCommentID};
        return this.jdbcTemplate.update(cancelCommentLikeQuery, cancelCommentLikeParams);
    }

    public int doCommentLike(int userID, int photoCommentID){
        String doCommentLikeQuery = "update photo_comment_like set state = 'ACTIVE' where userID = ? and commentID = ?;";
        Object[] doCommentLikeParams = new Object[]{userID, photoCommentID};
        return this.jdbcTemplate.update(doCommentLikeQuery, doCommentLikeParams);
    }

    public List<GetPhotoLikeUserRes> getPhotoLikeUsers(int photoID){
        String getUserInfoQuery = "select profileImageLink, userNickname from user inner join photo_like pl on user.userID = pl.userID where photoID = ? AND user.state = 'ACTIVE' order by pl.createAt desc;";
        return this.jdbcTemplate.query(getUserInfoQuery,
                (rs,rowNum) -> new GetPhotoLikeUserRes(
                        rs.getString("profileImageLink"),
                        rs.getString("userNickname")
                ), photoID);
    }

    public List<GetPhotoLikeUserRes> getPhotoCommentLikeUsers(int photoCommentID){
        String getUserInfoQuery = "select profileImageLink, userNickname from user inner join photo_comment_like pcl on user.userID = pcl.userID where commentID = ? AND user.state = 'ACTIVE' order by pcl.createAt desc;";
        return this.jdbcTemplate.query(getUserInfoQuery,
                (rs,rowNum) -> new GetPhotoLikeUserRes(
                        rs.getString("profileImageLink"),
                        rs.getString("userNickname")
                ), photoCommentID);
    }
}
