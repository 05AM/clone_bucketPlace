package com.example.demo.src.search;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.photo.PhotoDao;
import com.example.demo.src.productPage.model.GetProductPageRes;
import com.example.demo.src.search.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository
public class SearchDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource, PhotoDao photoDao){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    // 가구 검색
    public List<GetProductPageRes> getFurniturePPs(GetFurnituresReq getFurnituresReq) {
        String getFurniturePPsQuery = "select pp.productPageID, categoryMain, categoryMid, productID, mainImageLink, pageTitle, " +
                "r.avgRate, r.cntReview, (10*IFNULL(avgRate,0) + 3*IFNULL(cntReview,0) + 5*IFNULL(cnt_order,0)) score, price, dcRate, " +
                "(price*(100-dcRate)/100) dcPrice, dcEndDate, productInfoImageLink, isSpecialPrice, isTodaysDeal, isRental, isCrossBorderShopping," +
                "isRefurbished, shipMethod, shipFee, paymentMethod, additionalShipFee, additionalEtcAreaFee, isIsolatedAreaAvailable, " +
                "isJejuAreaAvailable, proratedShipFee, returnShipFee, exchangeShipFee, returnAddress, pointRate, " +
                "pp.createAt, pp.updateAt, pp.state, pp.brandID, b.brandName " +
                "from product_page pp join brand b on (pp.brandID = b.brandID) " +
                "   left outer join (select productPageID, TRUNCATE(AVG(rate),2) avgRate, COUNT(reviewID) cntReview FROM review GROUP BY productPageID) r USING(productPageID) " +
                "   left outer join (select productPageID, count(orderListID) cnt_order from order_list) ol on pp.productPageID = ol.productPageID " +
                "   left outer join color c on c.productPageID = pp.productPageID " +
                "where pp.state = 'ACTIVE' " +
                "   and pp.categoryMain = '가구' ";
        List<Object> getFurniturePPsParams = new ArrayList<>();


        // 검색어
        if(getFurnituresReq.getSearchWord() != null) {
            getFurniturePPsQuery += "   and pageTitle like '%" + getFurnituresReq.getSearchWord() + "%' ";

            // 검색어 저장
            saveSearchWord(getFurnituresReq.getSearchWord());
        }
        // 색
        if(getFurnituresReq.getColor() != null) {
            for(int i=0; i <= getFurnituresReq.getColor().length-1; i++) {
                getFurniturePPsQuery += "and c.has" + getFurnituresReq.getColor()[i] + " = 1 ";
            }
        }
        //특가
        if(getFurnituresReq.getIsSpecialPrice() != null) {
            getFurniturePPsParams.add(getFurnituresReq.getIsSpecialPrice());
            getFurniturePPsQuery += "and isSpecialPrice = ? ";
        }
        // 렌탈 여부
        if(getFurnituresReq.getIsRental() != null){
            getFurniturePPsParams.add(getFurnituresReq.getIsRental());
            getFurniturePPsQuery += "and isRental = ? ";
        }
        // 해외 직구 여부
        if(getFurnituresReq.getIsCrossBorderShopping() != null){
            getFurniturePPsParams.add(getFurnituresReq.getIsCrossBorderShopping());
            getFurniturePPsQuery += "and isCrossBorderShopping = ? ";
        }
        // 리퍼 상품 여부
        if(getFurnituresReq.getIsRefurbished() != null){
            getFurniturePPsParams.add(getFurnituresReq.getIsRefurbished());
            getFurniturePPsQuery += "and isRefurbished = ? ";
        }
        // 최소 금액
        if(getFurnituresReq.getMinPrice() != null){
            getFurniturePPsParams.add(getFurnituresReq.getMinPrice());
            getFurniturePPsQuery += "and price >= ? ";
        }
        // 최대 금액
        if(getFurnituresReq.getMaxPrice() != null){
            getFurniturePPsParams.add(getFurnituresReq.getMaxPrice());
            getFurniturePPsQuery += "and price <= ? ";
        }
        // 배송 방법
        if(getFurnituresReq.getShipMethod() != null){
            for(int i=0; i <= getFurnituresReq.getShipMethod().length-1; i++) {
                if (getFurnituresReq.getShipMethod().length == 1) {
                    getFurniturePPsParams.add(getFurnituresReq.getShipMethod()[i]);
                    getFurniturePPsQuery += "and pp.shipMethod = ? ";
                } else {
                    if (i == 0) {
                        getFurniturePPsParams.add(getFurnituresReq.getShipMethod()[i]);
                        getFurniturePPsQuery += "and shipMethod in (?";
                    }
                    else if (i == getFurnituresReq.getShipMethod().length - 1) {
                        getFurniturePPsParams.add(getFurnituresReq.getShipMethod()[i]);
                        getFurniturePPsQuery += " , ?) ";
                    }
                    else {
                        getFurniturePPsParams.add(getFurnituresReq.getShipMethod()[i]);
                        getFurniturePPsQuery += ", ? ";
                    }
                }
            }
        }

        // 정렬 순서
        if(getFurnituresReq.getOrder().equals("인기순")) {
            getFurniturePPsQuery += "order by score desc, r.avgRate desc";
        } else if(getFurnituresReq.getOrder().equals("판매순")) {
            getFurniturePPsQuery += "order by cnt_order desc, score desc";
        } else if(getFurnituresReq.getOrder().equals("낮은가격순")) {
            getFurniturePPsQuery += "order by price, score desc";
        } else if(getFurnituresReq.getOrder().equals("높은가격순")) {
            getFurniturePPsQuery += "order by price desc, score desc";
        } else if(getFurnituresReq.getOrder().equals("리뷰많은순")) {
            getFurniturePPsQuery += "order by cntReview desc, score desc";
        }  else if(getFurnituresReq.getOrder().equals("최신순")) {
            getFurniturePPsQuery += "order by pp.createAt desc, score desc";
        }

        return this.jdbcTemplate.query(getFurniturePPsQuery,
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
                getFurniturePPsParams.toArray());
    }

