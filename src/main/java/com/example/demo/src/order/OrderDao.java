package com.example.demo.src.order;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.cart.CartDao;
import com.example.demo.src.coupon.CouponDao;
import com.example.demo.src.order.model.*;
import com.example.demo.src.point.PointDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository
public class OrderDao {
    private JdbcTemplate jdbcTemplate;
    private CouponDao couponDao;
    private CartDao cartDao;
    private PointDao pointDao;

    @Autowired
    public void setDataSource(DataSource dataSource, CouponDao couponDao, CartDao cartDao, PointDao pointDao){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.couponDao = couponDao;
        this.cartDao = cartDao;
        this.pointDao = pointDao;
    }


    // TODO : 코드 리팩토링 해보기
    public GetOrderPageRes getOrderPage(int userID, Integer[] cartID) {
        // 주문 정보 가져오기
        // 기본 배송지가 있으면, 기본 배송지 가져오기
        // 기본 배송지가 없으면, 가장 최근에 등록한 배송지 가져오기
        String getOrderPageQuery = "select * " +
                        "from user join user_shipping_address using (userID) " +
                        "   join (select sum(point) sumPoint, userID from user_point where userID = ?) p on (p.userID = user.userID)" +
                        "where " +
                        "    user.userID = ? " +
                        "  and " +
                        "    isDefault = if((select exists(select userAddressID from user_shipping_address where userID = ? and isDefault = 1)), 1, 0) " +
                        "order by user_shipping_address.userAddressID desc " +
                        "limit 1";
        List<Object> getOrderPageCartParams = new ArrayList<>();
        GetOrderPageRes getOrderPageRes;

        // 매개변수 넣기
        getOrderPageCartParams.add(userID);
        getOrderPageCartParams.add(userID);
        getOrderPageCartParams.add(userID);

        try{
            getOrderPageRes =
                    this.jdbcTemplate.queryForObject(getOrderPageQuery,
                            (rs, rownum) -> new GetOrderPageRes (
                                    rs.getString("addressName"),
                                    rs.getString("recipientName"),
                                    rs.getString("recipientContact"),
                                    rs.getString("mailingAddress"),
                                    rs.getString("address"),
                                    rs.getString("detailedAddress"),
                                    rs.getString("recipientName"),
                                    rs.getString("recipientContact"),
                                    rs.getString("email"),
                                    rs.getInt("sumPoint")
                            ),
                            getOrderPageCartParams.toArray()
                    );
        } catch (EmptyResultDataAccessException e) {
            getOrderPageRes = new GetOrderPageRes();
        }

        getOrderPageCartParams.clear();


        // 장바구니에서 가져오기
        getOrderPageQuery = "select " +
                "cartID, cart.productPageID, cart.userID, cart.brandID, brandName, mainImageLink, pageTitle, " +
                "(product_page.price*(100-product_page.dcRate)/100) price, shipMethod, shipFee, " +
                "paymentMethod, productOption, quantity, cart.createAt, cart.updateAt, cart.state " +
                "from cart join product_page using (productPageID) " +
                "where cart.state = 'ACTIVE' and userID = ? and cartID in (";

        // 매개변수 값 넣기
        getOrderPageCartParams.add(userID);

        for (int id : cartID) {
            getOrderPageCartParams.add(id);
            if(id == cartID[cartID.length - 1]) {
                getOrderPageQuery += "?) order by cartID";
                break;
            }
            getOrderPageQuery += "?, ";
        }

        getOrderPageRes.setCartList((this.jdbcTemplate.query(getOrderPageQuery,
                (rs, rownum) -> new GetOrderPageRes.Cart (
                        rs.getInt("cartID"),
                        rs.getInt("productPageID"),
                        rs.getInt("brandID"),
                        rs.getString("brandName"),
                        rs.getString("mainImageLink"),
                        rs.getString("pageTitle"),
                        rs.getString("shipMethod"),
                        rs.getInt("shipFee"),
                        rs.getString("productOption"),
                        rs.getInt("price"),
                        rs.getInt("quantity"),
                        rs.getString("createAt"),
                        rs.getString("updateAt"),
                        rs.getString("state")
                ),
                getOrderPageCartParams.toArray()
        )));
        getOrderPageCartParams.clear();

        if(getOrderPageRes.getCartList() == null){
            throw new BaseException(BaseResponseStatus.GET_ORDER_PAGE_CART_LIST_FAIL);
        }

        int totalOrderAmount = 0;
        // 총 주문금액 계산
        for(GetOrderPageRes.Cart cart : getOrderPageRes.getCartList()){
            totalOrderAmount += (cart.getPrice() * cart.getQuantity());
        }

        // 최소 주문금액 이상인지 확인하기
        // 사용 가능한 쿠폰 가져오기
        getOrderPageQuery =
                "select userCouponID, c.couponID, couponTitle, dcRate, dcAmount, minOrderAmount, " +
                        "SEC_TO_TIME(expirationDate - current_timestamp) lastTime, uc.createAt, uc.updateAt, uc.state "+
                "from coupon c join user_coupon uc using (couponID) " +
                "where uc.state = 'ACTIVE' " +
                        "and couponID in " +
                        "(select distinct c.couponID " +
                        "from user_coupon uc join coupon c on uc.couponID = c.couponID " +
                        "    join mapping_coupon_product_page mcpp on c.couponID = mcpp.couponID " +
                        "    where c.minOrderAmount <= ? " +
                        "       and productPageID in (";

        // 매개변수 - 상품 페이지 값 넣기
        getOrderPageCartParams.add(totalOrderAmount);
        for(int i=0; i < getOrderPageRes.getCartList().size(); i++) {
            if(i>=1){
                // 중복을 피하기 위해
                // 이전 productID와 현재 productID가 같으면 스킵
                if(getOrderPageRes.getCartList().get(i-1).getProductPageID() == getOrderPageRes.getCartList().get(i).getProductPageID()) {
                    // 마지막 인덱스이면,
                    if (i == getOrderPageRes.getCartList().size() - 1) {
                        getOrderPageQuery += "))";
                        break;
                    }
                    continue;
                }
            }
            getOrderPageCartParams.add(getOrderPageRes.getCartList().get(i).getProductPageID());

            if(i == getOrderPageRes.getCartList().size()-1) {
                getOrderPageQuery += "?))";
                break;
            }

            if(i == 0) {
                getOrderPageQuery += "?";

            } else {
                getOrderPageQuery += ", ?";
            }
        }

        getOrderPageRes.setCouponList((this.jdbcTemplate.query(getOrderPageQuery,
                (rs, rownum) -> new GetOrderPageRes.Coupon (
                        rs.getInt("userCouponID"),
                        rs.getInt("couponID"),
                        rs.getString("couponTitle"),
                        rs.getInt("dcRate"),
                        rs.getInt("dcAmount"),
                        rs.getInt("minOrderAmount"),
                        rs.getString("lastTime"),
                        rs.getString("createAt"),
                        rs.getString("updateAt"),
                        rs.getString("state")
                ),
                getOrderPageCartParams.toArray()
        )));

        return getOrderPageRes;
    }

