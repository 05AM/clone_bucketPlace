package com.example.demo.src.brand;

import com.example.demo.src.brand.model.GetBrandRes;
import com.example.demo.src.brand.model.PostBrandReq;
import com.example.demo.src.brand.model.PostBrandRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class BrandDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public GetBrandRes getBrand(int brandID) {
        String getBrandQuery = "select b.brandID, brandName, businessName, r.avgRate, r.cntReview," +
                "representativeName, businessLocation, inquiryPhoneNum, email, registrationNum, " +
                "b.createAt, b.updateAt, b.state " +
                "from brand b " +
                "left outer join " +
                "(select brandID, ROUND(AVG(rate), 2) avgRate, COUNT(reviewID) cntReview FROM review GROUP BY brandID) r USING(brandID) " +
                "where b.brandID = ? " +
                "";
        int getBrandParam = brandID;

        return this.jdbcTemplate.queryForObject(getBrandQuery,
                (rs, rownum) -> new GetBrandRes(
                        rs.getInt("brandID"),
                        rs.getString("brandName"),
                        rs.getString("businessName"),
                        rs.getFloat("avgRate"),
                        rs.getInt("cntReview"),
                        rs.getString("representativeName"),
                        rs.getString("businessLocation"),
                        rs.getString("inquiryPhoneNum"),
                        rs.getString("email"),
                        rs.getString("registrationNum"),
                        rs.getString("createAt"),
                        rs.getString("updateAt"),
                        rs.getString("state")),
                getBrandParam);
    }

    public List<GetBrandRes> getBrands(String state) {
        String getBrandsQuery = "select b.brandID, brandName, businessName, r.avgRate, r.cntReview," +
                "representativeName, businessLocation, inquiryPhoneNum, email, registrationNum, " +
                "b.createAt, b.updateAt, b.state " +
                "from brand b " +
                "left outer join " +
                "(select brandID, ROUND(AVG(rate), 2) avgRate, COUNT(reviewID) cntReview FROM review GROUP BY brandID) r USING(brandID) " +
                "where b.state = ? " +
                "";
        String getBrandsParam = state;

        return this.jdbcTemplate.query(getBrandsQuery,
                (rs, rownum) -> new GetBrandRes(
                        rs.getInt("brandID"),
                        rs.getString("brandName"),
                        rs.getString("businessName"),
                        rs.getFloat("avgRate"),
                        rs.getInt("cntReview"),
                        rs.getString("representativeName"),
                        rs.getString("businessLocation"),
                        rs.getString("inquiryPhoneNum"),
                        rs.getString("email"),
                        rs.getString("registrationNum"),
                        rs.getString("createAt"),
                        rs.getString("updateAt"),
                        rs.getString("state")),
                getBrandsParam);
    }

    public int checkRegistNumDuplication(PostBrandReq postBrandReq) {
        String checkRegistNumDuplicationQuery =
                "select exists (select brandID from brand where registrationNum = ? and state = 'ACTIVE')";
        String checkRegistNumDuplicationParams = postBrandReq.getRegistrationNum();

        return this.jdbcTemplate.queryForObject(checkRegistNumDuplicationQuery, int.class, checkRegistNumDuplicationParams);
    }

    public int checkbusinessNameDuplication(PostBrandReq postBrandReq) {
        String checkbusinessNameDuplicationQuery =
                "select exists (select brandID from brand where businessName = ? and state = 'ACTIVE')";
        String checkbusinessNameDuplicationParams = postBrandReq.getBusinessName();

        return this.jdbcTemplate.queryForObject(checkbusinessNameDuplicationQuery, int.class, checkbusinessNameDuplicationParams);
    }

    public Integer createBrand(PostBrandReq postBrandReq) {
        String createBrandQuery = "insert into brand " +
                "(brandName, businessName, representativeName, businessLocation, inquiryPhoneNum, email, registrationNum) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        Object[] createBrandParams = new Object[]{
                postBrandReq.getBrandName(),
                postBrandReq.getBusinessName(),
                postBrandReq.getRepresentativeName(),
                postBrandReq.getBusinessLocation(),
                postBrandReq.getInquiryPhoneNum(),
                postBrandReq.getEmail(),
                postBrandReq.getRegistrationNum()
        };

        this.jdbcTemplate.update(createBrandQuery, createBrandParams);

        String lastInserIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInserIdQuery,int.class);
    }
}
