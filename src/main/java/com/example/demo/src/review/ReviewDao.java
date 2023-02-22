package com.example.demo.src.review;

import com.example.demo.src.review.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class ReviewDao {
    private JdbcTemplate jdbcTemplate;
    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public int checkReviewDuplication(int orderListID) {
        String checkReviewDuplicationQuery = "select exists(select reviewID from review where orderListID = ?)";
        int checkReviewDuplicationParam = orderListID;

        return this.jdbcTemplate.queryForObject(checkReviewDuplicationQuery, int.class, checkReviewDuplicationParam);
    }

    public int createReview(PostReviewReq postReviewReq) {
        String createReviewQuery = "insert into review " +
                "(rate, reviewImageLink, content, userID, orderListID, productPageID, brandID) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        Object[] createReviewParams = new Object[]{
                postReviewReq.getRate(),
                postReviewReq.getReviewImageLink(),
                postReviewReq.getContent(),
                postReviewReq.getUserID(),
                postReviewReq.getOrderListID(),
                postReviewReq.getProductPageID(),
                postReviewReq.getBrandID()
        };

        this.jdbcTemplate.update(createReviewQuery, createReviewParams);

        String lastInserIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInserIdQuery,int.class);
    }

    public List<GetReviewRes> getUserReviews(int userID) {
        String getUserReviewsQuery = "select * from review where userID = ? and state = 'ACTIVE'";
        int getUserReviewsParam = userID;

        return this.jdbcTemplate.query(getUserReviewsQuery,
                (rs, rownum) -> new GetReviewRes(
                        rs.getInt("reviewID"),
                        rs.getInt("userID"),
                        rs.getInt("orderListID"),
                        rs.getInt("productPageID"),
                        rs.getInt("brandID"),
                        rs.getFloat("rate"),
                        rs.getString("reviewImageLink"),
                        rs.getString("content"),
                        rs.getInt("itHelps"),
                        rs.getString("createAt"),
                        rs.getString("updateAt"),
                        rs.getString("state")
                ),
                getUserReviewsParam);
    }

    public List<GetReviewRes> getProductPageReviews(int productPageID) {
        String getProductPageReviewsQuery = "select * from review where productPageID = ? and state = 'ACTIVE'";
        int getProductPageReviewsParam = productPageID;

        return this.jdbcTemplate.query(getProductPageReviewsQuery,
                (rs, rownum) -> new GetReviewRes(
                        rs.getInt("reviewID"),
                        rs.getInt("userID"),
                        rs.getInt("orderListID"),
                        rs.getInt("productPageID"),
                        rs.getInt("brandID"),
                        rs.getFloat("rate"),
                        rs.getString("reviewImageLink"),
                        rs.getString("content"),
                        rs.getInt("itHelps"),
                        rs.getString("createAt"),
                        rs.getString("updateAt"),
                        rs.getString("state")
                ),
                getProductPageReviewsParam);
    }

    public List<GetReviewRes> getBrandReviews(int brandID) {
        String getProductPageReviewsQuery = "select * from review where brandID = ? and state = 'ACTIVE'";
        int getProductPageReviewsParam = brandID;

        return this.jdbcTemplate.query(getProductPageReviewsQuery,
                (rs, rownum) -> new GetReviewRes(
                        rs.getInt("reviewID"),
                        rs.getInt("userID"),
                        rs.getInt("orderListID"),
                        rs.getInt("productPageID"),
                        rs.getInt("brandID"),
                        rs.getFloat("rate"),
                        rs.getString("reviewImageLink"),
                        rs.getString("content"),
                        rs.getInt("itHelps"),
                        rs.getString("createAt"),
                        rs.getString("updateAt"),
                        rs.getString("state")
                ),
                getProductPageReviewsParam);
    }

    public int modifyReview(PatchReviewReq patchReviewReq) {
        String modifyReviewQuery;
        Object[] modifyReviewParams;
        if(patchReviewReq.getReviewImageLink() == null) {
            modifyReviewQuery = "update review " +
                    "set rate = ?, content = ?, itHelps = ?, state = ? " +
                    "where reviewID = ?";
            modifyReviewParams = new Object[]{
                    patchReviewReq.getRate(),
                    patchReviewReq.getContent(),
                    patchReviewReq.getItHelps(),
                    patchReviewReq.getState(),
                    patchReviewReq.getReviewID()
            };
        } else {
            modifyReviewQuery = "update review " +
                    "set rate = ?, reviewImageLink = ?, content = ?, itHelps = ?, state = ? " +
                    "where reviewID = ?";
            modifyReviewParams = new Object[]{
                    patchReviewReq.getRate(),
                    patchReviewReq.getReviewImageLink(),
                    patchReviewReq.getContent(),
                    patchReviewReq.getItHelps(),
                    patchReviewReq.getState(),
                    patchReviewReq.getReviewID()
            };
        }

        return this.jdbcTemplate.update(modifyReviewQuery, modifyReviewParams);
    }

    public int deleteReview(DeleteReviewReq deleteReviewReq) {
        String deleteReviewQuery = "update review " +
                "set state = 'INACTIVE' " +
                "where reviewID = ? and userID = ?";
        Object[] deleteReviewParam = new Object[]{
                deleteReviewReq.getReviewID(),
                deleteReviewReq.getUserID()
        };

        return this.jdbcTemplate.update(deleteReviewQuery, deleteReviewParam);
    }

    public GetReviewTotalRes getBrandReviewsTotal(int brandID) {
        String getBrandReviewsTotalQuery = "select truncate(avg(r.rate), 2) avgRate, " +
                "       count(IF(r.rate = 1, r.rate, null)) cnt_1, " +
                "       count(IF(r.rate = 2, r.rate, null)) cnt_2, " +
                "       count(IF(r.rate = 3, r.rate, null)) cnt_3, " +
                "       count(IF(r.rate = 4, r.rate, null)) cnt_4, " +
                "       count(IF(r.rate = 5, r.rate, null)) cnt_5," +
                "       count(r.reviewID) cnt_reviews " +
                "from review r join brand b on r.brandID = b.brandID " +
                "where b.brandID = ?";
        int getBrandReviewsTotalParam = brandID;

        return this.jdbcTemplate.queryForObject(getBrandReviewsTotalQuery,
                (rs, rownum) -> new GetReviewTotalRes(
                        rs.getFloat("avgRate"),
                        rs.getInt("cnt_1"),
                        rs.getInt("cnt_2"),
                        rs.getInt("cnt_3"),
                        rs.getInt("cnt_4"),
                        rs.getInt("cnt_5"),
                        rs.getInt("cnt_reviews")
                ),
                getBrandReviewsTotalParam
        );
    }

    public GetReviewTotalRes getProductPageReviewsTotal(int productPageID) {
        String getProductPageReviewsTotalQuery = "select truncate(avg(r.rate), 2) avgRate, " +
                "       count(IF(r.rate = 1, r.rate, null)) cnt_1, " +
                "       count(IF(r.rate = 2, r.rate, null)) cnt_2, " +
                "       count(IF(r.rate = 3, r.rate, null)) cnt_3, " +
                "       count(IF(r.rate = 4, r.rate, null)) cnt_4, " +
                "       count(IF(r.rate = 5, r.rate, null)) cnt_5," +
                "       count(r.reviewID) cnt_reviews " +
                "from review r join product_page pp on r.productPageID = pp.productPageID " +
                "where pp.productPageID = ?";
        int getProductPageReviewsTotalParam = productPageID;

        return this.jdbcTemplate.queryForObject(getProductPageReviewsTotalQuery,
                (rs, rownum) -> new GetReviewTotalRes(
                        rs.getFloat("avgRate"),
                        rs.getInt("cnt_1"),
                        rs.getInt("cnt_2"),
                        rs.getInt("cnt_3"),
                        rs.getInt("cnt_4"),
                        rs.getInt("cnt_5"),
                        rs.getInt("cnt_reviews")
                ),
                getProductPageReviewsTotalParam
        );
    }
}