    // 가구 - 소파 검색
    public List<GetProductPageRes> getFurnitureSofaPPs(GetFurnitureSofasReq getFurnitureSofasReq) {
        String getFurnitureSofaPPsQuery;
        List<Object> getFurnitureSofaPPsParams = new ArrayList<>();

        if(getFurnitureSofasReq.getCategoryDetail().equals("소파")) {
            getFurnitureSofaPPsQuery = "select pp.productPageID, categoryMain, categoryMid, productID, mainImageLink, pageTitle, " +
                    "r.avgRate, r.cntReview, (10*IFNULL(avgRate,0) + 3*IFNULL(cntReview,0) + 5*IFNULL(cnt_order,0)) score, price, dcRate, " +
                    "(price*(100-dcRate)/100) dcPrice, dcEndDate, productInfoImageLink, isSpecialPrice, isTodaysDeal, isRental, isCrossBorderShopping," +
                    "isRefurbished, shipMethod, shipFee, paymentMethod, additionalShipFee, additionalEtcAreaFee, isIsolatedAreaAvailable, " +
                    "isJejuAreaAvailable, proratedShipFee, returnShipFee, exchangeShipFee, returnAddress, pointRate, " +
                    "pp.createAt, pp.updateAt, pp.state, pp.brandID, b.brandName " +
                    "from product_page pp left outer join product_sofa ps on (pp.productID = ps.sofaID) " +
                    "   join brand b on (pp.brandID = b.brandID) " +
                    "   left outer join (select productPageID, TRUNCATE(AVG(rate),2) avgRate, COUNT(reviewID) cntReview FROM review GROUP BY productPageID) r USING(productPageID) " +
                    "   left outer join (select productPageID, count(orderListID) cnt_order from order_list) ol on pp.productPageID = ol.productPageID " +
                    "   left outer join color c on c.productPageID = pp.productPageID " +
                    "where pp.state = 'ACTIVE' " +
                    "   and pp.categoryMain = '가구' " +
                    "   and pp.categoryMid = '소파' ";
        } else {
            getFurnitureSofaPPsQuery = "select pp.productPageID, categoryMain, categoryMid, productID, mainImageLink, pageTitle, " +
                    "r.avgRate, r.cntReview, (10*IFNULL(avgRate,0) + 3*IFNULL(cntReview,0) + 5*IFNULL(cnt_order,0)) score, price, dcRate, " +
                    "(price*(100-dcRate)/100) dcPrice, dcEndDate, productInfoImageLink, isSpecialPrice, isTodaysDeal, isRental, isCrossBorderShopping," +
                    "isRefurbished, shipMethod, shipFee, paymentMethod, additionalShipFee, additionalEtcAreaFee, isIsolatedAreaAvailable, " +
                    "isJejuAreaAvailable, proratedShipFee, returnShipFee, exchangeShipFee, returnAddress, pointRate, " +
                    "pp.createAt, pp.updateAt, pp.state, pp.brandID, b.brandName " +
                    "from product_page pp left outer join product_sofa ps on (pp.productID = ps.sofaID) " +
                    "   join brand b on (pp.brandID = b.brandID) " +
                    "   left outer join (select productPageID, TRUNCATE(AVG(rate),2) avgRate, COUNT(reviewID) cntReview FROM review GROUP BY productPageID) r USING(productPageID) " +
                    "   left outer join (select productPageID, count(orderListID) cnt_order from order_list) ol on pp.productPageID = ol.productPageID " +
                    "   left outer join color c on c.productPageID = pp.productPageID " +
                    "where pp.state = 'ACTIVE' " +
                    "   and pp.categoryMain = '가구' " +
                    "   and pp.categoryMid = '소파' " +
                    "   and ps.categorySofa = ? ";

            // 카테고리 매개변수 추가
            getFurnitureSofaPPsParams.add(getFurnitureSofasReq.getCategoryDetail());
        }

        if(getFurnitureSofasReq.getCategoryDetail().equals("소파")
                || getFurnitureSofasReq.getCategoryDetail().equals("일반소파")
                || getFurnitureSofasReq.getCategoryDetail().equals("리클라이너")
                || getFurnitureSofasReq.getCategoryDetail().equals("소파베드")
                || getFurnitureSofasReq.getCategoryDetail().equals("좌식소파")
                || getFurnitureSofasReq.getCategoryDetail().equals("소파스툴")) {
            // 공통
            // 검색어
            if(getFurnitureSofasReq.getSearchWord() != null) {
                // 검색어 저장
                saveSearchWord(getFurnitureSofasReq.getSearchWord());
                getFurnitureSofaPPsQuery += "   and pageTitle like '%" + getFurnitureSofasReq.getSearchWord() + "%' ";
            }

            // 소재
            if(getFurnitureSofasReq.getMaterial() != null){
                for(int i=0; i <= getFurnitureSofasReq.getMaterial().length-1; i++) {
                    if (getFurnitureSofasReq.getMaterial().length == 1) {
                        getFurnitureSofaPPsParams.add(getFurnitureSofasReq.getMaterial()[i]);
                        getFurnitureSofaPPsQuery += "and ps.material = ? ";
                    } else {
                        if (i == 0) {
                            getFurnitureSofaPPsParams.add(getFurnitureSofasReq.getMaterial()[i]);
                            getFurnitureSofaPPsQuery += "and ps.material in (?";
                        }
                        else if (i == getFurnitureSofasReq.getMaterial().length - 1) {
                            getFurnitureSofaPPsParams.add(getFurnitureSofasReq.getMaterial()[i]);
                            getFurnitureSofaPPsQuery += " , ?) ";
                        }
                        else {
                            getFurnitureSofaPPsParams.add(getFurnitureSofasReq.getMaterial()[i]);
                            getFurnitureSofaPPsQuery += ", ? ";
                        }
                    }
                }
            }

            // 색
            if(getFurnitureSofasReq.getColor() != null) {
                for(int i=0; i <= getFurnitureSofasReq.getColor().length-1; i++) {
                    getFurnitureSofaPPsQuery += "and c.has" + getFurnitureSofasReq.getColor()[i] + " = 1 ";
                }
            }
            //특가
            if(getFurnitureSofasReq.getIsSpecialPrice() != null) {
                getFurnitureSofaPPsParams.add(getFurnitureSofasReq.getIsSpecialPrice());
                getFurnitureSofaPPsQuery += "and isSpecialPrice = ? ";
            }
            // 렌탈 여부
            if(getFurnitureSofasReq.getIsRental() != null){
                getFurnitureSofaPPsParams.add(getFurnitureSofasReq.getIsRental());
                getFurnitureSofaPPsQuery += "and isRental = ? ";
            }
            // 해외 직구 여부
            if(getFurnitureSofasReq.getIsCrossBorderShopping() != null){
                getFurnitureSofaPPsParams.add(getFurnitureSofasReq.getIsCrossBorderShopping());
                getFurnitureSofaPPsQuery += "and isCrossBorderShopping = ? ";
            }
            // 리퍼 상품 여부
            if(getFurnitureSofasReq.getIsRefurbished() != null){
                getFurnitureSofaPPsParams.add(getFurnitureSofasReq.getIsRefurbished());
                getFurnitureSofaPPsQuery += "and isRefurbished = ? ";
            }
            // 최소 금액
            if(getFurnitureSofasReq.getMinPrice() != null){
                getFurnitureSofaPPsParams.add(getFurnitureSofasReq.getMinPrice());
                getFurnitureSofaPPsQuery += "and price >= ? ";
            }
            // 최대 금액
            if(getFurnitureSofasReq.getMaxPrice() != null){
                getFurnitureSofaPPsParams.add(getFurnitureSofasReq.getMaxPrice());
                getFurnitureSofaPPsQuery += "and price <= ? ";
            }
            // 배송 방법
            if(getFurnitureSofasReq.getShipMethod() != null){
                for(int i=0; i <= getFurnitureSofasReq.getShipMethod().length-1; i++) {
                    if (getFurnitureSofasReq.getShipMethod().length == 1) {
                        getFurnitureSofaPPsParams.add(getFurnitureSofasReq.getShipMethod()[i]);
                        getFurnitureSofaPPsQuery += "and pp.shipMethod = ? ";
                    } else {
                        if (i == 0) {
                            getFurnitureSofaPPsParams.add(getFurnitureSofasReq.getShipMethod()[i]);
                            getFurnitureSofaPPsQuery += "and shipMethod in (?";
                        }
                        else if (i == getFurnitureSofasReq.getShipMethod().length - 1) {
                            getFurnitureSofaPPsParams.add(getFurnitureSofasReq.getShipMethod()[i]);
                            getFurnitureSofaPPsQuery += " , ?) ";
                        }
                        else {
                            getFurnitureSofaPPsParams.add(getFurnitureSofasReq.getShipMethod()[i]);
                            getFurnitureSofaPPsQuery += ", ? ";
                        }
                    }
                }
            }
            //상세 사이즈
            // 최소 가로
            if(getFurnitureSofasReq.getMinFullWidth() != null){
                getFurnitureSofaPPsParams.add(getFurnitureSofasReq.getMinFullWidth());
                getFurnitureSofaPPsQuery += "and ps.fullWidth >= ? ";
            }
            // 최대 가로
            if(getFurnitureSofasReq.getMaxFullWidth() != null){
                getFurnitureSofaPPsParams.add(getFurnitureSofasReq.getMaxFullWidth());
                getFurnitureSofaPPsQuery += "and ps.fullWidth <= ? ";
            }
            // 최소 세로
            if(getFurnitureSofasReq.getMinFullDepth() != null){
                getFurnitureSofaPPsParams.add(getFurnitureSofasReq.getMinFullDepth());
                getFurnitureSofaPPsQuery += "and ps.fullDepth >= ? ";
            }
            // 최대 세로
            if(getFurnitureSofasReq.getMaxFullDepth() != null){
                getFurnitureSofaPPsParams.add(getFurnitureSofasReq.getMaxFullDepth());
                getFurnitureSofaPPsQuery += "and ps.fullDepth <= ? ";
            }
            // 최소 높이
            if(getFurnitureSofasReq.getMinFullHeight() != null){
                getFurnitureSofaPPsParams.add(getFurnitureSofasReq.getMinFullHeight());
                getFurnitureSofaPPsQuery += "and ps.fullHeight >= ? ";
            }
            // 최대 높이
            if(getFurnitureSofasReq.getMaxFullHeight() != null){
                getFurnitureSofaPPsParams.add(getFurnitureSofasReq.getMaxFullHeight());
                getFurnitureSofaPPsQuery += "and ps.fullHeight <= ? ";
            }

            //개별 부분
            // 일반소파, 리클라이너, 소파베드, 좌식소파
            if(getFurnitureSofasReq.getCategoryDetail().equals("소파")
                    || getFurnitureSofasReq.getCategoryDetail().equals("일반소파")
                    || getFurnitureSofasReq.getCategoryDetail().equals("리클라이너")
                    || getFurnitureSofasReq.getCategoryDetail().equals("소파베드")
                    || getFurnitureSofasReq.getCategoryDetail().equals("좌식소파")){
                //사용인원
                if(getFurnitureSofasReq.getCapacity() != null){
                    for(int i=0; i <= getFurnitureSofasReq.getCapacity().length-1; i++) {
                        if (getFurnitureSofasReq.getCapacity().length == 1) {
                            getFurnitureSofaPPsParams.add(getFurnitureSofasReq.getCapacity()[i]);
                            getFurnitureSofaPPsQuery += "and ps.capacity = ? ";
                        } else {
                            if (i == 0) {
                                getFurnitureSofaPPsParams.add(getFurnitureSofasReq.getCapacity()[i]);
                                getFurnitureSofaPPsQuery += "and ps.capacity in (?";
                            }
                            else if (i == getFurnitureSofasReq.getCapacity().length - 1) {
                                getFurnitureSofaPPsParams.add(getFurnitureSofasReq.getCapacity()[i]);
                                getFurnitureSofaPPsQuery += " , ?) ";
                            }
                            else {
                                getFurnitureSofaPPsParams.add(getFurnitureSofasReq.getCapacity()[i]);
                                getFurnitureSofaPPsQuery += ", ? ";
                            }
                        }
                    }
                }
                //쿠션감
                if(getFurnitureSofasReq.getCushionFeeling() != null){
                    for(int i=0; i <= getFurnitureSofasReq.getCushionFeeling().length-1; i++) {
                        if (getFurnitureSofasReq.getCushionFeeling().length == 1) {
                            getFurnitureSofaPPsParams.add(getFurnitureSofasReq.getCushionFeeling()[i]);
                            getFurnitureSofaPPsQuery += "and ps.cushionFeeling = ? ";
                        } else {
                            if (i == 0) {
                                getFurnitureSofaPPsParams.add(getFurnitureSofasReq.getCushionFeeling()[i]);
                                getFurnitureSofaPPsQuery += "and ps.cushionFeeling in (?";
                            }
                            else if (i == getFurnitureSofasReq.getCushionFeeling().length - 1) {
                                getFurnitureSofaPPsParams.add(getFurnitureSofasReq.getCushionFeeling()[i]);
                                getFurnitureSofaPPsQuery += " , ?) ";
                            }
                            else {
                                getFurnitureSofaPPsParams.add(getFurnitureSofasReq.getCushionFeeling()[i]);
                                getFurnitureSofaPPsQuery += ", ? ";
                            }
                        }
                    }
                }
                //방수
                if(getFurnitureSofasReq.getIsWaterproof() != null){
                    getFurnitureSofaPPsParams.add(getFurnitureSofasReq.getIsWaterproof());
                    getFurnitureSofaPPsQuery += "and isWaterproof = ? ";
                }
            }
            // 일반소파, 소파베드, 좌식소파
            if(getFurnitureSofasReq.getCategoryDetail().equals("소파")
                    || getFurnitureSofasReq.getCategoryDetail().equals("일반소파")
                    || getFurnitureSofasReq.getCategoryDetail().equals("소파베드")
                    || getFurnitureSofasReq.getCategoryDetail().equals("좌식소파")){
                //커버분리
                if(getFurnitureSofasReq.getIsCoverRemovable() != null){
                    getFurnitureSofaPPsParams.add(getFurnitureSofasReq.getIsCoverRemovable());
                    getFurnitureSofaPPsQuery += "and isCoverRemovable = ? ";
                }
            }
            // 일반소파, 소파베드, 소파스툴
            if(getFurnitureSofasReq.getCategoryDetail().equals("일반소파")
                    || getFurnitureSofasReq.getCategoryDetail().equals("소파베드")
                    || getFurnitureSofasReq.getCategoryDetail().equals("소파스툴")){
                //다리
                if(getFurnitureSofasReq.getHasLegs() != null){
                    getFurnitureSofaPPsParams.add(getFurnitureSofasReq.getHasLegs());
                    getFurnitureSofaPPsQuery += "and hasLegs = ? ";
                }
            }
            // 리클라이너
            if(getFurnitureSofasReq.getCategoryDetail().equals("리클라이너")) {
                //리클라이닝 좌석
                if(getFurnitureSofasReq.getRecliningSeat() != null){
                    for(int i=0; i <= getFurnitureSofasReq.getRecliningSeat().length-1; i++) {
                        if (getFurnitureSofasReq.getRecliningSeat().length == 1) {
                            getFurnitureSofaPPsParams.add(getFurnitureSofasReq.getRecliningSeat()[i]);
                            getFurnitureSofaPPsQuery += "and ps.recliningSeat = ? ";
                        } else {
                            if (i == 0) {
                                getFurnitureSofaPPsParams.add(getFurnitureSofasReq.getRecliningSeat()[i]);
                                getFurnitureSofaPPsQuery += "and ps.recliningSeat in (?";
                            }
                            else if (i == getFurnitureSofasReq.getRecliningSeat().length - 1) {
                                getFurnitureSofaPPsParams.add(getFurnitureSofasReq.getRecliningSeat()[i]);
                                getFurnitureSofaPPsQuery += " , ?) ";
                            }
                            else {
                                getFurnitureSofaPPsParams.add(getFurnitureSofasReq.getRecliningSeat()[i]);
                                getFurnitureSofaPPsQuery += ", ? ";
                            }
                        }
                    }
                }
                //수동/전동
                if(getFurnitureSofasReq.getIsAutomatic() != null){
                    getFurnitureSofaPPsParams.add(getFurnitureSofasReq.getIsAutomatic());
                    getFurnitureSofaPPsQuery += "and isAutomatic = ? ";
                }
            }
            // 일반소파
            if(getFurnitureSofasReq.getCategoryDetail().equals("소파")
                    || getFurnitureSofasReq.getCategoryDetail().equals("일반소파")) {
                //형태
                if(getFurnitureSofasReq.getShape() != null){
                    for(int i=0; i <= getFurnitureSofasReq.getShape().length-1; i++) {
                        if (getFurnitureSofasReq.getShape().length == 1) {
                            getFurnitureSofaPPsParams.add(getFurnitureSofasReq.getShape()[i]);
                            getFurnitureSofaPPsQuery += "and ps.shape = ? ";
                        } else {
                            if (i == 0) {
                                getFurnitureSofaPPsParams.add(getFurnitureSofasReq.getShape()[i]);
                                getFurnitureSofaPPsQuery += "and ps.shape in (?";
                            }
                            else if (i == getFurnitureSofasReq.getShape().length - 1) {
                                getFurnitureSofaPPsParams.add(getFurnitureSofasReq.getShape()[i]);
                                getFurnitureSofaPPsQuery += " , ?) ";
                            }
                            else {
                                getFurnitureSofaPPsParams.add(getFurnitureSofasReq.getShape()[i]);
                                getFurnitureSofaPPsQuery += ", ? ";
                            }
                        }
                    }
                }
                //헤드레스트
                if(getFurnitureSofasReq.getHasHeadrest() != null){
                    getFurnitureSofaPPsParams.add(getFurnitureSofasReq.getHasHeadrest());
                    getFurnitureSofaPPsQuery += "and hasHeadrest = ? ";
                }
                //팔걸이
                if(getFurnitureSofasReq.getHasElbowrest() != null){
                    getFurnitureSofaPPsParams.add(getFurnitureSofasReq.getHasElbowrest());
                    getFurnitureSofaPPsQuery += "and hasElbowrest = ? ";
                }
                //스툴포함 여부
                if(getFurnitureSofasReq.getHasStool() != null){
                    getFurnitureSofaPPsParams.add(getFurnitureSofasReq.getHasStool());
                    getFurnitureSofaPPsQuery += "and hasStool = ? ";
                }
                //오염방지
                if(getFurnitureSofasReq.getIsAntipollution() != null){
                    getFurnitureSofaPPsParams.add(getFurnitureSofasReq.getIsAntipollution());
                    getFurnitureSofaPPsQuery += "and isAntipollution = ? ";
                }
            }

            // 정렬 순서
            if(getFurnitureSofasReq.getOrder().equals("인기순")) {
                getFurnitureSofaPPsQuery += "order by score desc, r.avgRate desc";
            } else if(getFurnitureSofasReq.getOrder().equals("판매순")) {
                getFurnitureSofaPPsQuery += "order by cnt_order desc, score desc";
            } else if(getFurnitureSofasReq.getOrder().equals("낮은가격순")) {
                getFurnitureSofaPPsQuery += "order by price, score desc";
            } else if(getFurnitureSofasReq.getOrder().equals("높은가격순")) {
                getFurnitureSofaPPsQuery += "order by price desc, score desc";
            } else if(getFurnitureSofasReq.getOrder().equals("리뷰많은순")) {
                getFurnitureSofaPPsQuery += "order by cntReview desc, score desc";
            }  else if(getFurnitureSofasReq.getOrder().equals("최신순")) {
                getFurnitureSofaPPsQuery += "order by pp.createAt desc, score desc";
            }
        } else
            // 상세 카테고리 값이 틀리면
            throw new BaseException(BaseResponseStatus.GET_SEARCH_CATEGORY_DETAIL);

        System.out.println(getFurnitureSofaPPsQuery);

        return this.jdbcTemplate.query(getFurnitureSofaPPsQuery,
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
                getFurnitureSofaPPsParams.toArray());
    }

