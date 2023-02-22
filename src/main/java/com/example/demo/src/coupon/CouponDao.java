package com.example.demo.src.coupon;

import com.example.demo.src.coupon.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class CouponDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public int createCoupon(PostCouponReq postCouponReq) {
        String createCouponQuery = "insert into coupon " +
                "(couponCode, couponTitle, dcRate, dcAmount, minOrderAmount, openDate, endDate, availablePeriod, brandID) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Object[] createCouponParams = new Object[] {
                postCouponReq.getCouponCode(),
                postCouponReq.getCouponTitle(),
                postCouponReq.getDcRate(),
                postCouponReq.getDcAmount(),
                postCouponReq.getMinOrderAmount(),
                postCouponReq.getOpenDate(),
                postCouponReq.getEndDate(),
                postCouponReq.getAvailablePeriod(),
                postCouponReq.getBrandID()
        };

        this.jdbcTemplate.update(createCouponQuery, createCouponParams);

        String lastInsertIdQuery = "select last_insert_id()";
        int lastInsertId = this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);

        // 적용 가능한 상품 페이지 등록
        for(Integer id : postCouponReq.getApplicableProductPageID()) {
            createCouponQuery = "insert into mapping_coupon_product_page " +
                    "(couponID, productPageID) VALUES (?, ?)";
            createCouponParams = new Object[]{
                    lastInsertId,
                    id
            };
            this.jdbcTemplate.update(createCouponQuery, createCouponParams);
        }

        return lastInsertId;
    }

    public List<GetCouponRes> getCoupons(Integer userID, String state) {
        String getCouponsQuery;
        Object[] getCouponsParam;
        if(userID == null) {
            getCouponsQuery =
                    "select * " +
                    "from coupon " +
                    "where state = ?" +
                    "order by endDate, couponTitle";

            getCouponsParam = new Object[]{
                    state
            };
        } else {
            getCouponsQuery =
                    "select * " +
                    "from coupon " +
                    "where state = ? " +
                    "  and couponID NOT IN (select couponID from user_coupon where userID = ? and state = 'INACTIVE')" +
                    "order by endDate, couponTitle";
            getCouponsParam = new Object[]{
                    state,
                    userID
            };
        }

        return this.jdbcTemplate.query(getCouponsQuery,
                (rs, rownum) -> new GetCouponRes(
                        rs.getInt("couponID"),
                        rs.getString("couponCode"),
                        rs.getString("couponTitle"),
                        rs.getInt("dcRate"),
                        rs.getInt("dcAmount"),
                        rs.getInt("minOrderAmount"),
                        rs.getString("openDate"),
                        rs.getString("endDate"),
                        rs.getInt("availablePeriod"),
                        rs.getString("createAt"),
                        rs.getString("updateAt"),
                        rs.getString("state"),
                        rs.getInt("brandID")==0?null:rs.getInt("brandID")
                ),
                getCouponsParam
        );
    }

    public List<GetUserCouponRes> getUserCoupons(int userID, String state) {
        String getUserCouponsQuery;
        if(state.equals("ACTIVE")) {
            getUserCouponsQuery =
                    "select * " +
                    "from user_coupon join coupon using (couponID) " +
                    "where userID = ? and user_coupon.state = 'ACTIVE' " +
                    "order by expirationDate, couponTitle";
        }
        // 비활성화된 쿠폰 불러오기
        else {
            getUserCouponsQuery =
                    "select * " +
                    "from user_coupon join coupon using (couponID) " +
                    "where userID = ? and user_coupon.state = 'INACTIVE' " +
                    "order by usedDate, expirationDate, couponTitle";
        }

        int getUserCouponsParams = userID;

        return this.jdbcTemplate.query(getUserCouponsQuery,
                (rs, rownum) -> new GetUserCouponRes(
                        rs.getInt("userCouponID"),
                        rs.getInt("couponID"),
                        rs.getString("couponTitle"),
                        rs.getInt("dcRate"),
                        rs.getInt("dcAmount"),
                        rs.getInt("minOrderAmount"),
                        rs.getString("expirationDate"),
                        rs.getString("createAt"),
                        rs.getString("updateAt"),
                        rs.getString("state"),
                        rs.getInt("brandID")==0?null:rs.getInt("brandID")
                ),
                getUserCouponsParams
        );
    }

    private GetCouponRes getCoupon(int couponID) {
        String getCouponQuery = "select * " +
                "from coupon " +
                "where couponID = ?";
        int getCouponParam = couponID;

        return this.jdbcTemplate.queryForObject(getCouponQuery,
                (rs, rownum) -> new GetCouponRes(
                        rs.getInt("couponID"),
                        rs.getString("couponCode"),
                        rs.getString("couponTitle"),
                        rs.getInt("dcRate"),
                        rs.getInt("dcAmount"),
                        rs.getInt("minOrderAmount"),
                        rs.getString("openDate"),
                        rs.getString("endDate"),
                        rs.getInt("availablePeriod"),
                        rs.getString("createAt"),
                        rs.getString("updateAt"),
                        rs.getString("state"),
                        rs.getInt("brandID")==0?null:rs.getInt("brandID")
                ),
                getCouponParam);
    }

    private GetCouponRes getCoupon(String couponCode) {
        String getCouponQuery = "select * " +
                "from coupon " +
                "where couponCode = ?";
        String getCouponParam = couponCode;

        return this.jdbcTemplate.queryForObject(getCouponQuery,
                (rs, rownum) -> new GetCouponRes(
                        rs.getInt("couponID"),
                        rs.getString("couponCode"),
                        rs.getString("couponTitle"),
                        rs.getInt("dcRate"),
                        rs.getInt("dcAmount"),
                        rs.getInt("minOrderAmount"),
                        rs.getString("openDate"),
                        rs.getString("endDate"),
                        rs.getInt("availablePeriod"),
                        rs.getString("createAt"),
                        rs.getString("updateAt"),
                        rs.getString("state"),
                        rs.getInt("brandID")==0?null:rs.getInt("brandID")
                ),
                getCouponParam);
    }

    public List<GetCouponApplicableProduct> getCouponApplicableProducts(int couponID) {
        String GetCouponApplicableProductQuery =
                "select c.couponID, pp.productPageID, couponTitle, c.dcRate couponDcRate, dcAmount couponDcAmount, endDate, \n" +
                "       pp.mainImageLink, b.brandName, pp.pageTitle, pp.price productPrice, pp.dcRate productDcRate, \n" +
                "       (pp.price*(100-pp.dcRate)/100) productDcPrice, r.avgRate, r.cntReview, pp.isSpecialPrice, pp.isTodaysDeal\n" +
                "from coupon c\n" +
                "    join mapping_coupon_product_page mcpp on c.couponID = mcpp.couponID\n" +
                "    join product_page pp on mcpp.productPageID = pp.productPageID\n" +
                "    left outer join (select productPageID, TRUNCATE(AVG(rate),2) avgRate, COUNT(reviewID) cntReview FROM review GROUP BY productPageID) r ON pp.productPageID = r.productPageID " +
                "    join brand b on b.brandID = pp.brandID\n" +
                "where c.couponID = ? and pp.state = 'ACTIVE' and c.state = 'ACTIVE';";
        int GetCouponApplicableProductParam = couponID;

        return this.jdbcTemplate.query(GetCouponApplicableProductQuery,
                (rs, rownum) -> new GetCouponApplicableProduct(
                        rs.getInt("couponID"),
                        rs.getInt("productPageID"),
                        rs.getString("couponTitle"),
                        rs.getInt("couponDcRate"),
                        rs.getInt("couponDcAmount"),
                        rs.getString("endDate"),
                        rs.getString("mainImageLink"),
                        rs.getString("brandName"),
                        rs.getString("pageTitle"),
                        rs.getInt("productPrice"),
                        rs.getInt("productDcRate"),
                        rs.getInt("productDcPrice"),
                        rs.getFloat("avgRate"),
                        rs.getInt("cntReview"),
                        rs.getInt("isSpecialPrice"),
                        rs.getInt("isTodaysDeal")
                ),
                GetCouponApplicableProductParam
        );
    }

    public int checkUserCouponDuplication(int userID, int couponID) {
        String checkUserCouponDuplicationQuery =
                "select exists(select couponID from user_coupon where userID = ? and couponID = ?)";
        Object[] checkUserCouponDuplicationParam = new Object[]{
                userID,
                couponID
        };

        return this.jdbcTemplate.queryForObject(checkUserCouponDuplicationQuery,
                int.class,
                checkUserCouponDuplicationParam
        );
    }

    public int checkUserCouponDuplication(int userID, String couponCode) {
        String checkUserCouponDuplicationQuery =
                "select exists(select coupon.couponID " +
                        "from user_coupon join coupon using (couponID) " +
                        "where userID = ? and couponCode = ?)";
        Object[] checkUserCouponDuplicationParam = new Object[]{
                userID,
                couponCode
        };

        return this.jdbcTemplate.queryForObject(checkUserCouponDuplicationQuery,
                int.class,
                checkUserCouponDuplicationParam
        );
    }

    public int createUserCoupon(int userID, int couponID) {
        // 쿠폰 정보를 가져와서 저장
        GetCouponRes getCouponRes = getCoupon(couponID);
        // (현재시간 + 사용가능 시간) > (행사 종료일)이면
        // 만료일을 행사 종료일로
        String createUserCouponQuery =
                "insert into user_coupon " +
                "(expirationDate, couponID, userID) " +
                "VALUES (" +
                "        IF(DATE_ADD(current_date, INTERVAL ? DAY) > ?, ?, DATE_SUB(DATE_ADD(current_date, INTERVAL ? DAY), INTERVAL 1 SECOND)), " +
                "        ?, " +
                "        ? " +
                "        )";
        Object[] createUserCouponParams = new Object[]{
                getCouponRes.getAvailablePeriod(),
                getCouponRes.getEndDate(),
                getCouponRes.getEndDate(),
                getCouponRes.getAvailablePeriod(),
                getCouponRes.getCouponID(),
                userID
        };

        this.jdbcTemplate.update(createUserCouponQuery, createUserCouponParams);

        String lastInserIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInserIdQuery,int.class);
    }

    public int createUserCoupon(int userID, String couponCode) {
        // 쿠폰 정보를 가져와서 저장
        GetCouponRes getCouponRes = getCoupon(couponCode);

        // (현재시간 + 사용가능 시간) > (행사 종료일)이면
        // 만료일을 행사 종료일로
        String createUserCouponQuery =
                "insert into user_coupon " +
                "(expirationDate, couponID, userID) " +
                "VALUES (" +
                "        IF(DATE_ADD(current_date, INTERVAL ? DAY) > ?, ?, DATE_SUB(DATE_ADD(current_date, INTERVAL ? DAY), INTERVAL 1 SECOND)), " +
                "        ?, " +
                "        ? " +
                "        )";
        Object[] createUserCouponParams = new Object[]{
                getCouponRes.getAvailablePeriod(),
                getCouponRes.getEndDate(),
                getCouponRes.getEndDate(),
                getCouponRes.getAvailablePeriod(),
                getCouponRes.getCouponID(),
                userID
        };

        this.jdbcTemplate.update(createUserCouponQuery, createUserCouponParams);

        String lastInserIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInserIdQuery,int.class);
    }

    // 쿠폰 만료 시 유저 쿠폰 삭제
    public int deleteUserCoupon(int userCouponID) {
        String deleteUserCouponQuery = "update user_coupon set state = 'INACTIVE' where userCouponID = ?";
        int deleteUserCouponParam = userCouponID;

        return this.jdbcTemplate.update(deleteUserCouponQuery, deleteUserCouponParam);
    }

    // 쿠폰 사용일 삽입, 쿠폰 비활성화
    public int modifyUserCoupon(int userCouponID) {
        String modifyUserCouponQuery = "update user_coupon set usedDate = CURRENT_TIMESTAMP, state = 'INACTIVE' where userCouponID = ?";
        int modifyUserCouponParam = userCouponID;

        return this.jdbcTemplate.update(modifyUserCouponQuery, modifyUserCouponParam);
    }


    public int deleteCoupon(int couponID) {
        // 유저 쿠폰 비활성화
        String deleteCouponQuery = "update user_coupon set state = 'INACTIVE' where couponID = ?";
        int deleteCouponParam = couponID;

        this.jdbcTemplate.update(deleteCouponQuery, deleteCouponParam);

        // 쿠폰 비활성화
        deleteCouponQuery = "update coupon set state = 'INACTIVE' where couponID = ?";

        return this.jdbcTemplate.update(deleteCouponQuery, deleteCouponParam);
    }
}
