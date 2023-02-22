package com.example.demo.src.cart;

import com.example.demo.src.cart.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class CartDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public GetCartRes getCart(int cartID) {
        String getCartQuery = "select " +
                "cartID, cart.productPageID, cart.userID, cart.brandID, brandName, mainImageLink, pageTitle, " +
                "(product_page.price*(100-product_page.dcRate)/100) price, shipMethod, shipFee, paymentMethod, " +
                "productOption, quantity, cart.createAt, cart.updateAt, cart.state " +
                "from cart join product_page using (productPageID) " +
                "where cartID = ?";
        int getCartParam = cartID;

        return this.jdbcTemplate.queryForObject(getCartQuery,
                (rs, rownum) -> new GetCartRes(
                        rs.getInt("cartID"),
                        rs.getInt("productPageID"),
                        rs.getInt("userID"),
                        rs.getInt("brandID"),
                        rs.getString("brandName"),
                        rs.getString("mainImageLink"),
                        rs.getString("pageTitle"),
                        rs.getInt("price"),
                        rs.getString("shipMethod"),
                        rs.getInt("shipFee"),
                        rs.getString("paymentMethod"),
                        rs.getString("productOption"),
                        rs.getInt("quantity"),
                        rs.getString("createAt"),
                        rs.getString("updateAt"),
                        rs.getString("state")
                ),
                getCartParam
        );
    }

    public List<GetCartRes> getUserCart(int userID, String state) {
        String getUserCartQuery = "select " +
                "cartID, cart.productPageID, cart.userID, cart.brandID, brandName, mainImageLink, pageTitle, " +
                "(product_page.price*(100-product_page.dcRate)/100) price, shipMethod, shipFee, paymentMethod, " +
                "productOption, quantity, cart.createAt, cart.updateAt, cart.state " +
                "from cart join product_page using (productPageID) " +
                "where cart.userID = ? and cart.state = ?";
        Object[] getUserCartParam = new Object[]{userID, state};

        return this.jdbcTemplate.query(getUserCartQuery,
                (rs, rownum) -> new GetCartRes(
                        rs.getInt("cartID"),
                        rs.getInt("productPageID"),
                        rs.getInt("userID"),
                        rs.getInt("brandID"),
                        rs.getString("brandName"),
                        rs.getString("mainImageLink"),
                        rs.getString("pageTitle"),
                        rs.getInt("price"),
                        rs.getString("shipMethod"),
                        rs.getInt("shipFee"),
                        rs.getString("paymentMethod"),
                        rs.getString("productOption"),
                        rs.getInt("quantity"),
                        rs.getString("createAt"),
                        rs.getString("updateAt"),
                        rs.getString("state")
                ),
                getUserCartParam
        );
    }

    public List<GetCartOptionRes> getProductOptions(int cartID) {
        String getProductOptionsQuery = "select product_page_option.optionID, product_page_option_detail.optionDetailID, " +
                "product_page_option.title, product_page_option_detail.titleDetail, isSelective, price,  " +
                "product_page_option.state state, product_page_option_detail.state stateDetail " +
                "from cart join product_page_option using (productPageID) " +
                "    join product_page_option_detail on (product_page_option.optionID = product_page_option_detail.optionID) " +
                "where cartID = ?";

        int getProductOptionsParam = cartID;

        return this.jdbcTemplate.query(getProductOptionsQuery,
                (rs, rownum) -> new GetCartOptionRes(
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

    public Integer checkDuplication(PostCartReq postCartReq) {
        String checkDuplicationQuery =
                "select exists(select cartID from cart where productPageID = ? and productOption = ? and state = 'ACTIVE')";
        Object[] checkDuplicationParams = new Object[]{
                postCartReq.getProductPageID(),
                postCartReq.getProductOption()
        };

        int result = this.jdbcTemplate.queryForObject(checkDuplicationQuery,
                int.class,
                checkDuplicationParams);

        if(result == 0) {
            // 중복된 데이터 값이 없을 경우
            return null;
        } else {
            // 중복된 데이터 값이 있을 경우
            checkDuplicationQuery = "select *" +
                    "from cart " +
                    "where productPageID = ?" +
                    "  and productOption = ?" +
                    "  and state = 'ACTIVE'";

            checkDuplicationParams = new Object[]{
                    postCartReq.getProductPageID(),
                    postCartReq.getProductOption()
            };
            Integer cartID = this.jdbcTemplate.queryForObject(checkDuplicationQuery,
                    (rs, rownum) -> new Integer(rs.getInt("cartID")),
                    checkDuplicationParams);

            return cartID;
        }
    }

    // 장바구니 수량 추가로 더하기
    public int addCartQuantity(int cartID, int quantity) {
        String addCartQuantityQuery = "update cart set quantity = quantity+? where cartID = ?";
        Object[] addCartQuantityParams = new Object[]{
                quantity,
                cartID
        };

        return this.jdbcTemplate.update(addCartQuantityQuery, addCartQuantityParams);
    }

    public Integer createCart(PostCartReq postCartReq) {
        String creatCartQuery = "insert into cart " +
                "    (quantity, productPageID, brandID, productOption, brandName, userID) " +
                "VALUES (?, ?, ?, ?, ?, ?);";
        Object[] createCartParams = new Object[]{
                postCartReq.getQuantity(),
                postCartReq.getProductPageID(),
                postCartReq.getBrandID(),
                postCartReq.getProductOption(),
                postCartReq.getBrandName(),
                postCartReq.getUserID()
        };
        this.jdbcTemplate.update(creatCartQuery, createCartParams);

        String lastInserIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInserIdQuery,int.class);
    }

    public int modifyCartQuantity(PatchCartReq patchCartReq) {
        String modifyCartQuantityQuery = "update cart set quantity = ? where cartID = ?";
        Object[] modifyCartQuantityParams = new Object[]{
                patchCartReq.getQuantity(),
                patchCartReq.getCartID()
        };

        return this.jdbcTemplate.update(modifyCartQuantityQuery, modifyCartQuantityParams);
    }

    // cart 비활성화
    public int modifyCartState(int cartID) {
        String modifyCartStateQuery = "update cart set state = 'INACTIVE' where cartID = ?";
        int modifyCartStateParam = cartID;

        return this.jdbcTemplate.update(modifyCartStateQuery, modifyCartStateParam);
    }

    public int deleteCart(int cartID) {
        String deleteCartQuery = "update cart set state = 'UNACTIVE' where cartID = ?";
        int deleteCartParam = cartID;

        return this.jdbcTemplate.update(deleteCartQuery, deleteCartParam);
    }
}
