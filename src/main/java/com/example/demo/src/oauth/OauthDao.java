package com.example.demo.src.oauth;

import com.example.demo.src.oauth.model.PostOauthCreateUserReq;
import com.example.demo.src.oauth.model.UserInfo;
import org.graalvm.compiler.nodes.calc.ObjectEqualsNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Random;

@Repository
public class OauthDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public int checkUserByEmail(String email){
        String checkEmailQuery = "select exists(select email from user where email = ?)";
        String checkEmailParam = email;
        return this.jdbcTemplate.queryForObject(checkEmailQuery, int.class, checkEmailParam);
    }

    public int getUserId(String email){
        String getUserIdQuery = "select userID from user where email = ?";
        return this.jdbcTemplate.queryForObject(getUserIdQuery, int.class, email);
    }

    public int createUser(String nickName, String email){
        String createUserQuery = "insert into user (userNickName, email) values (?,?)";
        Object[] createUserParams = new Object[]{nickName, email};
        this.jdbcTemplate.update(createUserQuery, createUserParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery, int.class);
    }

    public int changeNickName(String nickName, int userID) {
        String updateUserNickNameQuery = "update user set userNickname = ? where userID = ?";
        Object[] updateUserNickNameParams = new Object[]{nickName, userID};
        return this.jdbcTemplate.update(updateUserNickNameQuery, updateUserNickNameParams);
    }

    public int registerSocialUser(PostOauthCreateUserReq postOauthCreateUserReq, int userID, String userNickname){
        Random r = new Random();
        int certifiedIdCode = r.nextInt(888888) + 111111;

        String registerUserInfoQuery;
        if(postOauthCreateUserReq.getIsNoticeAgree() == 1){
            registerUserInfoQuery = "update user set userNickname = ?, certifiedIdCode = ?, isEmailNoticeAgree = 1, isSMSNoticeAgree = 1, isAppPushNoticeAgree = 1, isPersonalInfoAgree = ?, isMarketingAgree = ? where userID = ?";
        }
        else {
            registerUserInfoQuery = "update user set userNickname = ?, certifiedIdCode = ?, isEmailNoticeAgree = 0, isSMSNoticeAgree = 0, isAppPushNoticeAgree = 0, isPersonalInfoAgree = ?, isMarketingAgree = ? where userID = ?";
        }
        Object[] registerUserInfoParams = new Object[]{userNickname, certifiedIdCode, postOauthCreateUserReq.getIsPersonalInfoAgree(), postOauthCreateUserReq.getIsMarketingAgree() ,userID};
        return this.jdbcTemplate.update(registerUserInfoQuery ,registerUserInfoParams);
    }

    public int registerNormalUser(PostOauthCreateUserReq postOauthCreateUserReq){
        Random r = new Random();
        int certifiedIdCode = r.nextInt(888888) + 111111;

        String registerUserInfoQuery;
        if(postOauthCreateUserReq.getIsNoticeAgree() == 1){
            registerUserInfoQuery = "insert into user (email, password, userNickname, certifiedIdCode, isPersonalInfoAgree, isMarketingAgree, isEmailNoticeAgree, isSMSNoticeAgree, isAppPushNoticeAgree, gradeID) values (?,?,?,?,1,?,1,1,1)";
        }
        else{
            registerUserInfoQuery = "insert into user (email, password, userNickname, certifiedIdCode, isPersonalInfoAgree, isMarketingAgree, isEmailNoticeAgree, isSMSNoticeAgree, isAppPushNoticeAgree) values (?,?,?,?,1,?,0,0,0)";
        }
        Object[] registerUserParams = new Object[]{postOauthCreateUserReq.getEmail(), postOauthCreateUserReq.getPassword(), postOauthCreateUserReq.getUserNickname(), certifiedIdCode,postOauthCreateUserReq.getIsMarketingAgree()};
        this.jdbcTemplate.update(registerUserInfoQuery, registerUserParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery, int.class);
    }

    public int checkDuplicatedNickName(String nickName){
        String checkNickNameQuery = "select exists(select userNickname from user where userNickname = ?)";
        String checkNickNameParam = nickName;
        return this.jdbcTemplate.queryForObject(checkNickNameQuery, int.class, checkNickNameParam);
    }

    public int checkEmail(String email){
        String checkEmailQuery = "select exists(select email from user where email = ?)";
        String checkEmailParams = email;
        return this.jdbcTemplate.queryForObject(checkEmailQuery, int.class, checkEmailParams);
    }

    public UserInfo getPwd(String email){
        String getPwdQuery = "select userID, password from user where email = ?";
        return this.jdbcTemplate.queryForObject(getPwdQuery,
                (rs,rowNum) -> new UserInfo(
                        rs.getInt("userID"),
                        rs.getString("password")),
                email);
    }

    public UserInfo getUserInfo(int userID){
        String getUserInfo = "select email, userNickname from user where userID = ?";
        return this.jdbcTemplate.queryForObject(getUserInfo,
                (rs,rowNum) -> new UserInfo(
                        rs.getString("email"),
                        rs.getString("userNickname")),
                userID);
    }

    public void setTypeOfOAuth(String google, int userID){
        String setTypeOfOAuthQuery = "update user set typeOfOAuth = ? where userID = ?";
        Object[] setTypeOfOAuthParams = new Object[]{google, userID};
        this.jdbcTemplate.update(setTypeOfOAuthQuery, setTypeOfOAuthParams);
    }
}
