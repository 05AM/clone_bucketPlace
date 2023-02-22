package com.example.demo.src.notice;


import com.example.demo.src.notice.model.GetNoticeRes;
import com.example.demo.src.user.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class NoticeDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetNoticeRes> getUserNotice(int userID){
        String getUserNoticeQuery = "select noticeTitle, createAt from user_notice where userID = ? order by createAt desc;";
        return this.jdbcTemplate.query(getUserNoticeQuery,
                (rs, rowNum) -> new GetNoticeRes(
                        rs.getString("noticeTitle"),
                        rs.getString("createAt")),
                userID);

    }
}
