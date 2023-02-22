package com.example.demo.src.inquiry;

import com.example.demo.src.inquiry.model.DeleteInquiryReq;
import com.example.demo.src.inquiry.model.GetInquiryRes;
import com.example.demo.src.inquiry.model.PostInquiryReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class InquiryDao {
    private JdbcTemplate jdbcTemplate;
    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public int createInquiry(PostInquiryReq postInquiryReq) {
        String createInquiryQuery = "insert into product_inquiry\n" +
                "(userID, productPageID, category, type, productOption, inquiryContent, isSecret)\n" +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        Object[] createInquiryParams = new Object[]{
                postInquiryReq.getUserID(),
                postInquiryReq.getProductPageID(),
                postInquiryReq.getCategory(),
                postInquiryReq.getType(),
                postInquiryReq.getProductOption(),
                postInquiryReq.getInquiryContent(),
                postInquiryReq.getIsSecret()
        };

        this.jdbcTemplate.update(createInquiryQuery, createInquiryParams);

        String lastInserIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInserIdQuery,int.class);
    }

    public List<GetInquiryRes> getProductPageInquiries(int productPageID) {
        String getProductPageInquiriesQuery = "select * from product_inquiry " +
                "where productPageID = ? and state = 'ACTIVE'";
        int getProductPageInquiriesParam = productPageID;

        return this.jdbcTemplate.query(getProductPageInquiriesQuery,
                (rs, rowNum) -> new GetInquiryRes(
                        rs.getInt("inquiryID"),
                        rs.getInt("userID"),
                        rs.getInt("productPageID"),
                        rs.getString("category"),
                        rs.getString("type"),
                        rs.getString("inquiryContent"),
                        rs.getInt("isSecret"),
                        rs.getString("isAnswered"),
                        rs.getString("createAt"),
                        rs.getString("updateAt"),
                        rs.getString("state"),
                        rs.getString("answerContent"),
                        rs.getString("answerCreateAt"),
                        rs.getString("answerUpdateAt"),
                        rs.getString("answerState")
                ),
                getProductPageInquiriesParam);
    }

    public List<GetInquiryRes> getUserInquiries(int userID) {
        String getUserInquiriesQuery = "select * from product_inquiry " +
                "where userID = ? and state = 'ACTIVE'";
        int getUserInquiriesParam = userID;

        return this.jdbcTemplate.query(getUserInquiriesQuery,
                (rs, rowNum) -> new GetInquiryRes(
                        rs.getInt("inquiryID"),
                        rs.getInt("userID"),
                        rs.getInt("productPageID"),
                        rs.getString("category"),
                        rs.getString("type"),
                        rs.getString("inquiryContent"),
                        rs.getInt("isSecret"),
                        rs.getString("isAnswered"),
                        rs.getString("createAt"),
                        rs.getString("updateAt"),
                        rs.getString("state"),
                        rs.getString("answerContent"),
                        rs.getString("answerCreateAt"),
                        rs.getString("answerUpdateAt"),
                        rs.getString("answerState")
                ),
                getUserInquiriesParam);
    }

    public GetInquiryRes getInquiry(int inquiryID) {
        String getInquiryQuery = "select * from product_inquiry " +
                "where inquiryID = ?";
        int getInquiryParam = inquiryID;

        return this.jdbcTemplate.queryForObject(getInquiryQuery,
                (rs, rowNum) -> new GetInquiryRes(
                        rs.getInt("inquiryID"),
                        rs.getInt("userID"),
                        rs.getInt("productPageID"),
                        rs.getString("category"),
                        rs.getString("type"),
                        rs.getString("inquiryContent"),
                        rs.getInt("isSecret"),
                        rs.getString("isAnswered"),
                        rs.getString("createAt"),
                        rs.getString("updateAt"),
                        rs.getString("state"),
                        rs.getString("answerContent"),
                        rs.getString("answerCreateAt"),
                        rs.getString("answerUpdateAt"),
                        rs.getString("answerState")
                ),
                getInquiryParam);
    }

    public int deleteInquiry(DeleteInquiryReq deleteInquiryReq) {
        String deleteInquiryQuery = "update product_inquiry set " +
                "state = 'INACTIVE' " +
                "where inquiryID = ?";
        int deleteInquiryParam = deleteInquiryReq.getInquiryID();

        return this.jdbcTemplate.update(deleteInquiryQuery, deleteInquiryParam);
    }
}