    // 검색어 저장
    public void saveSearchWord (String searchWord) {
        String saveSearchWordQuery = "insert into search_word_history (searchWord) value(?)";
        String saveSearchWordParam = searchWord;

        this.jdbcTemplate.update(saveSearchWordQuery, saveSearchWordParam);
    }

    public List<GetPopularSearchWordsRes> getPopularSearchWords() {
        String getPopularSearchWordsQuery =
                "select searchWord, count(searchWord) cntSearchWord " +
                "from search_word_history " +
                "where createAt >= DATE_SUB(createAt, INTERVAL 2 WEEK) " +
                "group by searchWord " +
                "order by cntSearchWord desc " +
                "limit 10";

        return this.jdbcTemplate.query(getPopularSearchWordsQuery,
                (rs, rownum) -> new GetPopularSearchWordsRes(
                        rs.getString("searchWord"),
                        rs.getInt("cntSearchWord")
                ));
    }

    public GetUnifiedSearchRes getUnifiedSearch(String searchWord, String[] hashTag) {
        String getUnifiedSearchQuery = "select pp.productPageID, categoryMain, categoryMid, productID, mainImageLink, pageTitle, " +
                "r.avgRate, r.cntReview, (10*IFNULL(avgRate,0) + 3*IFNULL(cntReview,0) + 5*IFNULL(cnt_order,0)) score, price, dcRate, " +
                "(price*(100-dcRate)/100) dcPrice, dcEndDate, " +
                "productInfoImageLink, isSpecialPrice, isTodaysDeal, isRental, isCrossBorderShopping, isRefurbished, shipMethod, shipFee, paymentMethod, additionalShipFee, " +
                "additionalEtcAreaFee, isIsolatedAreaAvailable, isJejuAreaAvailable, proratedShipFee, returnShipFee, exchangeShipFee, " +
                "returnAddress, pointRate, pp.createAt, pp.updateAt, pp.state, pp.brandID, b.brandName " +
                "from product_page pp join brand b on (pp.brandID = b.brandID) " +
                "   left outer join (select productPageID, TRUNCATE(AVG(rate),2) avgRate, COUNT(reviewID) cntReview FROM review GROUP BY productPageID) r on r.productPageID = pp.productPageID  " +
                "   left outer join (select productPageID, count(orderListID) cnt_order from order_list) ol on pp.productPageID = ol.productPageID " +
                "where pp.state = 'ACTIVE' and pageTitle like '%" +
                searchWord +
                "%' " +
                "order by score desc, r.avgRate desc;";

        GetUnifiedSearchRes getUnifiedSearchRes = new GetUnifiedSearchRes();

        try{
            getUnifiedSearchRes.setProductPageList(this.jdbcTemplate.query(getUnifiedSearchQuery,
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
                    )));
        } catch (EmptyResultDataAccessException e) {
            getUnifiedSearchRes.setProductPageList(null);
        }


