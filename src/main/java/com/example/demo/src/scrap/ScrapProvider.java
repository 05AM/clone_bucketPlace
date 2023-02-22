package com.example.demo.src.scrap;


import com.example.demo.config.BaseException;
import com.example.demo.src.scrap.model.GetFolderRes;
import com.example.demo.src.scrap.model.GetScrapFolderRes;
import com.example.demo.src.scrap.model.GetScrapRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
@Transactional(readOnly = true)
public class ScrapProvider {

    private final ScrapDao scrapDao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public ScrapProvider(ScrapDao scrapDao, JwtService jwtService) {
        this.scrapDao = scrapDao;
        this.jwtService = jwtService;
    }

    public GetScrapRes getUserScraps(int userID) throws BaseException {
        try {
            GetScrapRes getScrapRes = scrapDao.getUserScraps(userID);
            return getScrapRes;
        } catch (Exception exception) {
            logger.error("App - getUserScraps Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public GetScrapRes getUserScrapsProducts(int userID) throws BaseException {
        try {
            GetScrapRes getScrapRes = scrapDao.getUserScrapsProducts(userID);
            return getScrapRes;
        } catch (Exception exception) {
            logger.error("App - getUserScrapsProducts Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public GetScrapRes getUserScrapsPhotos(int userID) throws BaseException {
        try {
            GetScrapRes getScrapRes = scrapDao.getUserScrapsPhotos(userID);
            return getScrapRes;
        } catch (Exception exception) {
            logger.error("App - getUserScrapsPhoto Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public GetScrapRes getUserScrapsFolders(int userID) throws BaseException {
        try {
            GetScrapRes getScrapRes = scrapDao.getUserScrapsFolders(userID);
            return getScrapRes;
        } catch (Exception exception) {
            logger.error("App - getUserScrapsPhoto Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public GetScrapFolderRes getUserScrapsFolder(int userID, int folderID, String category) throws BaseException {
        try {
            GetScrapFolderRes getFolderScrapRes;
            if(category.equals("모두"))
                getFolderScrapRes = scrapDao.getUserScrapsFolderAll(userID, folderID);
            else if(category.equals("상품"))
                getFolderScrapRes = scrapDao.getUserScrapsFolderProduct(userID, folderID);
            else
                getFolderScrapRes = scrapDao.getUserScrapsFolderPhoto(userID, folderID);

            return getFolderScrapRes;
        } catch (Exception exception) {
            logger.error("App - getUserScrapsPhoto Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public GetFolderRes getFolder(int userID, int folderID) throws BaseException {
        try{
            GetFolderRes getFolderRes = scrapDao.getFolder(userID, folderID);
            return getFolderRes;
        } catch (Exception exception){
            logger.error("App - getFolder Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int getFolderID(int scrapID, int userID) throws BaseException {
        try{
            return scrapDao.getFolderID(scrapID, userID);
        } catch (Exception exception){
            logger.error("App - getFolderID Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public String getScrapStateProduct(int userID, int productPageID) throws BaseException {
        try{
            return scrapDao.getScrapStateProduct(userID, productPageID);
        } catch (Exception exception){
            logger.error("App - getScrapStateProduct Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public String getScrapStatePhoto(int userID, int photoID) throws BaseException {
        try{
            return scrapDao.getScrapStatePhoto(userID, photoID);
        } catch (Exception exception){
            logger.error("App - getScrapStatePhoto Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
