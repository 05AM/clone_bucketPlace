package com.example.demo.src.productPage;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.cart.model.GetCartOptionRes;
import com.example.demo.src.productPage.model.GetProductOptionRes;
import com.example.demo.src.productPage.model.GetProductPageRes;
import com.example.demo.src.productPage.model.PostProductPageReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.management.Query;
import javax.sql.DataSource;
import java.util.List;

@Repository
public class ProductPageDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetProductPageRes> getProductPages(String state) {
        String getProductPagesQuery =
                "select " +
                        "pp.productPageID, categoryMain, categoryMid, productID, mainImageLink, pageTitle, " +
                        "r.avgRate, r.cntReview, (10*IFNULL(avgRate,0) + 3*IFNULL(cntReview,0) + 5*IFNULL(cnt_order,0)) score, " +
                        "price, dcRate, (price*(100-dcRate)/100) dcPrice, dcEndDate, productInfoImageLink, isSpecialPrice, isTodaysDeal, " +
                        "isRental, isCrossBorderShopping, isRefurbished, shipMethod, shipFee, paymentMethod, additionalShipFee, " +
                        "additionalEtcAreaFee, isIsolatedAreaAvailable, isJejuAreaAvailable, proratedShipFee, returnShipFee, exchangeShipFee, " +
                        "returnAddress, pointRate, pp.createAt, pp.updateAt, pp.state, pp.brandID, b.brandName " +
                "from product_page pp join brand b on (pp.brandID = b.brandID) " +
                "   left outer join (select productPageID, TRUNCATE(AVG(rate),2) avgRate, COUNT(reviewID) cntReview FROM review GROUP BY productPageID) r USING(productPageID) " +
                "   left outer join (select productPageID, count(orderListID) cnt_order from order_list) ol on pp.productPageID = ol.productPageID " +
                "where pp.state = ? " +
                "order by score desc, r.avgRate desc";
        String getProductPagesParam = state;

