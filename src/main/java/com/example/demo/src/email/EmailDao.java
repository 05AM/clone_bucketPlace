package com.example.demo.src.email;


import com.example.demo.src.user.model.GetUserNoticeRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class EmailDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public int getCertifiedIdCodeByEmail(String email){
        String getCertifiedCodeByEmailQuery = "select certifiedIdCode from user where email = ?;";
        return this.jdbcTemplate.queryForObject(getCertifiedCodeByEmailQuery, int.class, email);
    }

    public void updateCertifiedIdCode(int userId, int certifiedIdCode){
        String updateCertifiedIdCode = "update user set certifiedIdCode = ? where userID = ?;";
        this.jdbcTemplate.update(updateCertifiedIdCode, certifiedIdCode, userId);
    }
}