    public List<GetUserAddressRes> getUserAddress(int userID) {
        String getUserAddressQuery = "select * " +
                "from user_shipping_address " +
                "where userID = ? " +
                "order by isDefault desc, userAddressID";
        int getUserAddressParam = userID;

        return this.jdbcTemplate.query(getUserAddressQuery,
                (rs,rownum) -> new GetUserAddressRes(
                        rs.getInt("userAddressID"),
                        rs.getString("addressName"),
                        rs.getString("recipientName"),
                        rs.getString("recipientContact"),
                        rs.getString("mailingAddress"),
                        rs.getString("address"),
                        rs.getString("detailedAddress"),
                        rs.getInt("isDefault")
                ),
                getUserAddressParam);
    }

    public int checkAddressNameDuplication(int userID, String addressName) {
        String checkAddressNameDuplicationQuery =
                "select exists(select userAddressID from user_shipping_address where userID = ? and addressName = ? )";
        Object[] checkAddressNameDuplicationParam = new Object[]{
                userID,
                addressName
        };

        return this.jdbcTemplate.queryForObject(checkAddressNameDuplicationQuery,
                int.class,
                checkAddressNameDuplicationParam
        );
    }

    public int createUserAddress(PostUserAddressReq postUserAddressReq) {
        String createUserAddressQuery;
        Object[] createUserAddressParams;

        // 기본 배송지로 설정한다면
        if(postUserAddressReq.getIsDefault() == 1){
            // 이전의 주소들은 기본배송지 해제
            createUserAddressQuery = "update user_shipping_address set isDefault=0 where userID=?";
            createUserAddressParams = new Object[]{
                    postUserAddressReq.getUserID()
            };

            this.jdbcTemplate.update(createUserAddressQuery, createUserAddressParams);
        }
        // 배송지 삽입
        createUserAddressQuery = "insert into user_shipping_address " +
                "    (addressName, recipientName, recipientContact, mailingAddress, address, detailedAddress, isDefault, userID) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        createUserAddressParams = new Object[]{
                postUserAddressReq.getAddressName(),
                postUserAddressReq.getRecipientName(),
                postUserAddressReq.getRecipientContact(),
                postUserAddressReq.getMailingAddress(),
                postUserAddressReq.getAddress(),
                postUserAddressReq.getDetailedAddress(),
                postUserAddressReq.getIsDefault(),
                postUserAddressReq.getUserID()
        };

