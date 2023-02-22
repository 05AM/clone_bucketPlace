package com.example.demo.src.scrap;


import com.example.demo.src.scrap.model.*;
import com.example.demo.src.scrap.model.Photo;
import com.example.demo.src.scrap.model.PhotoForTotalScrap;
import com.example.demo.src.scrap.model.ProductForTotalScrap;
import com.example.demo.src.user.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ScrapDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public GetScrapRes getUserScraps(int userID){
        String getPhotoQuery = "select photoLink, us.createAt from photo inner join user_scrap us on photo.photoID = us.photoID where us.userID = ? AND us.state = 'ACTIVE' order by photo.createAt desc;";
        List<PhotoForTotalScrap> photos = jdbcTemplate.query(getPhotoQuery,
                (rs, rowNum) -> new PhotoForTotalScrap(
                        "사진",
                        rs.getString("photoLink"),
                        rs.getTimestamp("createAt")),
                userID);

        String getProductQuery = "select mainImageLink, us.createAt from product_page inner join user_scrap us on product_page.productPageID = us.productPageID where us.userID = ? AND us.state = 'ACTIVE' order by product_page.createAt desc;";
        List<ProductForTotalScrap> products = jdbcTemplate.query(getProductQuery,
                (rs, rowNum) -> new ProductForTotalScrap(
                        "상품",
                        rs.getString("mainImageLink"),
                        rs.getTimestamp("createAt")),
                userID);

        List<Object> objectList = new ArrayList<>();
        int j = 0,k = 0;
        for(int i=0; i<photos.size() + products.size(); i++){
            if(j >= photos.size() || k >= products.size()) break;
            if(photos.get(j).getCreateAt().after(products.get(k).getCreateAt())){
                objectList.add(photos.get(j));
                j++;
            }
            else if(products.get(k).getCreateAt().after(photos.get(j).getCreateAt())){
                objectList.add(products.get(k));
                k++;
            }
        }
        if(j < photos.size()){
            while(j < photos.size()){
                objectList.add(photos.get(j));
                j++;
            }
        }
        if(k < products.size()){
            while(k < products.size()){
                objectList.add(products.get(k));
                k++;
            }
        }

        String totalPhotoQuery = "select count(distinct us.photoID) as totalPhoto from user inner join user_scrap us on user.userID = us.userID where us.userID = ? AND us.state = 'ACTIVE';";
        int totalPhoto = jdbcTemplate.queryForObject(totalPhotoQuery, int.class, userID);

        String totalProductQuery = "select count(distinct us.productPageID) as totalProduct from user inner join user_scrap us on user.userID = us.userID where us.userID = ? AND us.state = 'ACTIVE';";
        int totalProduct = jdbcTemplate.queryForObject(totalProductQuery, int.class, userID);

        String getScrapQuery = "select userNickname, count(distinct us.productPageID) as totalProduct, count(distinct us.photoID) as totalPhoto from user left outer join user_scrap us on user.userID = us.userID  AND us.state = 'ACTIVE' where user.userID = ?;";
        int getScrapParams = userID;

        return this.jdbcTemplate.queryForObject(getScrapQuery,
                (rs, rowNum) -> new GetScrapRes(
                        rs.getString("userNickname"),
                        totalPhoto + totalProduct,
                        rs.getInt("totalProduct"),
                        rs.getInt("totalPhoto"),
                        objectList.toArray(new Object[objectList.size()])),
                userID);
    }

    public GetScrapRes getUserScrapsProducts(int userID){
        String getProductPageIDQuery = "select productPageID from user_scrap where user_scrap.userID = ? AND state = 'ACTIVE' AND productPageID is not null order by createAt desc;";
        List<ProductPageID> productPageIDs = this.jdbcTemplate.query(getProductPageIDQuery,
                (rs,rowNum) -> new ProductPageID(
                        rs.getInt("productPageID")),
                userID);

        List<Product> products = new ArrayList<>();

        for(int i=0; i<productPageIDs.size(); i++){
            double avgRate;
            String getAvgRate = "select avg(rate) as avgRate from review inner join product_page pp on review.productPageID = pp.productPageID where pp.productPageID = ?;";
            if(this.jdbcTemplate.queryForObject(getAvgRate, Double.class, productPageIDs.get(i).getProductPageID()) == null){
                avgRate = 0;
            }
            else {
                avgRate = this.jdbcTemplate.queryForObject(getAvgRate, Double.class, productPageIDs.get(i).getProductPageID());
            }
            String getCntReview = "select count(reviewID) as cntReview from review inner join product_page pp on review.productPageID = pp.productPageID where pp.productPageID = ?;";
            int cntReview = this.jdbcTemplate.queryForObject(getCntReview, int.class, productPageIDs.get(i).getProductPageID());
            String getProductQuery = "select mainImageLink, brandName, pageTitle, dcRate, price, shipMethod, isTodaysDeal from product_page inner join user_scrap us on product_page.productPageID = us.productPageID inner join brand b on product_page.brandID = b.brandID where us.userID = ? AND us.productPageID = ? AND us.state = 'ACTIVE' order by product_page.createAt desc;";
            Product product = jdbcTemplate.queryForObject(getProductQuery,
                    (rs, rowNum) -> new Product(
                            rs.getString("mainImageLink"),
                            rs.getString("brandName"),
                            rs.getString("pageTitle"),
                            rs.getInt("dcRate"),
                            rs.getInt("price"),
                            avgRate,
                            cntReview,
                            rs.getString("shipMethod"),
                            rs.getInt("isTodaysDeal")),
                    userID, productPageIDs.get(i).getProductPageID());
            products.add(product);
        }

        String totalPhotoQuery = "select count(distinct us.photoID) as totalPhoto from user inner join user_scrap us on user.userID = us.userID where us.userID = ? AND us.state = 'ACTIVE';";
        int totalPhoto = jdbcTemplate.queryForObject(totalPhotoQuery, int.class, userID);

        String totalProductQuery = "select count(distinct us.productPageID) as totalProduct from user inner join user_scrap us on user.userID = us.userID where us.userID = ? AND us.state = 'ACTIVE';";
        int totalProduct = jdbcTemplate.queryForObject(totalProductQuery, int.class, userID);

        String getScrapQuery = "select userNickname, count(distinct us.productPageID) as totalProduct, count(distinct us.photoID) as totalPhoto from user left outer join user_scrap us on user.userID = us.userID AND us.state = 'ACTIVE' where user.userID = ?;";
        int getScrapParams = userID;

        return this.jdbcTemplate.queryForObject(getScrapQuery,
                (rs, rowNum) -> new GetScrapRes(
                        rs.getString("userNickname"),
                        totalPhoto + totalProduct,
                        rs.getInt("totalProduct"),
                        rs.getInt("totalPhoto"),
                        products.toArray(new Product[products.size()])),
                userID);
    }

    public GetScrapRes getUserScrapsPhotos(int userID){
        String getPhotoQuery = "select photoLink from photo inner join user_scrap us on photo.photoID = us.photoID where us.userID = ? AND us.state = 'ACTIVE' order by photo.createAt desc;";
        List<Photo> photos = jdbcTemplate.query(getPhotoQuery, (rs, rowNum) -> new Photo(rs.getString("photoLink")), userID);

        String totalPhotoQuery = "select count(distinct us.photoID) as totalPhoto from user inner join user_scrap us on user.userID = us.userID where us.userID = ? AND us.state = 'ACTIVE';";
        int totalPhoto = jdbcTemplate.queryForObject(totalPhotoQuery, int.class, userID);

        String totalProductQuery = "select count(distinct us.productPageID) as totalProduct from user inner join user_scrap us on user.userID = us.userID where us.userID = ? AND us.state = 'ACTIVE';";
        int totalProduct = jdbcTemplate.queryForObject(totalProductQuery, int.class, userID);

        String getScrapQuery = "select userNickname, count(distinct us.productPageID) as totalProduct, count(distinct us.photoID) as totalPhoto from user left outer join user_scrap us on user.userID = us.userID AND us.state = 'ACTIVE' where user.userID = ?;";
        int getScrapParams = userID;

        return this.jdbcTemplate.queryForObject(getScrapQuery,
                (rs, rowNum) -> new GetScrapRes(
                        rs.getString("userNickname"),
                        totalPhoto + totalProduct,
                        rs.getInt("totalProduct"),
                        rs.getInt("totalPhoto"),
                        photos.toArray(new Photo[photos.size()])),
                userID);
    }

    public GetScrapRes getUserScrapsFolders(int userID){
        String getFolderNameQuery = "select scrapFolderName from user_scrap_folder where userID = ? AND user_scrap_folder.state = 'ACTIVE' order by createAt desc;";
        List<Folder> folders = jdbcTemplate.query(getFolderNameQuery, (rs, rowNum) -> new Folder(rs.getString("scrapFolderName")), userID);

        for(int i=0; i<folders.size(); i++){
            //폴더 내에서 상품 갯수
            System.out.println("folders.get(i).getScrapFolderName() = " + folders.get(i).getScrapFolderName());
            String getTotalProductQuery = "select count(mainImageLink) as totalScrap from product_page inner join user_scrap us on product_page.productPageID = us.productPageID AND us.userID = ? left outer join user_scrap_folder usf on us.scrapFolderID = usf.scrapFolderID AND usf.state = 'ACTIVE' AND  scrapFolderName = ?;";
            int totalProductScraps = jdbcTemplate.queryForObject(getTotalProductQuery, int.class, userID, folders.get(i).getScrapFolderName() );
            //폴더 내에서 사진 갯수
            String getTotalPhotoQuery = "select count(photoLink) as totalScrap from photo inner join user_scrap us on photo.photoID = us.photoID AND us.userID = ? left outer join user_scrap_folder usf on us.scrapFolderID = usf.scrapFolderID  AND usf.state = 'ACTIVE' AND scrapFolderName = ?;";
            int totalPhotoScraps = jdbcTemplate.queryForObject(getTotalPhotoQuery, int.class, userID, folders.get(i).getScrapFolderName());

            folders.get(i).setTotalScraps(totalPhotoScraps + totalProductScraps);
            //스크랩 된 사진과 생성일자 가져오기
            String getPhotoQuery = "select photoLink, us.createAt from photo inner join user_scrap us on photo.photoID = us.photoID AND us.userID = ? inner join user_scrap_folder usf on us.scrapFolderID = usf.scrapFolderID AND scrapFolderName = ? AND usf.state = 'ACTIVE' order by us.createAt desc;";
            List<PhotoForTotalScrap> photos = jdbcTemplate.query(getPhotoQuery, (rs, rowNum) -> new PhotoForTotalScrap(
                    "사진",
                    rs.getString("photoLink"),
                    rs.getTimestamp("createAt")),
                    userID, folders.get(i).getScrapFolderName());
            //스크랩 된 상품과 생성일자 가져오기
            String getProductQuery = "select mainImageLink, us.createAt from product_page inner join user_scrap us on product_page.productPageID = us.productPageID AND us.userID = ? inner join user_scrap_folder usf on us.scrapFolderID = usf.scrapFolderID AND scrapFolderName = ? AND usf.state = 'ACTIVE' order by us.createAt desc;";
            List<ProductForTotalScrap> products = jdbcTemplate.query(getProductQuery, (rs, rowNum) -> new ProductForTotalScrap(
                    "상품",
                    rs.getString("mainImageLink"),
                    rs.getTimestamp("createAt")),
                    userID, folders.get(i).getScrapFolderName());

            if(photos.size() == 0 && products.size() == 0)
                folders.get(i).setRecentImage("");
            else if(photos.size() == 0)
                folders.get(i).setRecentImage(products.get(0).getMainImageLink());
            else if(products.size() == 0)
                folders.get(i).setRecentImage(photos.get(0).getPhotoLink());
            else{
                if(photos.get(0).getCreateAt().after(products.get(0).getCreateAt()))
                    folders.get(i).setRecentImage(photos.get(0).getPhotoLink());
                else
                    folders.get(i).setRecentImage(products.get(0).getMainImageLink());
            }
        }

        //전체 스크랩 사진 갯수
        String totalPhotoQuery = "select count(distinct us.photoID) as totalPhoto from user inner join user_scrap us on user.userID = us.userID where us.userID = ?;";
        int totalPhoto = jdbcTemplate.queryForObject(totalPhotoQuery, int.class, userID);

        //전체 스크랩 상품 갯수
        String totalProductQuery = "select count(distinct us.productPageID) as totalProduct from user inner join user_scrap us on user.userID = us.userID where us.userID = ?;";
        int totalProduct = jdbcTemplate.queryForObject(totalProductQuery, int.class, userID);

        String getScrapQuery = "select userNickname, count(distinct us.productPageID) as totalProduct, count(distinct us.photoID) as totalPhoto from user left outer join user_scrap us on user.userID = us.userID AND us.state = 'ACTIVE' where user.userID = ?;";
        int getScrapParams = userID;

        return this.jdbcTemplate.queryForObject(getScrapQuery,
                (rs, rowNum) -> new GetScrapRes(
                        rs.getString("userNickname"),
                        totalPhoto + totalProduct,
                        rs.getInt("totalProduct"),
                        rs.getInt("totalPhoto"),
                        folders.toArray(new Folder[folders.size()])),
                userID);
    }

    public GetScrapFolderRes getUserScrapsFolderAll(int userID, int folderID){
        String getPhotoQuery = "select photoLink, us.createAt from photo inner join user_scrap us on photo.photoID = us.photoID where us.userID = ? AND scrapFolderID = ? AND us.state = 'ACTIVE' order by photo.createAt desc;";
        List<PhotoForTotalScrap> photos = jdbcTemplate.query(getPhotoQuery,
                (rs, rowNum) -> new PhotoForTotalScrap(
                        "사진",
                        rs.getString("photoLink"),
                        rs.getTimestamp("createAt")),
                userID, folderID);

        String getProductQuery = "select mainImageLink, us.createAt from product_page inner join user_scrap us on product_page.productPageID = us.productPageID where us.userID = ? AND scrapFolderID = ? AND us.state = 'ACTIVE' order by product_page.createAt desc;";
        List<ProductForTotalScrap> products = jdbcTemplate.query(getProductQuery,
                (rs, rowNum) -> new ProductForTotalScrap(
                        "상품",
                        rs.getString("mainImageLink"),
                        rs.getTimestamp("createAt")),
                userID, folderID);

        List<Object> objectList = new ArrayList<>();
        int j = 0,k = 0;
        for(int i=0; i<photos.size() + products.size(); i++){
            if(j >= photos.size() || k >= products.size()) break;
            if(photos.get(j).getCreateAt().after(products.get(k).getCreateAt())){
                objectList.add(photos.get(j));
                j++;
            }
            else if(products.get(k).getCreateAt().after(photos.get(j).getCreateAt())){
                objectList.add(products.get(k));
                k++;
            }
        }
        if(j < photos.size()){
            while(j < photos.size()){
                objectList.add(photos.get(j));
                j++;
            }
        }
        if(k < products.size()){
            while(k < products.size()){
                objectList.add(products.get(k));
                k++;
            }
        }

        String totalPhotoQuery = "select count(distinct us.photoID) as totalPhoto from user inner join user_scrap us on user.userID = us.userID where us.userID = ? AND scrapFolderID = ? AND us.state = 'ACTIVE';";
        int totalPhoto = jdbcTemplate.queryForObject(totalPhotoQuery, int.class, userID, folderID);

        String totalProductQuery = "select count(distinct us.productPageID) as totalProduct from user inner join user_scrap us on user.userID = us.userID where us.userID = ? AND scrapFolderID = ? AND us.state = 'ACTIVE';";
        int totalProduct = jdbcTemplate.queryForObject(totalProductQuery, int.class, userID, folderID);

        String getFolderAllQuery = "select scrapFolderName, content from user_scrap_folder where userID = ? AND scrapFolderID = ? AND user_scrap_folder.state = 'ACTIVE';";

        return this.jdbcTemplate.queryForObject(getFolderAllQuery,
                (rs, rowNum) -> new GetScrapFolderRes(
                        rs.getString("scrapFolderName"),
                        rs.getString("content"),
                        "모두",
                        totalPhoto + totalProduct,
                        totalProduct,
                        totalPhoto,
                        objectList.toArray(new Object[objectList.size()])),
                userID, folderID);
    }

    public GetScrapFolderRes getUserScrapsFolderProduct(int userID, int folderID){
        String getProductPageIDQuery = "select productPageID from user_scrap left outer join user_scrap_folder usf on user_scrap.scrapFolderID = usf.scrapFolderID where user_scrap.userID = ? AND usf.scrapFolderID = ? AND user_scrap.state = 'ACTIVE' AND productPageID is not null order by user_scrap.createAt desc;";
        List<ProductPageID> productPageIDs = this.jdbcTemplate.query(getProductPageIDQuery,
                (rs,rowNum) -> new ProductPageID(
                        rs.getInt("productPageID")),
                userID, folderID);

        List<Product> products = new ArrayList<>();

        for(int i=0; i<productPageIDs.size(); i++){
            double avgRate;
            String getAvgRate = "select avg(rate) as avgRate from review inner join product_page pp on review.productPageID = pp.productPageID where pp.productPageID = ?;";
            if(this.jdbcTemplate.queryForObject(getAvgRate, Double.class, productPageIDs.get(i).getProductPageID()) == null){
                avgRate = 0;
            }
            else {
                avgRate = this.jdbcTemplate.queryForObject(getAvgRate, Double.class, productPageIDs.get(i).getProductPageID());
            }
            String getCntReview = "select count(reviewID) as cntReview from review inner join product_page pp on review.productPageID = pp.productPageID where pp.productPageID = ?;";
            int cntReview = this.jdbcTemplate.queryForObject(getCntReview, int.class, productPageIDs.get(i).getProductPageID());
            String getProductQuery = "select mainImageLink, brandName, pageTitle, dcRate, price, shipMethod, isTodaysDeal from product_page inner join user_scrap us on product_page.productPageID = us.productPageID inner join brand b on product_page.brandID = b.brandID where us.userID = ? AND us.productPageID = ? AND us.state = 'ACTIVE' order by product_page.createAt desc;";
            Product product = jdbcTemplate.queryForObject(getProductQuery,
                    (rs, rowNum) -> new Product(
                            rs.getString("mainImageLink"),
                            rs.getString("brandName"),
                            rs.getString("pageTitle"),
                            rs.getInt("dcRate"),
                            rs.getInt("price"),
                            avgRate,
                            cntReview,
                            rs.getString("shipMethod"),
                            rs.getInt("isTodaysDeal")),
                    userID, productPageIDs.get(i).getProductPageID());
            products.add(product);
        }

        String totalPhotoQuery = "select count(distinct us.photoID) as totalPhoto from user inner join user_scrap us on user.userID = us.userID where us.userID = ? AND scrapFolderID = ? AND us.state = 'ACTIVE';";
        int totalPhoto = jdbcTemplate.queryForObject(totalPhotoQuery, int.class, userID, folderID);

        String totalProductQuery = "select count(distinct us.productPageID) as totalProduct from user inner join user_scrap us on user.userID = us.userID where us.userID = ? AND scrapFolderID = ? AND us.state = 'ACTIVE';";
        int totalProduct = jdbcTemplate.queryForObject(totalProductQuery, int.class, userID, folderID);

        String getFolderAllQuery = "select scrapFolderName, content from user_scrap_folder where userID = ? AND scrapFolderID = ? AND user_scrap_folder.state = 'ACTIVE';";

        return this.jdbcTemplate.queryForObject(getFolderAllQuery,
                (rs, rowNum) -> new GetScrapFolderRes(
                        rs.getString("scrapFolderName"),
                        rs.getString("content"),
                        "상품",
                        totalPhoto + totalProduct,
                        totalProduct,
                        totalPhoto,
                        products.toArray(new Product[products.size()])),
                userID, folderID);
    }

    public GetScrapFolderRes getUserScrapsFolderPhoto(int userID, int folderID){
        String getPhotoQuery = "select photoLink from photo inner join user_scrap us on photo.photoID = us.photoID inner join user_scrap_folder usf on us.scrapFolderID = usf.scrapFolderID where us.userID = ? AND usf.scrapFolderID = ? AND us.state = 'ACTIVE' order by photo.createAt desc;";
        List<Photo> photos = jdbcTemplate.query(getPhotoQuery, (rs, rowNum) -> new Photo(rs.getString("photoLink")), userID, folderID);

        String totalPhotoQuery = "select count(distinct us.photoID) as totalPhoto from user inner join user_scrap us on user.userID = us.userID where us.userID = ? AND scrapFolderID = ? AND us.state = 'ACTIVE';";
        int totalPhoto = jdbcTemplate.queryForObject(totalPhotoQuery, int.class, userID, folderID);

        String totalProductQuery = "select count(distinct us.productPageID) as totalProduct from user inner join user_scrap us on user.userID = us.userID where us.userID = ? AND scrapFolderID = ? AND us.state = 'ACTIVE';";
        int totalProduct = jdbcTemplate.queryForObject(totalProductQuery, int.class, userID, folderID);

        String getFolderAllQuery = "select scrapFolderName, content from user_scrap_folder where userID = ? AND scrapFolderID = ? AND user_scrap_folder.state = 'ACTIVE';";

        return this.jdbcTemplate.queryForObject(getFolderAllQuery,
                (rs, rowNum) -> new GetScrapFolderRes(
                        rs.getString("scrapFolderName"),
                        rs.getString("content"),
                        "사진",
                        totalPhoto + totalProduct,
                        totalProduct,
                        totalPhoto,
                        photos.toArray(new Photo[photos.size()])),
                userID, folderID);
    }

    public void createFolder(int userID, PostScrapFolderReq postScrapFolderReq){
        String createFolderQuery = "insert into user_scrap_folder (scrapFolderName, content, userID) VALUES (?,?,?)";
        Object[] createFolderParams = new Object[]{postScrapFolderReq.getScrapFolderName(), postScrapFolderReq.getContent(), userID};
        this.jdbcTemplate.update(createFolderQuery, createFolderParams);
    }

    public int deleteFolder(int userID, int folderID){
        String deleteFolderQuery = "update user_scrap_folder set state = 'INACTIVE' where userID = ? AND scrapFolderID = ?";
        return this.jdbcTemplate.update(deleteFolderQuery, userID, folderID);
    }

    public int modifyFolder(int userID, int folderID, PatchScrapFolderReq patchScrapFolderReq){
        String modifyFolderQuery = "update user_scrap_folder set scrapFolderName = ?, content = ? where userID = ? AND scrapFolderID = ?";
        Object[] modifyFolderParams = new Object[]{patchScrapFolderReq.getScrapFolderName(), patchScrapFolderReq.getContent(), userID, folderID};
        return this.jdbcTemplate.update(modifyFolderQuery, modifyFolderParams);
    }

    public GetFolderRes getFolder(int userID, int folderID){
        String getFolderQuery = "select scrapFolderName, content from user_scrap_folder where userID = ? AND scrapFolderID = ?";
        return this.jdbcTemplate.queryForObject(getFolderQuery,
                (rs, rowNum) -> new GetFolderRes(
                        rs.getString("scrapFolderName"),
                        rs.getString("content")),
                userID, folderID);
    }

    public int getFolderID(int scrapID, int userID){
        String getFolderID = "select scrapFolderID from user_scrap where scrapID = ? AND userID = ?;";
        if(this.jdbcTemplate.queryForObject(getFolderID, int.class, scrapID, userID) == null){
            return 0;
        }
        return this.jdbcTemplate.queryForObject(getFolderID, int.class, scrapID, userID);
    }

    public void addScrapToFolder(int scrapID, int userID, int folderID){
        String addScrapToFolderQuery = "update user_scrap set scrapFolderID = ? where userID = ? and scrapID = ?;";
        Object[] addScrapToFolderParams = new Object[]{folderID, userID, scrapID};
        this.jdbcTemplate.update(addScrapToFolderQuery, addScrapToFolderParams);
    }

    public void deleteScrapToFolder(int scrapID, int userID){
        String deleteScrapToFolderQuery = "update user_scrap set scrapFolderID = null where userID = ? and scrapID = ?;";
        Object[] deleteScrapToFolderParams = new Object[]{userID, scrapID};
        this.jdbcTemplate.update(deleteScrapToFolderQuery, deleteScrapToFolderParams);
    }

    public String getScrapStateProduct(int userID, int productPageID){
        String checkExistsCrapQuery = "select exists(select state from user_scrap where userID = ? AND productPageID = ?)";
        Object[] getScrapStateParams = new Object[]{userID, productPageID};

        String getScrapStateQuery = "select state from user_scrap where userID = ? AND productPageID = ?;";

        if(this.jdbcTemplate.queryForObject(checkExistsCrapQuery, int.class, getScrapStateParams) == 0)
            return null;
        return this.jdbcTemplate.queryForObject(getScrapStateQuery, String.class, getScrapStateParams);
    }

    public String getScrapStatePhoto(int userID, int photoID){
        String checkExistsScrapQuery = "select exists(select state from user_scrap where userID = ? AND photoID = ?)";
        Object[] getScrapStateParams = new Object[]{userID, photoID};

        String getScrapStateQuery = "select state from user_scrap where userID = ? AND photoID = ?;";

        if(this.jdbcTemplate.queryForObject(checkExistsScrapQuery, int.class, getScrapStateParams) == 0)
            return null;
        return this.jdbcTemplate.queryForObject(getScrapStateQuery, String.class, getScrapStateParams);
    }

    public void createScrapProduct(int userID, int productPageID){
        String createScrapQuery = "insert into user_scrap (userID, productPageID) VALUES (?,?)";
        Object[] createScrapParams = new Object[]{userID, productPageID};
        this.jdbcTemplate.update(createScrapQuery, createScrapParams);
    }

    public void createScrapPhoto(int userID, int photoID){
        String createScrapQuery = "insert into user_scrap (userID, photoID) VALUES (?,?)";
        Object[] createScrapParams = new Object[]{userID, photoID};
        this.jdbcTemplate.update(createScrapQuery, createScrapParams);
    }

    public void addScrapProduct(int userID, int productPageID){
        String addScrapQuery = "update user_scrap set state = 'ACTIVE' where userID = ? and productPageID = ?;";
        Object[] addScrapParams = new Object[]{userID, productPageID};
        this.jdbcTemplate.update(addScrapQuery, addScrapParams);
    }

    public void addScrapPhoto(int userID, int photoID){
        String addScrapQuery = "update user_scrap set state = 'ACTIVE' where userID = ? and photoID = ?;";
        Object[] addScrapParams = new Object[]{userID, photoID};
        this.jdbcTemplate.update(addScrapQuery, addScrapParams);
    }

    public void deleteScrapProduct(int userID, int productPageID){
        String deleteScrapQuery = "update user_scrap set state = 'INACTIVE' where userID = ? and productPageID = ?;";
        Object[] deleteScrapParams = new Object[]{userID, productPageID};
        this.jdbcTemplate.update(deleteScrapQuery, deleteScrapParams);
    }

    public void deleteScrapPhoto(int userID, int photoID){
        String deleteScrapQuery = "update user_scrap set state = 'INACTIVE' where userID = ? and photoID = ?;";
        Object[] deleteScrapParams = new Object[]{userID, photoID};
        this.jdbcTemplate.update(deleteScrapQuery, deleteScrapParams);
    }
}
