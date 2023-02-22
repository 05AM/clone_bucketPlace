package com.example.demo.src.user;


import com.example.demo.src.scrap.model.PhotoForTotalScrap;
import com.example.demo.src.scrap.model.ProductForTotalScrap;
import com.example.demo.src.user.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UserDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public GetUserNoticeRes getUserNotice(int userID){
        String getUserNoticeQuery = "select isEmailNoticeAgree, isSMSNoticeAgree, isAppPushNoticeAgree, isLikeNoticeAgree, isScrapNoticeAgree, isCommentNoticeAgree, isMentionNoticeAgree, isCommentLikeNoticeAgree, isFollowerNoticeAgree, isFollowingNoticeAgree from user where userID = ?";
        return this.jdbcTemplate.queryForObject(getUserNoticeQuery,
                (rs, rowNum) -> new GetUserNoticeRes(
                        rs.getString("isEmailNoticeAgree"),
                        rs.getString("isSMSNoticeAgree"),
                        rs.getString("isAppPushNoticeAgree"),
                        rs.getString("isLikeNoticeAgree"),
                        rs.getString("isScrapNoticeAgree"),
                        rs.getString("isCommentNoticeAgree"),
                        rs.getString("isMentionNoticeAgree"),
                        rs.getString("isCommentLikeNoticeAgree"),
                        rs.getString("isFollowerNoticeAgree"),
                        rs.getString("isFollowingNoticeAgree")),
                userID);

    }

    public GetUserProfileModifyRes getUserProfileModify(int userID){
        String getProfileModifyQuery = "select profileImageLink, profileBackgroundImageLink, userNickname, myURL, introduction from user where userID = ?";
        return this.jdbcTemplate.queryForObject(getProfileModifyQuery,
                (rs, rowNum) -> new GetUserProfileModifyRes(
                      rs.getString("userNickname"),
                      rs.getString("profileImageLink"),
                      rs.getString("profileBackgroundImageLink"),
                      rs.getString("myURL"),
                      rs.getString("introduction")),
                userID);
    }

    public GetUserMyShoppingRes getUserMyShopping(int userID){
        String waitingForDepositQuery = "select count(orderStatus) from order_list where userID = ? AND orderStatus = '입금대기'";
        String paymentCompleteQuery = "select count(orderStatus) from order_list where userID = ? AND orderStatus = '결재완료'";
        String readyForDeliveryQuery = "select count(orderStatus) from order_list where userID = ? AND orderStatus = '배송준비'";
        String deliveryInProgressQuery = "select count(orderStatus) from order_list where userID = ? AND orderStatus = '배송중'";
        String deliveryCompleteQuery = "select count(orderStatus) from order_list where userID = ? AND orderStatus = '배송완료'";
        String writeReviewQuery = "select count(orderStatus) from order_list where userID = ? AND orderStatus = '리뷰쓰기'";
        int waitingForDeposit = jdbcTemplate.queryForObject(waitingForDepositQuery, int.class, userID);
        int paymentComplete = jdbcTemplate.queryForObject(paymentCompleteQuery, int.class, userID);
        int readyForDelivery = jdbcTemplate.queryForObject(readyForDeliveryQuery, int.class, userID);
        int deliveryInProgress = jdbcTemplate.queryForObject(deliveryInProgressQuery, int.class, userID);
        int deliveryComplete = jdbcTemplate.queryForObject(deliveryCompleteQuery, int.class, userID);
        int writeReview = jdbcTemplate.queryForObject(writeReviewQuery, int.class, userID);

        String getUserMyShoppingQuery = "select totalCoupon, totalPoint, ug.gradeName, count(distinct scrapID) as totalScrapbook, count(distinct inquiryID) as totalInquiry, count(distinct reviewID) as totalReview from user left outer join user_grade ug on user.gradeID = ug.gradeID left outer join user_scrap us on user.userID = us.userID AND us.state = 'ACTIVE' left outer join product_inquiry pi on user.userID = pi.userID AND pi.state = 'ACTIVE' left outer join review r on user.userID = r.userID AND r.state = 'ACTIVE' where user.userID = ?;";
        return this.jdbcTemplate.queryForObject(getUserMyShoppingQuery,
                (rs, rowNum) -> new GetUserMyShoppingRes(
                        rs.getInt("totalCoupon"),
                        rs.getInt("totalPoint"),
                        rs.getString("gradeName"),
                        waitingForDeposit,
                        paymentComplete,
                        readyForDelivery,
                        deliveryInProgress,
                        deliveryComplete,
                        writeReview,
                        rs.getInt("totalScrapbook"),
                        rs.getInt("totalInquiry"),
                        rs.getInt("totalReview")),
                userID);
    }

    public GetUserRes getUserProfile(int userID){
        String getUserPhotoQuery = "select distinct categoryPhoto, photoLink from photo where userID = ? AND photo.state = 'ACTIVE' AND (categoryPhoto, createAt) in (select categoryPhoto, max(createAt) from photo group by categoryPhoto) order by categoryPhoto";
        List<Photo> photos = jdbcTemplate.query(getUserPhotoQuery,
                (rs, rowNum) -> new Photo(
                        rs.getString("categoryPhoto"),
                        rs.getString("photoLink")),
                userID);

        String checkExistsPhotoQuery = "select exists(select photoLink from photo where userID = ? AND photo.state = 'ACTIVE')";
        Photo photo;
        if(this.jdbcTemplate.queryForObject(checkExistsPhotoQuery, int.class, userID) == 0) {
            photo = new Photo("전체", "");
        }
        else{
            String totalPhotoQuery = "select photoLink from photo where userID = ? AND photo.state = 'ACTIVE' order by createAt desc limit 1";
            String totalPhoto = jdbcTemplate.queryForObject(totalPhotoQuery, String.class, userID);
            photo = new Photo("전체", totalPhoto);
        }

        photos.add(photo);

        String getPhotoQuery = "select photoLink, us.createAt from photo inner join user_scrap us on photo.photoID = us.photoID where us.userID = ? AND us.state = 'ACTIVE' order by photo.createAt desc;";
        List<PhotoForTotalScrap> scrapPhotos = jdbcTemplate.query(getPhotoQuery,
                (rs, rowNum) -> new PhotoForTotalScrap(
                        "사진",
                        rs.getString("photoLink"),
                        rs.getTimestamp("createAt")),
                userID);

        String getProductQuery = "select mainImageLink, us.createAt from product_page inner join user_scrap us on product_page.productPageID = us.productPageID where us.userID = ? AND us.state = 'ACTIVE' order by product_page.createAt desc;";
        List<ProductForTotalScrap> products = jdbcTemplate.query(getProductQuery,
                (rs, rowNum) -> new ProductForTotalScrap(
                        "상품",
                        rs.getString("mainImageLink"),
                        rs.getTimestamp("createAt")),
                userID);

        List<Object> objectList = new ArrayList<>();
        int j = 0,k = 0;
        for(int i=0; i<scrapPhotos.size() + products.size(); i++){
            if(j >= scrapPhotos.size() || k >= products.size()) break;
            if(scrapPhotos.get(j).getCreateAt().after(products.get(k).getCreateAt())){
                objectList.add(scrapPhotos.get(j));
                j++;
            }
            else if(products.get(k).getCreateAt().after(scrapPhotos.get(j).getCreateAt())){
                objectList.add(products.get(k));
                k++;
            }
        }
        if(j < scrapPhotos.size()){
            while(j < scrapPhotos.size()){
                objectList.add(scrapPhotos.get(j));
                j++;
            }
        }
        if(k < products.size()){
            while(k < products.size()){
                objectList.add(products.get(k));
                k++;
            }
        }

        String getUserQuery = "select userNickname, profileImageLink, cntFollower, cntFollowing, count(distinct scrapID) as totalScrapbook, totalCoupon, totalPoint, count(distinct ol.orderListID) as totalOrder, count(distinct pl.likeID) as totalLike, count(distinct reviewID) as totalReview from user left outer join user_scrap us on user.userID = us.userID AND us.state = 'ACTIVE' left outer join order_list ol on user.userID = ol.userID AND orderStatus != '배송완료' AND orderStatus != '리뷰쓰기' AND ol.state = 'ACTIVE' left outer join photo_like pl on user.userID = pl.userID AND pl.state = 'ACTIVE' left outer join review r on user.userID = r.userID  AND r.state = 'ACTIVE' where user.userID = ?;";
        int getUserParams = userID;
        return this.jdbcTemplate.queryForObject(getUserQuery,
                (rs, rowNum) -> new GetUserRes(
                        rs.getString("userNickname"),
                        rs.getString("profileImageLink"),
                        rs.getInt("cntFollower"),
                        rs.getInt("cntFollowing"),
                        rs.getInt("totalScrapbook"),
                        rs.getInt("totalCoupon"),
                        rs.getInt("totalPoint"),
                        rs.getInt("totalOrder"),
                        rs.getInt("totalLike"),
                        rs.getInt("totalReview"),
                        photos.toArray(new Photo[photos.size()]),
                        objectList.toArray(new Object[objectList.size()])),
                getUserParams);
    }
    

    public int createUser(PostUserReq postUserReq){
        String createUserQuery = "insert into UserInfo (userName, ID, password, email) VALUES (?,?,?,?)";
        Object[] createUserParams = new Object[]{postUserReq.getUserName(), postUserReq.getId(), postUserReq.getPassword(), postUserReq.getEmail()};
        this.jdbcTemplate.update(createUserQuery, createUserParams);

        String lastInserIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInserIdQuery,int.class);
    }

    public int checkUserNickname(String nickname){
        String checkNicknameQuery = "select exists(select userNickname from user where userNickname = ?)";
        String checkNicknameParams = nickname;
        return this.jdbcTemplate.queryForObject(checkNicknameQuery,
                int.class,
                checkNicknameParams);
    }

    public int modifyUserProfile(PatchUserReq patchUserReq){
        String modifyUserProfileQuery = "update user set userNickname = ?, profileImageLink = ?, profileBackgroundImageLink = ?, myURL = ?, introduction = ? where userID = ? ";
        Object[] modifyUserProfileParams = new Object[]{patchUserReq.getUserNickname(), patchUserReq.getProfileImageLink(), patchUserReq.getProfileBackgroundImageLink(), patchUserReq.getMyURL(), patchUserReq.getIntroduction(), patchUserReq.getUserID()};

        return this.jdbcTemplate.update(modifyUserProfileQuery, modifyUserProfileParams);
    }

    public int modifyUserNotice(PatchUserReq req){
        String modifyUserNoticeQuery = "update user set isEmailNoticeAgree = ?, isSMSNoticeAgree = ?, isAppPushNoticeAgree = ?, isLikeNoticeAgree = ?, isScrapNoticeAgree = ?, isCommentNoticeAgree = ?, isMentionNoticeAgree = ?, isCommentLikeNoticeAgree = ?, isFollowerNoticeAgree = ?, isFollowingNoticeAgree = ? where userID = ? ";
        Object[] modifyUserNoticeParams = new Object[]{req.getIsEmailNoticeAgree(), req.getIsSMSNoticeAgree(), req.getIsAppPushNoticeAgree(), req.getIsLikeNoticeAgree(), req.getIsScrapNoticeAgree(), req.getIsCommentNoticeAgree(), req.getIsMentionNoticeAgree(), req.getIsCommentLikeNoticeAgree(), req.getIsFollowerNoticeAgree(), req.getIsFollowingNoticeAgree(), req.getUserID()};

        return this.jdbcTemplate.update(modifyUserNoticeQuery, modifyUserNoticeParams);
    }

    /*public User getPwd(PostLoginReq postLoginReq){
        String getPwdQuery = "select userIdx, password,email,userName,ID from UserInfo where ID = ?";
        String getPwdParams = postLoginReq.getId();

        return this.jdbcTemplate.queryForObject(getPwdQuery,
                (rs,rowNum)-> new User(
                        rs.getInt("userIdx"),
                        rs.getString("ID"),
                        rs.getString("userName"),
                        rs.getString("password"),
                        rs.getString("email")
                ),
                getPwdParams
                );
    }*/

    public int leaveUser(int userID){
        String leaveUserQuery = "update user set state = 'INACTIVE' where userID = ?";
        return this.jdbcTemplate.update(leaveUserQuery, userID);
    }
}