        return this.jdbcTemplate.query(getProductPagesQuery,
                (rs, rownum) -> new GetProductPageRes(
                        rs.getInt("productPageID"),
                        rs.getInt("productID"),
                        rs.getInt("brandID"),
                        rs.getString("brandName"),
                        rs.getString("categoryMain"),
                        rs.getString("categoryMid"),
                        rs.getString("pageTitle"),
                        rs.getString("mainImageLink"),
                        rs.getFloat("avgRate"),
                        rs.getInt("cntReview"),
                        rs.getInt("price"),
                        rs.getInt("dcRate"),
                        rs.getInt("dcPrice"),
                        rs.getString("dcEndDate"),
                        rs.getInt("isSpecialPrice"),
                        rs.getInt("isTodaysDeal"),
                        rs.getInt("isRental"),
                        rs.getInt("isCrossBorderShopping"),
                        rs.getInt("isRefurbished"),
                        rs.getInt("pointRate"),
                        rs.getString("productInfoImageLink"),
                        rs.getString("shipMethod"),
                        rs.getInt("shipFee"),
                        rs.getString("paymentMethod"),
                        rs.getString("additionalShipFee"),
                        rs.getInt("isIsolatedAreaAvailable"),
                        rs.getInt("isJejuAreaAvailable"),
                        rs.getString("proratedShipFee"),
                        rs.getString("additionalEtcAreaFee"),
                        rs.getInt("returnShipFee"),
                        rs.getInt("exchangeShipFee"),
                        rs.getString("returnAddress"),
                        rs.getString("createAt"),
                        rs.getString("updateAt"),
                        rs.getString("state")
                ),
                getProductPagesParam
        );
    }

    public List<GetProductPageRes> getProductPages(String state, String category, String categoryName) {
        String getProductPagesQuery;
        if(category == "categoryMain") {
            getProductPagesQuery = "select pp.productPageID, categoryMain, categoryMid, productID, mainImageLink, pageTitle, " +
                    "r.avgRate, r.cntReview, (10*IFNULL(avgRate,0) + 3*IFNULL(cntReview,0) + 5*IFNULL(cnt_order,0)) score, price, dcRate, " +
                    "(price*(100-dcRate)/100) dcPrice, dcEndDate, " +
                    "productInfoImageLink, isSpecialPrice, isTodaysDeal, isRental, isCrossBorderShopping, isRefurbished, shipMethod, shipFee, paymentMethod, additionalShipFee, " +
                    "additionalEtcAreaFee, isIsolatedAreaAvailable, isJejuAreaAvailable, proratedShipFee, returnShipFee, exchangeShipFee, " +
                    "returnAddress, pointRate, pp.createAt, pp.updateAt, pp.state, pp.brandID, b.brandName " +
                    "from product_page pp join brand b on (pp.brandID = b.brandID) " +
                    "   left outer join (select productPageID, TRUNCATE(AVG(rate),2) avgRate, COUNT(reviewID) cntReview FROM review GROUP BY productPageID) r on r.productPageID = pp.productPageID  " +
                    "   left outer join (select productPageID, count(orderListID) cnt_order from order_list) ol on pp.productPageID = ol.productPageID " +
                    "where pp.state = ? and categoryMain = ? " +
                    "order by score desc, r.avgRate desc;";
        } else if(category == "categoryMid") {
            getProductPagesQuery = "select pp.productPageID, categoryMain, categoryMid, productID, mainImageLink, pageTitle, " +
                    "r.avgRate, r.cntReview, (10*IFNULL(avgRate,0) + 3*IFNULL(cntReview,0) + 5*IFNULL(cnt_order,0)) score, price, dcRate, " +
                    "(price*(100-dcRate)/100) dcPrice, dcEndDate, " +
                    "productInfoImageLink, isSpecialPrice, isTodaysDeal, isRental, isCrossBorderShopping, isRefurbished, shipMethod, shipFee, paymentMethod, additionalShipFee, " +
                    "additionalEtcAreaFee, isIsolatedAreaAvailable, isJejuAreaAvailable, proratedShipFee, returnShipFee, exchangeShipFee, " +
                    "returnAddress, pointRate, pp.createAt, pp.updateAt, pp.state, pp.brandID, b.brandName " +
                    "from product_page pp join brand b on (pp.brandID = b.brandID) " +
                    "   left outer join (select productPageID, TRUNCATE(AVG(rate),2) avgRate, COUNT(reviewID) cntReview FROM review GROUP BY productPageID) r on r.productPageID = pp.productPageID  " +
                    "   left outer join (select productPageID, count(orderListID) cnt_order from order_list) ol on pp.productPageID = ol.productPageID " +
                    "where pp.state = ? and categoryMid = ? " +
                    "order by score desc, r.avgRate desc;";

        } else if(category == "categoryDetail") {
            getProductPagesQuery = "select pp.productPageID, categoryMain, categoryMid, productID, mainImageLink, pageTitle, " +
                    "r.avgRate, r.cntReview, (10*IFNULL(avgRate,0) + 3*IFNULL(cntReview,0) + 5*IFNULL(cnt_order,0)) score, price, dcRate, " +
                    "(price*(100-dcRate)/100) dcPrice, dcEndDate, productInfoImageLink, isSpecialPrice, isTodaysDeal, isRental, " +
                    "isCrossBorderShopping, isRefurbished, shipMethod, shipFee, paymentMethod, additionalShipFee, additionalEtcAreaFee, " +
                    "isIsolatedAreaAvailable, isJejuAreaAvailable, proratedShipFee, returnShipFee, exchangeShipFee, returnAddress, " +
                    "pointRate, pp.createAt, pp.updateAt, pp.state, pp.brandID, b.brandName " +
                    "from product_page pp join product_sofa ps on (pp.productID = ps.sofaID) " +
                    "join brand b on (pp.brandID = b.brandID) " +
                    "   left outer join (select productPageID, TRUNCATE(AVG(rate),2) avgRate, COUNT(reviewID) cntReview FROM review GROUP BY productPageID) r on r.productPageID = pp.productPageID  " +
                    "   left outer join (select productPageID, count(orderListID) cnt_order from order_list) ol on pp.productPageID = ol.productPageID " +
                    "where pp.state = ? and categorySofa = ? " +
                    "order by score desc, r.avgRate desc;";
        } else {
            throw new BaseException(BaseResponseStatus.SERVER_ERROR);
        }

        Object[] getProductPagesParams = new Object[]{
                state,
                categoryName
        };

        return this.jdbcTemplate.query(getProductPagesQuery,
                (rs, rownum) -> new GetProductPageRes(
                        rs.getInt("productPageID"),
                        rs.getInt("productID"),
                        rs.getInt("brandID"),
                        rs.getString("brandName"),
                        rs.getString("categoryMain"),
                        rs.getString("categoryMid"),
                        rs.getString("pageTitle"),
                        rs.getString("mainImageLink"),
                        rs.getFloat("avgRate"),
                        rs.getInt("cntReview"),
                        rs.getInt("price"),
                        rs.getInt("dcRate"),
                        rs.getInt("dcPrice"),
                        rs.getString("dcEndDate"),
                        rs.getInt("isSpecialPrice"),
                        rs.getInt("isTodaysDeal"),
                        rs.getInt("isRental"),
                        rs.getInt("isCrossBorderShopping"),
                        rs.getInt("isRefurbished"),
                        rs.getInt("pointRate"),
                        rs.getString("productInfoImageLink"),
                        rs.getString("shipMethod"),
                        rs.getInt("shipFee"),
                        rs.getString("paymentMethod"),
                        rs.getString("additionalShipFee"),
                        rs.getInt("isIsolatedAreaAvailable"),
                        rs.getInt("isJejuAreaAvailable"),
                        rs.getString("proratedShipFee"),
                        rs.getString("additionalEtcAreaFee"),
                        rs.getInt("returnShipFee"),
                        rs.getInt("exchangeShipFee"),
                        rs.getString("returnAddress"),
                        rs.getString("createAt"),
                        rs.getString("updateAt"),
                        rs.getString("state")
                ),
                getProductPagesParams
        );
    }

    // 특정 상품 페이지 가져오기
    public GetProductPageRes getProductPage(int productPageID) {
        String getProductPageQuery = "select pp.productPageID, categoryMain, categoryMid, productID, mainImageLink, pageTitle, " +
                "r.avgRate, r.cntReview, price, dcRate, (price*(100-dcRate)/100) dcPrice, dcEndDate, " +
                "productInfoImageLink, isSpecialPrice, isTodaysDeal, isRental, isCrossBorderShopping, isRefurbished, shipMethod, shipFee, paymentMethod, additionalShipFee, " +
                "additionalEtcAreaFee, isIsolatedAreaAvailable, isJejuAreaAvailable, proratedShipFee, returnShipFee, exchangeShipFee, " +
                "returnAddress, pointRate, pp.createAt, pp.updateAt, pp.state, pp.brandID, b.brandName " +
                "from product_page pp join brand b on (pp.brandID = b.brandID) " +
                "                  left outer join (select productPageID, TRUNCATE(AVG(rate),2) avgRate, COUNT(reviewID) cntReview FROM review GROUP BY productPageID) r USING(productPageID) " +
                "where pp.state = 'ACTIVE' and productPageID = ?";
        int getProductPageParam = productPageID;

        GetProductPageRes getProductPageRes = this.jdbcTemplate.queryForObject(getProductPageQuery,
                (rs, rownum) -> new GetProductPageRes(
                        rs.getInt("productPageID"),
                        rs.getInt("productID"),
                        rs.getInt("brandID"),
                        rs.getString("brandName"),
                        rs.getString("categoryMain"),
                        rs.getString("categoryMid"),
                        rs.getString("pageTitle"),
                        rs.getString("mainImageLink"),
                        rs.getFloat("avgRate"),
                        rs.getInt("cntReview"),
                        rs.getInt("price"),
                        rs.getInt("dcRate"),
                        rs.getInt("dcPrice"),
                        rs.getString("dcEndDate"),
                        rs.getInt("isSpecialPrice"),
                        rs.getInt("isTodaysDeal"),
                        rs.getInt("isRental"),
                        rs.getInt("isCrossBorderShopping"),
                        rs.getInt("isRefurbished"),
                        rs.getInt("pointRate"),
                        rs.getString("productInfoImageLink"),
                        rs.getString("shipMethod"),
                        rs.getInt("shipFee"),
                        rs.getString("paymentMethod"),
                        rs.getString("additionalShipFee"),
                        rs.getInt("isIsolatedAreaAvailable"),
                        rs.getInt("isJejuAreaAvailable"),
                        rs.getString("proratedShipFee"),
                        rs.getString("additionalEtcAreaFee"),
                        rs.getInt("returnShipFee"),
                        rs.getInt("exchangeShipFee"),
                        rs.getString("returnAddress"),
                        rs.getString("createAt"),
                        rs.getString("updateAt"),
                        rs.getString("state")
                ),
                getProductPageParam
        );

        // 배너 사진 목록 가져오기
        getProductPageQuery = "select * from product_banner_image where productPageID = ?";
        getProductPageRes.setBannerImageLink(
                this.jdbcTemplate.query(getProductPageQuery,
                        (rs, rownum) -> rs.getString("bannerImageLink"),
                        getProductPageParam));

        return getProductPageRes;
    }

    public List<GetProductPageRes> getProductPagesByBrand(int brandID) {
        String getProductPagesByBrandQuery = "select pp.productPageID, categoryMain, categoryMid, productID, mainImageLink, pageTitle, " +
                "r.avgRate, r.cntReview, (10*IFNULL(avgRate,0) + 3*IFNULL(cntReview,0) + 5*IFNULL(cnt_order,0)) score,price, dcRate, " +
                "(price*(100-dcRate)/100) dcPrice, dcEndDate, " +
                "productInfoImageLink, isSpecialPrice, isTodaysDeal, isRental, isCrossBorderShopping, isRefurbished, shipMethod, shipFee, paymentMethod, additionalShipFee, " +
                "additionalEtcAreaFee, isIsolatedAreaAvailable, isJejuAreaAvailable, proratedShipFee, returnShipFee, exchangeShipFee, " +
                "returnAddress, pointRate, pp.createAt, pp.updateAt, pp.state, pp.brandID, b.brandName " +
                "from product_page pp join brand b on (pp.brandID = b.brandID) " +
                "   left outer join (select brandID, TRUNCATE(AVG(rate),2) avgRate, COUNT(reviewID) cntReview FROM review GROUP BY brandID) r ON b.brandID = r.brandID " +
                "   left outer join (select productPageID, count(orderListID) cnt_order from order_list) ol on pp.productPageID = ol.productPageID " +
                "where pp.state = 'ACTIVE' and pp.brandID = ? " +
                "order by score desc, r.avgRate desc";
        int getProductPagesByBrandParam = brandID;

        return this.jdbcTemplate.query(getProductPagesByBrandQuery,
                (rs, rownum) -> new GetProductPageRes(
                        rs.getInt("productPageID"),
                        rs.getInt("productID"),
                        rs.getInt("brandID"),
                        rs.getString("brandName"),
                        rs.getString("categoryMain"),
                        rs.getString("categoryMid"),
                        rs.getString("pageTitle"),
                        rs.getString("mainImageLink"),
                        rs.getFloat("avgRate"),
                        rs.getInt("cntReview"),
                        rs.getInt("price"),
                        rs.getInt("dcRate"),
                        rs.getInt("dcPrice"),
                        rs.getString("dcEndDate"),
                        rs.getInt("isSpecialPrice"),
                        rs.getInt("isTodaysDeal"),
                        rs.getInt("isRental"),
                        rs.getInt("isCrossBorderShopping"),
                        rs.getInt("isRefurbished"),
                        rs.getInt("pointRate"),
                        rs.getString("productInfoImageLink"),
                        rs.getString("shipMethod"),
                        rs.getInt("shipFee"),
                        rs.getString("paymentMethod"),
                        rs.getString("additionalShipFee"),
                        rs.getInt("isIsolatedAreaAvailable"),
                        rs.getInt("isJejuAreaAvailable"),
                        rs.getString("proratedShipFee"),
                        rs.getString("additionalEtcAreaFee"),
                        rs.getInt("returnShipFee"),
                        rs.getInt("exchangeShipFee"),
                        rs.getString("returnAddress"),
                        rs.getString("createAt"),
                        rs.getString("updateAt"),
                        rs.getString("state")
                ),
                getProductPagesByBrandParam
        );
    }

    public List<GetProductOptionRes> getProductOptions(int productPageID) {
        String getProductOptionsQuery = "select product_page_option.optionID, product_page_option_detail.optionDetailID, " +
                "product_page_option.title, product_page_option_detail.titleDetail, isSelective, product_page_option_detail.price, " +
                " product_page_option.state state, product_page_option_detail.state stateDetail " +
                "from product_page join product_page_option using(productPageID) " +
                "join product_page_option_detail on (product_page_option.optionID = product_page_option_detail.optionID) " +
                "where productPageID = ?";

        int getProductOptionsParam = productPageID;

        return this.jdbcTemplate.query(getProductOptionsQuery,
                (rs, rownum) -> new GetProductOptionRes(
                        rs.getInt("optionID"),
                        rs.getInt("optionDetailID"),
                        rs.getInt("isSelective"),
                        rs.getString("title"),
                        rs.getString("titleDetail"),
                        rs.getInt("price"),
                        rs.getString("state"),
                        rs.getString("stateDetail")
                ),
                getProductOptionsParam
        );
    }

    public int checkTitleDuplication(PostProductPageReq postProductPageReq) {
        String checkTitleDuplicationQuery =
                "select exists(select productPageID from product_page where pageTitle = ? and state = 'ACTIVE')";
        String checkTitleDuplicationParam = postProductPageReq.getPageTitle();

        return this.jdbcTemplate.queryForObject(checkTitleDuplicationQuery, int.class, checkTitleDuplicationParam);
    }

    public Integer createProductPage(PostProductPageReq postProductPageReq) {
        String createProductPageQuery = "insert into product_page " +
                "(categoryMain, categoryMid, productID, mainImageLink, pageTitle, price, dcRate, dcEndDate, pointRate, " +
                "productInfoImageLink, isSpecialPrice, isTodaysDeal, isRental, isCrossBorderShopping, isRefurbished, shipMethod, " +
                "shipFee, paymentMethod, additionalShipFee, isIsolatedAreaAvailable, isJejuAreaAvailable, additionalEtcAreaFee, " +
                "proratedShipFee, returnShipFee, " +
                "exchangeShipFee, returnAddress, brandID) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?)";
        Object[] createProductPageParams = new Object[]{
                postProductPageReq.getCategoryMain(),
                postProductPageReq.getCategoryMid(),
                postProductPageReq.getProductID()==0?null:postProductPageReq.getProductID(),
                postProductPageReq.getMainImageLink(),
                postProductPageReq.getPageTitle(),
                postProductPageReq.getPrice(),
                postProductPageReq.getDcRate(),
                postProductPageReq.getDcEndDate(),
                postProductPageReq.getPointRate(),
                postProductPageReq.getProductInfoImageLink(),
                postProductPageReq.getIsSpecialPrice(),
                postProductPageReq.getIsTodaysDeal(),
                postProductPageReq.getIsRental(),
                postProductPageReq.getIsCrossBorderShopping(),
                postProductPageReq.getIsRefurbished(),
                postProductPageReq.getShipMethod(),
                postProductPageReq.getShipFee(),
                postProductPageReq.getPaymentMethod(),
                postProductPageReq.getAdditionalShipFee(),
                postProductPageReq.getIsIsolatedAreaAvailable(),
                postProductPageReq.getIsJejuAreaAvailable(),
                postProductPageReq.getAdditionalEtcAreaFee(),
                postProductPageReq.getProratedShipFee(),
                postProductPageReq.getReturnShipFee(),
                postProductPageReq.getExchangeShipFee(),
                postProductPageReq.getReturnAddress(),
                postProductPageReq.getBrandID()
        };

        this.jdbcTemplate.update(createProductPageQuery, createProductPageParams);

        String lastInsertIdQuery = "select last_insert_id()";
        Integer productPageID = this.jdbcTemplate.queryForObject(lastInsertIdQuery, Integer.class);

        // 상품 배너 이미지 넣기
        createProductPageQuery = "insert into product_banner_image " +
                "(bannerImageLink, productPageID) " +
                "VALUES (?, ?);";
        for(String bannerImageLink : postProductPageReq.getBannerImageLink()){
            createProductPageParams = new Object[]{
                    bannerImageLink,
                    productPageID
            };

            this.jdbcTemplate.update(createProductPageQuery, createProductPageParams);
        }

        return productPageID;
    }

    public List<GetProductPageRes> getProductPagesTop10(String categoryDetail) {
        String getProductPagesTop10Query = "select pp.productPageID, categoryMain, categoryMid, productID, mainImageLink, pageTitle, " +
                "r.avgRate, r.cntReview, (10*IFNULL(avgRate,0) + 3*IFNULL(cntReview,0) + 5*IFNULL(cnt_order,0)) score, price, dcRate, " +
                "(price*(100-dcRate)/100) dcPrice, dcEndDate, productInfoImageLink, isSpecialPrice, isTodaysDeal, isRental, " +
                "isCrossBorderShopping, isRefurbished, shipMethod, shipFee, paymentMethod, additionalShipFee, additionalEtcAreaFee, " +
                "isIsolatedAreaAvailable, isJejuAreaAvailable, proratedShipFee, returnShipFee, exchangeShipFee, returnAddress, " +
                "pointRate, pp.createAt, pp.updateAt, pp.state, pp.brandID, b.brandName " +
                "from product_page pp join product_sofa ps on (pp.productID = ps.sofaID) " +
                "join brand b on (pp.brandID = b.brandID) " +
                "   left outer join (select productPageID, TRUNCATE(AVG(rate),2) avgRate, COUNT(reviewID) cntReview FROM review GROUP BY productPageID) r on r.productPageID = pp.productPageID " +
                "   left outer join (select productPageID, count(orderListID) cnt_order from order_list) ol on pp.productPageID = ol.productPageID " +
                "where pp.state = 'ACTIVE' and categorySofa = ? " +
                "order by score desc, r.avgRate desc " +
                "limit 10";
        String getProductPagesTop10Param = categoryDetail;

        try{
            return this.jdbcTemplate.query(getProductPagesTop10Query,
                    (rs, rowNum) -> new GetProductPageRes(
                            rs.getInt("productPageID"),
                            rs.getInt("productID"),
                            rs.getInt("brandID"),
                            rs.getString("brandName"),
                            rs.getString("categoryMain"),
                            rs.getString("categoryMid"),
                            rs.getString("pageTitle"),
                            rs.getString("mainImageLink"),
                            rs.getFloat("avgRate"),
                            rs.getInt("cntReview"),
                            rs.getInt("price"),
                            rs.getInt("dcRate"),
                            rs.getInt("dcPrice"),
                            rs.getString("dcEndDate"),
                            rs.getInt("isSpecialPrice"),
                            rs.getInt("isTodaysDeal"),
                            rs.getInt("isRental"),
                            rs.getInt("isCrossBorderShopping"),
                            rs.getInt("isRefurbished"),
                            rs.getInt("pointRate"),
                            rs.getString("productInfoImageLink"),
                            rs.getString("shipMethod"),
                            rs.getInt("shipFee"),
                            rs.getString("paymentMethod"),
                            rs.getString("additionalShipFee"),
                            rs.getInt("isIsolatedAreaAvailable"),
                            rs.getInt("isJejuAreaAvailable"),
                            rs.getString("proratedShipFee"),
                            rs.getString("additionalEtcAreaFee"),
                            rs.getInt("returnShipFee"),
                            rs.getInt("exchangeShipFee"),
                            rs.getString("returnAddress"),
                            rs.getString("createAt"),
                            rs.getString("updateAt"),
                            rs.getString("state")
                    ),
                    getProductPagesTop10Param);
        } catch(EmptyResultDataAccessException e) {
            throw new BaseException(BaseResponseStatus.RESULT_NOT_EXISTS);
        }
    }

    public List<GetProductPageRes> getDCProductPagesFromCart(int userID) {
        String getDCProductPagesFromCartQuery = "select pp.productPageID, categoryMain, categoryMid, productID, mainImageLink, pageTitle, " +
                "r.avgRate, r.cntReview, (10*IFNULL(avgRate,0) + 3*IFNULL(cntReview,0) + 5*IFNULL(cnt_order,0)) score, price, dcRate, " +
                "(price*(100-dcRate)/100) dcPrice, dcEndDate, " +
                "productInfoImageLink, isSpecialPrice, isTodaysDeal, isRental, isCrossBorderShopping, isRefurbished, shipMethod, shipFee, paymentMethod, additionalShipFee, " +
                "additionalEtcAreaFee, isIsolatedAreaAvailable, isJejuAreaAvailable, proratedShipFee, returnShipFee, exchangeShipFee, " +
                "returnAddress, pointRate, pp.createAt, pp.updateAt, pp.state, pp.brandID, b.brandName " +
                "from product_page pp join brand b on (pp.brandID = b.brandID) " +
                "   join (select * from cart where userID = ?) c on pp.productPageID = c.productPageID " +
                "   left outer join (select productPageID, TRUNCATE(AVG(rate),2) avgRate, COUNT(reviewID) cntReview FROM review GROUP BY productPageID) r on r.productPageID = pp.productPageID " +
                "   left outer join (select productPageID, count(orderListID) cnt_order from order_list) ol on pp.productPageID = ol.productPageID " +
                "where pp.state = 'ACTIVE' " +
                "   and pp.dcEndDate > CURRENT_TIMESTAMP " +
                "order by score desc, r.avgRate desc";
        int getDCProductPagesFromCartParam = userID;

        try{
            return this.jdbcTemplate.query(getDCProductPagesFromCartQuery,
                    (rs, rowNum) -> new GetProductPageRes(
                            rs.getInt("productPageID"),
                            rs.getInt("productID"),
                            rs.getInt("brandID"),
                            rs.getString("brandName"),
                            rs.getString("categoryMain"),
                            rs.getString("categoryMid"),
                            rs.getString("pageTitle"),
                            rs.getString("mainImageLink"),
                            rs.getFloat("avgRate"),
                            rs.getInt("cntReview"),
                            rs.getInt("price"),
                            rs.getInt("dcRate"),
                            rs.getInt("dcPrice"),
                            rs.getString("dcEndDate"),
                            rs.getInt("isSpecialPrice"),
                            rs.getInt("isTodaysDeal"),
                            rs.getInt("isRental"),
                            rs.getInt("isCrossBorderShopping"),
                            rs.getInt("isRefurbished"),
                            rs.getInt("pointRate"),
                            rs.getString("productInfoImageLink"),
                            rs.getString("shipMethod"),
                            rs.getInt("shipFee"),
                            rs.getString("paymentMethod"),
                            rs.getString("additionalShipFee"),
                            rs.getInt("isIsolatedAreaAvailable"),
                            rs.getInt("isJejuAreaAvailable"),
                            rs.getString("proratedShipFee"),
                            rs.getString("additionalEtcAreaFee"),
                            rs.getInt("returnShipFee"),
                            rs.getInt("exchangeShipFee"),
                            rs.getString("returnAddress"),
                            rs.getString("createAt"),
                            rs.getString("updateAt"),
                            rs.getString("state")
                    ),
                    getDCProductPagesFromCartParam);
        } catch (EmptyResultDataAccessException e) {
            throw new BaseException(BaseResponseStatus.RESULT_NOT_EXISTS);
        }
    }

    public void checkProductPageDCEndDate() {
        String checkProductPageDCEndDateQuery = "update product_page " +
                "set isSpecialPrice = 0, dcEndDate = null, dcRate = null " +
                "where productPageID in " +
                "      (select productPageID " +
                "      from (select productPageID " +
                "       from product_page " +
                "       where dcEndDate < CURRENT_TIMESTAMP) tmp)";

        this.jdbcTemplate.update(checkProductPageDCEndDateQuery);
    }
}
