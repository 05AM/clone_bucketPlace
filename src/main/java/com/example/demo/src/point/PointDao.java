package com.example.demo.src.point;


import com.example.demo.src.photo.model.PostPhotoCommentReq;
import com.example.demo.src.point.model.GetPointRes;
import com.example.demo.src.point.model.PointInfo;
import com.example.demo.src.point.model.PostPointReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class PointDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public GetPointRes getUserPoints(int userID){
        String getPointsQuery = "select createAt, noticeTitle, content, point, expiredAt from user_point where userID = ? AND state = 'ACTIVE' order by createAt desc;";
        List<PointInfo> pointInfoList = this.jdbcTemplate.query(getPointsQuery,
                (rs, rowNum) -> new PointInfo(
                        rs.getString("createAt"),
                        rs.getString("noticeTitle"),
                        rs.getString("content"),
                        rs.getInt("point"),
                        rs.getString("expiredAt")),
                userID);

        String totalPointQuery = "select sum(point) from user_point where userID = ? AND state = 'ACTIVE' order by createAt desc;";
        Integer totalPoint = this.jdbcTemplate.queryForObject(totalPointQuery, int.class, userID);

        if(totalPoint != null){
            GetPointRes getPointRes = new GetPointRes(totalPoint, pointInfoList.toArray(new PointInfo[pointInfoList.size()]));
            return getPointRes;
        }
        else{
            GetPointRes getPointRes = new GetPointRes(0, pointInfoList.toArray(new PointInfo[pointInfoList.size()]));
            return getPointRes;
        }
    }

    public void createPoint(int userID, PostPointReq postPointReq) {
        String createPointQuery = "insert into user_point (noticeTitle, content, point, userID) VALUES (?,?,?,?);";
        Object[] createPointParams = new Object[]{postPointReq.getNoticeTitle(), postPointReq.getContent(), postPointReq.getPoint(), userID};
        this.jdbcTemplate.update(createPointQuery, createPointParams);
    }

}