        List<Object> getUnifiedSearchParams = new ArrayList<>();

        getUnifiedSearchQuery = "select distinct(p.photoID), p.photoLink, u.userNickname " +
                "from photo p inner join user u on p.userID = u.userID " +
                "   join photo_hashtag ph on p.photoID = ph.photoID " +
                "where p.state = 'ACTIVE' " +
                "   and (p.content like '%" + searchWord + "%' ";

        if(hashTag != null) {
            for(int i = 0; i <= hashTag.length-1; i++) {
                if(hashTag.length == 1) {
                    getUnifiedSearchParams.add(hashTag[i]);
                    getUnifiedSearchQuery += "or keyword in (?)) ";
                } else {
                    getUnifiedSearchParams.add(hashTag[i]);

                    if(i == 0)
                        getUnifiedSearchQuery += "or keyword in (?";
                    else if(i == hashTag.length-1)
                        getUnifiedSearchQuery += ", ?)) ";
                    else
                        getUnifiedSearchQuery += ", ?";
                }
            }
            getUnifiedSearchQuery += "order by p.createAt desc limit 9";
        } else {
            getUnifiedSearchQuery += ") order by p.createAt desc limit 9";
        }

        try{
            getUnifiedSearchRes.setPhotoList(this.jdbcTemplate.query(getUnifiedSearchQuery,
                    (rs, rowNum) -> new GetPhotoSearchRes(
                            rs.getString("userNickname"),
                            rs.getInt("photoID"),
                            rs.getString("photoLink")
                    ),
                    getUnifiedSearchParams.toArray()));
        } catch (EmptyResultDataAccessException e) {
            getUnifiedSearchRes.setPhotoList(null);
        }

        return getUnifiedSearchRes;
    }
}
