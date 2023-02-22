package com.example.demo.src.product;

import com.example.demo.src.product.model.PostProductSofaReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class ProductDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public int checkProductSofaNameDuplication(PostProductSofaReq postProductSofaReq) {
        String checkProductSofaNameDuplicationQuery =
                "select exists(select sofaID from product_sofa where productSofaName = ? and state = 'ACTIVE')";
        String checkProductSofaNameDuplicationParam = postProductSofaReq.getProductSofaName();

        return this.jdbcTemplate.queryForObject(checkProductSofaNameDuplicationQuery, int.class, checkProductSofaNameDuplicationParam);
    }

    public Integer createProduct(PostProductSofaReq postProductSofaReq) {
        String createProductQuery = "insert into product_sofa " +
                "(categorySofa, productSofaName, capacity, material, hasLegs, hasHeadrest, fullWidth, fullDepth, fullHeight, shape, " +
                " cushionFeeling, isAntipollution, isWaterproof, " +
                " hasElbowrest, hasStool, isCoverRemovable, " +
                " recliningSeat, isAutomatic,  brandID) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Object[] createProductParams = new Object[]{
                postProductSofaReq.getCategorySofa(),
                postProductSofaReq.getProductSofaName(),
                postProductSofaReq.getCapacity(),
                postProductSofaReq.getMaterial(),
                postProductSofaReq.getHasLegs(),
                postProductSofaReq.getHasHeadrest(),
                postProductSofaReq.getFullWidth(),
                postProductSofaReq.getFullDepth(),
                postProductSofaReq.getFullHeight(),
                postProductSofaReq.getShape(),
                postProductSofaReq.getCushionFeeling(),
                postProductSofaReq.getIsAntipollution(),
                postProductSofaReq.getIsWaterproof(),
                postProductSofaReq.getHasElbowrest(),
                postProductSofaReq.getHasStool(),
                postProductSofaReq.getIsCoverRemovable(),
                postProductSofaReq.getRecliningSeat(),
                postProductSofaReq.getIsAutomatic(),
                postProductSofaReq.getBrandID()
        };

        this.jdbcTemplate.update(createProductQuery, createProductParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery, Integer.class);
    }
}
