package com.example.demo.src.scrap;


import com.example.demo.config.BaseException;
import com.example.demo.src.scrap.model.PatchScrapFolderReq;
import com.example.demo.src.scrap.model.PostScrapFolderReq;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
@Transactional(rollbackFor = Exception.class)
public class ScrapService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ScrapDao scrapDao;
    private final JwtService jwtService;


    @Autowired
    public ScrapService(ScrapDao scrapDao, JwtService jwtService) {
        this.scrapDao = scrapDao;
        this.jwtService = jwtService;

    }

    public void createFolder(int userID, PostScrapFolderReq postScrapFolderReq){
        try{
            scrapDao.createFolder(userID, postScrapFolderReq);
        } catch (Exception exception) {
            logger.error("App - createFolder Service Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void deleteFolder(int userID, int folderID) throws BaseException {
        try{
            int result = scrapDao.deleteFolder(userID, folderID);
        } catch (Exception exception){
            logger.error("App - deleteFolder Service Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void modifyFolder(int userID, int folderID, PatchScrapFolderReq patchScrapFolderReq) throws BaseException {
        try{
            int result = scrapDao.modifyFolder(userID, folderID, patchScrapFolderReq);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_FOLDER);
            }
        } catch (Exception exception){
            logger.error("App - modifyFolder Service Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void addScrapToFolder(int scrapID, int userID, int folderID) throws BaseException {
        try{
            scrapDao.addScrapToFolder(scrapID, userID, folderID);
        } catch (Exception exception){
            logger.error("App - addScrapToFolder Service Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void deleteScrapToFolder(int scrapID, int userID) throws BaseException {
        try{
            scrapDao.deleteScrapToFolder(scrapID, userID);
        } catch (Exception exception){
            logger.error("App - deleteScrapToFolder Service Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void createScrapProduct(int userID, int productPageID) throws BaseException {
        try{
            scrapDao.createScrapProduct(userID, productPageID);
        } catch (Exception exception){
            logger.error("App - createScrap Service Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void createScrapPhoto(int userID, int photoID) throws BaseException {
        try{
            scrapDao.createScrapPhoto(userID, photoID);
        } catch (Exception exception){
            logger.error("App - createScrap Service Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void addScrapProduct(int userID, int productPageID) throws BaseException {
        try{
            scrapDao.addScrapProduct(userID, productPageID);
        } catch (Exception exception){
            logger.error("App - addScrap Service Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void addScrapPhoto(int userID, int photoID) throws BaseException {
        try{
            scrapDao.addScrapPhoto(userID, photoID);
        } catch (Exception exception){
            logger.error("App - addScrap Service Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void deleteScrapProduct(int userID, int productPageID) throws BaseException {
        try{
            scrapDao.deleteScrapProduct(userID, productPageID);
        } catch (Exception exception){
            logger.error("App - deleteScrap Service Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void deleteScrapPhoto(int userID, int photoID) throws BaseException {
        try{
            scrapDao.deleteScrapPhoto(userID, photoID);
        } catch (Exception exception){
            logger.error("App - deleteScrap Service Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
