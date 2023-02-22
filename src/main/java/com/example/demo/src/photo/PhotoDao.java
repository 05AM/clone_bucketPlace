package com.example.demo.src.photo;


import com.example.demo.src.like.model.GetPhotoLikeUserRes;
import com.example.demo.src.photo.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class PhotoDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public GetPhotosByCategoryRes getPhotosAll(int userID){
        String getPhotoQuery = "select photoID, content, photoLink from photo inner join user u on photo.userID = u.userID where photo.userID = ? AND photo.state = 'ACTIVE' order by photo.createAt desc;";
        List<PhotoForGetPhotosByCategoryRes> photos = jdbcTemplate.query(getPhotoQuery,
                (rs, rowNum) -> new PhotoForGetPhotosByCategoryRes(
                        rs.getInt("photoID"),
                        rs.getString("content"),
                        rs.getString("photoLink")),
                userID);

        for(int i=0; i<photos.size(); i++){
            String getTotalLikes = "select count(likeID) from photo_like inner join photo p on photo_like.photoID = p.photoID where p.photoID = ?;";
            photos.get(i).setTotalLikes(this.jdbcTemplate.queryForObject(getTotalLikes, int.class, photos.get(i).getPhotoID()));

            String getTotalScraps = "select count(scrapID) from user_scrap inner join photo p on user_scrap.photoID = p.photoID where p.photoID = ?;";
            photos.get(i).setTotalScraps(this.jdbcTemplate.queryForObject(getTotalScraps, int.class, photos.get(i).getPhotoID()));

            String getTotalComments = "select count(commentID) from photo_comment inner join photo p on photo_comment.photoID = p.photoID where p.photoID = ?;";
            photos.get(i).setTotalComments(this.jdbcTemplate.queryForObject(getTotalComments, int.class, photos.get(i).getPhotoID()));
        }

        String getUserInfoQuery = "select profileImageLink, userNickname from user where userID = ?;";
        return this.jdbcTemplate.queryForObject(getUserInfoQuery,
                (rs, rowNum) -> new GetPhotosByCategoryRes(
                        rs.getString("profileImageLink"),
                        rs.getString("userNickname"),
                        photos.toArray(new PhotoForGetPhotosByCategoryRes[photos.size()])),
                userID);
    }

    public GetPhotosByCategoryRes getPhotosByCategory(int userID, String photoCategory){
        String getPhotoQuery = "select photoID, photoLink, content from photo inner join user u on photo.userID = u.userID where photo.userID = ? AND photo.state = 'ACTIVE' AND categoryPhoto = ? order by photo.createAt desc;";
        List<PhotoForGetPhotosByCategoryRes> photos = jdbcTemplate.query(getPhotoQuery,
                (rs, rowNum) -> new PhotoForGetPhotosByCategoryRes(
                        rs.getInt("photoID"),
                        rs.getString("content"),
                        rs.getString("photoLink")),
                userID, photoCategory);

        for(int i=0; i<photos.size(); i++){
            String getTotalLikes = "select count(likeID) from photo_like inner join photo p on photo_like.photoID = p.photoID where p.photoID = ?;";
            photos.get(i).setTotalLikes(this.jdbcTemplate.queryForObject(getTotalLikes, int.class, photos.get(i).getPhotoID()));

            String getTotalScraps = "select count(scrapID) from user_scrap inner join photo p on user_scrap.photoID = p.photoID where p.photoID = ?;";
            photos.get(i).setTotalScraps(this.jdbcTemplate.queryForObject(getTotalScraps, int.class, photos.get(i).getPhotoID()));

            String getTotalComments = "select count(commentID) from photo_comment inner join photo p on photo_comment.photoID = p.photoID where p.photoID = ?;";
            photos.get(i).setTotalComments(this.jdbcTemplate.queryForObject(getTotalComments, int.class, photos.get(i).getPhotoID()));
        }

        String getUserInfoQuery = "select profileImageLink, userNickname from user where userID = ?;";
        return this.jdbcTemplate.queryForObject(getUserInfoQuery,
                (rs, rowNum) -> new GetPhotosByCategoryRes(
                        rs.getString("profileImageLink"),
                        rs.getString("userNickname"),
                        photos.toArray(new PhotoForGetPhotosByCategoryRes[photos.size()])),
                userID);
    }

    public GetPhotoInfoRes getPhotoInfo(int photoID, int userID){
        String getPhotoHashTag = "select keyword from photo_hashtag where photoID = ? AND state = 'ACTIVE';";
        List<PhotoHashTag> hashTagList = this.jdbcTemplate.query(getPhotoHashTag,
                (rs, rowNum) -> new PhotoHashTag(rs.getString("keyword")), photoID);

        String getPhotoQuery = "select spaceSize, dwellingType, style, photoLink, content from photo where photoID = ? order by createAt desc;";
        PhotoForGetPhotoInfo photo = jdbcTemplate.queryForObject(getPhotoQuery,
                (rs, rowNum) -> new PhotoForGetPhotoInfo(
                        rs.getString("spaceSize"),
                        rs.getString("dwellingType"),
                        rs.getString("style"),
                        rs.getString("photoLink"),
                        rs.getString("content"),
                        hashTagList.toArray(new PhotoHashTag[hashTagList.size()])),
                photoID);

        String getTotalLikes = "select count(likeID) from photo_like inner join photo p on photo_like.photoID = p.photoID where p.photoID = ?;";
        photo.setTotalLikes(this.jdbcTemplate.queryForObject(getTotalLikes, int.class, photoID));

        String getTotalScraps = "select count(scrapID) from user_scrap inner join photo p on user_scrap.photoID = p.photoID where p.photoID = ?;";
        photo.setTotalScraps(this.jdbcTemplate.queryForObject(getTotalScraps, int.class, photoID));

        String getTotalComments = "select count(commentID) from photo_comment inner join photo p on photo_comment.photoID = p.photoID where p.photoID = ?;";
        photo.setTotalComments(this.jdbcTemplate.queryForObject(getTotalComments, int.class, photoID));

        String getUserInfoQuery = "select profileImageLink, userNickname, introduction from user where userID = ?;";
        return this.jdbcTemplate.queryForObject(getUserInfoQuery,
                (rs, rowNum) -> new GetPhotoInfoRes(
                        rs.getString("profileImageLink"),
                        rs.getString("userNickname"),
                        rs.getString("introduction"),
                        photo), userID);
    }

    public void createPhoto(int userID, PostCreatePhotoReq photoReq) {
        String createPhotoQuery = "insert into photo (spaceSize, dwellingType, style, photoLink, categoryPhoto, content, userID) VALUES (?,?,?,?,?,?,?);";
        Object[] createPhotoParams = new Object[]{photoReq.getSpaceSize(), photoReq.getDwellingType(), photoReq.getStyle(), photoReq.getPhotoLink(), photoReq.getCategoryPhoto(), photoReq.getContent(), userID};
        this.jdbcTemplate.update(createPhotoQuery, createPhotoParams);

        String lastInserIdQuery = "select last_insert_id()";
        int photoId =  this.jdbcTemplate.queryForObject(lastInserIdQuery,int.class);

        for(int i=0; i<photoReq.getKeyword().length; i++){
            System.out.println("photoReq = " + photoReq.getKeyword()[i]);
        }

        for(int i=0; i<photoReq.getKeyword().length; i++){
            String createHashTagQuery = "insert into photo_hashtag (keyword, photoID) VALUES(?,?)";
            Object[] createHashTagParams = new Object[]{photoReq.getKeyword()[i], photoId};
            this.jdbcTemplate.update(createHashTagQuery, createHashTagParams);
        }

    }

    public void createPhotoComment(int photoID, int userID, PostPhotoCommentReq postPhotoCommentReq) {
        String createPhotoCommentQuery = "insert into photo_comment (photoID, userID, content) VALUES (?,?,?);";
        Object[] createPhotoCommentParams = new Object[]{photoID, userID, postPhotoCommentReq.getContent()};
        this.jdbcTemplate.update(createPhotoCommentQuery, createPhotoCommentParams);
    }

    public List<GetPhotoCommentUserRes> getPhotoCommentUsers(int photoID){
        String getUserInfoQuery = "select userNickname from user inner join photo_comment pc on user.userID = pc.userID where photoID = ? AND user.state = 'ACTIVE';";
        return this.jdbcTemplate.query(getUserInfoQuery,
                (rs,rowNum) -> new GetPhotoCommentUserRes(
                        rs.getString("userNickname")),
                photoID);
    }

    public List<GetPhotoCommentRes> getPhotoComments(int photoID){
        String getPhotoCommentsQuery = "select commentID ,profileImageLink, userNickname, content, pc.createAt as createAt from user inner join photo_comment pc on user.userID = pc.userID where photoID = ? AND pc.state = 'ACTIVE' order by createAt desc;";
        List<GetPhotoCommentRes> getPhotoCommentResList = this.jdbcTemplate.query(getPhotoCommentsQuery,
                (rs,rowNum) -> new GetPhotoCommentRes(
                        rs.getInt("commentID"),
                        rs.getString("profileImageLink"),
                        rs.getString("userNickname"),
                        rs.getString("content"),
                        rs.getString("createAt")),
                photoID);

        for(int i=0; i<getPhotoCommentResList.size(); i++){
            String getTotalCommentLikeQuery = "select count(commentID) from photo_comment_like where commentID = ?;";
            getPhotoCommentResList.get(i).setTotalLikes(this.jdbcTemplate.queryForObject(getTotalCommentLikeQuery, int.class, getPhotoCommentResList.get(i).getCommentID()));
        }
        return getPhotoCommentResList;
    }

    public List<GetTop10PhotosRes> getTop10Photos(){
        String getTop10PhotosQuery = "select photoLink, userNickname from photo inner join user u on photo.userID = u.userID order by hit desc limit 10;";
        return this.jdbcTemplate.query(getTop10PhotosQuery,
                (rs,rowNum) -> new GetTop10PhotosRes(
                        rs.getString("photoLink"),
                        rs.getString("userNickname")));
    }
}