        this.jdbcTemplate.update(createUserAddressQuery, createUserAddressParams);

        String lastInserIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInserIdQuery,int.class);
    }

    public Object[] createUserOrders(PostUserOrderReq postUserOrderReq) {
        String registUserOrdersQuery;
        Object[] registUserOrdersParams;
        List<Integer> orderID = new ArrayList<>();
        String lastInserIdQuery;

        for(PostUserOrderReq.Cart cart : postUserOrderReq.getCartList()) {
            registUserOrdersQuery = "insert into order_list " +
                    "(orderStatus, pageTitle, productOption, quantity, totalAmount, shipMethod, productPrice, prepaidShipFee, " +
                    " usedPointAmount, couponDCAmount, paymentMethod, orderer, contact, email, memo, productPageID, userID, brandID) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            registUserOrdersParams = new Object[]{
                    postUserOrderReq.getOrderStatus(),
                    cart.getPageTitle(),
                    cart.getProductOption(),
                    cart.getQuantity(),
                    postUserOrderReq.getTotalAmount(),
                    cart.getShipMethod(),
                    cart.getPrice(),
                    cart.getShipFee(),
                    postUserOrderReq.getUsedPointAmount(),
                    postUserOrderReq.getCouponDCAmount(),
                    postUserOrderReq.getPaymentMethod(),
                    postUserOrderReq.getOrderer(),
                    postUserOrderReq.getContact(),
                    postUserOrderReq.getEmail(),
                    postUserOrderReq.getMemo(),
                    cart.getProductPageID(),
                    postUserOrderReq.getUserID(),
                    cart.getBrandID()
            };

            this.jdbcTemplate.update(registUserOrdersQuery, registUserOrdersParams);

            lastInserIdQuery = "select last_insert_id()";
            orderID.add(this.jdbcTemplate.queryForObject(lastInserIdQuery,int.class));
        }
        // CART INACTIVE 처리하기
        for(PostUserOrderReq.Cart cart : postUserOrderReq.getCartList()) {
            int result = cartDao.modifyCartState(cart.getCartID());
            if(result == 0) {
                throw new BaseException(BaseResponseStatus.MODIFY_CART_STATE_FAIL);
            }
        }

        // user COUPON INACTIVE 처리하기
        for(Integer userCouponID : postUserOrderReq.getCouponList()) {
            int result = couponDao.modifyUserCoupon(userCouponID);
            if(result == 0) {
                throw new BaseException(BaseResponseStatus.MODIFY_COUPON_USER_FAIL);
            }
        }
        return orderID.toArray();
    }

    public List<GetUserOrderRes> getUserOrders(int userID, String orderStatus) {
        String getUserOrdersQuery;
        Object[] getUserOrdersParams;
        if(orderStatus != null) {
            getUserOrdersQuery = "select * from order_list where userID = ? and orderStatus = ?";
            getUserOrdersParams = new Object[]{
                    userID,
                    orderStatus
            };
        } else {
            getUserOrdersQuery = "select * from order_list where userID = ?";
            getUserOrdersParams = new Object[]{
                    userID
            };
        }
        return this.jdbcTemplate.query(getUserOrdersQuery,
                (rs, rownum) -> new GetUserOrderRes(
                        rs.getInt("orderListID"),
                        rs.getInt("userID"),
                        rs.getInt("productPageID"),
                        rs.getInt("brandID"),
                        rs.getString("orderStatus"),
                        rs.getString("pageTitle"),
                        rs.getInt("productPrice"),
                        rs.getString("productOption"),
                        rs.getInt("quantity"),
                        rs.getInt("usedPointAmount"),
                        rs.getInt("couponDCAmount"),
                        rs.getInt("prepaidShipFee"),
                        rs.getInt("totalAmount"),
                        rs.getString("paymentMethod"),
                        rs.getString("shipMethod"),
                        rs.getString("shipCompletedDate"),
                        rs.getString("orderer"),
                        rs.getString("contact"),
                        rs.getString("email"),
                        rs.getString("createAt"),
                        rs.getString("updateAt"),
                        rs.getString("state")
                ),
                getUserOrdersParams);
    }
}
