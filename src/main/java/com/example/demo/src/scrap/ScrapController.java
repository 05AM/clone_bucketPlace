package com.example.demo.src.scrap;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.scrap.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.example.demo.config.BaseResponseStatus.*;

@RestController
@RequestMapping("/app/scraps")
public class ScrapController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final ScrapProvider scrapProvider;
    @Autowired
    private final ScrapService scrapService;
    @Autowired
    private final JwtService jwtService;

    public ScrapController(ScrapProvider scrapProvider, ScrapService scrapService, JwtService jwtService){
        this.scrapProvider = scrapProvider;
        this.scrapService = scrapService;
        this.jwtService = jwtService;
    }

    /**
     * 전체 스크랩 조회 API
     * [GET] /app/scraps/users/:userId
     */
    @ResponseBody
    @GetMapping("/users/{userID}")
    public BaseResponse<GetScrapRes> getUserScraps(@PathVariable("userID") int userID) {
        try{
            GetScrapRes getScrapRes = scrapProvider.getUserScraps(userID);
            return new BaseResponse<>(getScrapRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 상품 스크랩 조회 API
     * [GET] /app/scraps/users/:userId/products
     */
    @ResponseBody
    @GetMapping("/users/{userID}/products")
    public BaseResponse<GetScrapRes> getUserScrapsProducts(@PathVariable("userID") int userID) {
        try{
            GetScrapRes getScrapRes = scrapProvider.getUserScrapsProducts(userID);
            return new BaseResponse<>(getScrapRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 사진 스크랩 조회 API
     * [GET] /app/scraps/users/:userId/photos
     */
    @ResponseBody
    @GetMapping("/users/{userID}/photos")
    public BaseResponse<GetScrapRes> getUserScrapsPhotos(@PathVariable("userID") int userID) {
        try{
            GetScrapRes getScrapRes = scrapProvider.getUserScrapsPhotos(userID);
            return new BaseResponse<>(getScrapRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 전체 스크랩 폴더 조회 API
     * [GET] /app/scraps/folders/users/:userId
     */
    @ResponseBody
    @GetMapping("/folders/users/{userID}")
    public BaseResponse<GetScrapRes> getUserScrapsFolders(@PathVariable("userID") int userID) {
        try{
            GetScrapRes getScrapRes = scrapProvider.getUserScrapsFolders(userID);
            return new BaseResponse<>(getScrapRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 특정 스크랩 폴더 조회 API
     * [GET] /app/scraps/folders/:forlderId/users/:userId
     */
    @ResponseBody
    @GetMapping("/folders/{folderId}/users/{userID}")
    public BaseResponse<GetScrapFolderRes> createFolder(@PathVariable("userID") int userID, @PathVariable("folderId") int folderID, @RequestParam("type") String categoryName) {
        try{
            GetScrapFolderRes getFolderScrapRes = scrapProvider.getUserScrapsFolder(userID, folderID, categoryName);
            return new BaseResponse<>(getFolderScrapRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 폴더 생성 API
     * [POST] /app/scraps/folders/users/:userId
     */
    @ResponseBody
    @PostMapping("/folders/users/{userID}")
    public BaseResponse<String> createFolder(@PathVariable("userID") int userID, @RequestBody PostScrapFolderReq postScrapFolderReq) {
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            if (userID != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            if (postScrapFolderReq.getScrapFolderName() == null) {
                return new BaseResponse<>(POST_SCRAP_EMPTY_FOLDERNAME);
            }
            scrapService.createFolder(userID, postScrapFolderReq);
            return new BaseResponse<>();
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

     /**
     * 폴더 삭제 API
     * [PATCH] app/scraps/folders/users/:userIdx/deletion
     */
    @ResponseBody
    @PatchMapping("/users/{userID}/folders/{folderID}/deletion")
    public BaseResponse<String> deleteFolder(@PathVariable("userID") int userID, @PathVariable("folderID") int folderID){
        try{
            int userIdxByJwt = jwtService.getUserIdx();
            if(userID != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            scrapService.deleteFolder(userID, folderID);
            return new BaseResponse<>();
        }catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 폴더 수정 API
     * [PATCH] app/scraps/folders/users/:userIdx
     */
    @ResponseBody
    @PatchMapping("/users/{userID}/folders/{folderID}")
    public BaseResponse<String> modifyFolder(@PathVariable("userID") int userID, @PathVariable("folderID") int folderID, @RequestBody PatchScrapFolderReq patchScrapFolderReq){
        try{
            int userIdxByJwt = jwtService.getUserIdx();
            if(userID != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            GetFolderRes getFolderRes = scrapProvider.getFolder(userID, folderID);
            Object[] objects = new Object[2];
            if(patchScrapFolderReq.getScrapFolderName() == null) objects[0] = getFolderRes.getScrapFolderName();
            else{
                if(patchScrapFolderReq.getScrapFolderName().length() == 0)
                    return new BaseResponse<>(POST_SCRAP_EMPTY_FOLDERNAME);
                objects[0] = patchScrapFolderReq.getScrapFolderName();
            }
            if(patchScrapFolderReq.getContent() == null) objects[1] = getFolderRes.getContent();
            else objects[1] = patchScrapFolderReq.getContent();
            PatchScrapFolderReq folder = new PatchScrapFolderReq((String)objects[0], (String)objects[1]);
            scrapService.modifyFolder(userID, folderID, folder);
            return new BaseResponse<>();
        }catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 폴더에 스크랩 추가 삭제 API
     * [PATCH] app/scraps/folders/users/:userIdx
     */
    @ResponseBody
    @PatchMapping("/{scrapID}/users/{userID}/folders/{folderID}")
    public BaseResponse<String> addScrapToFolder(@PathVariable("scrapID") int scrapID, @PathVariable("userID") int userID, @PathVariable("folderID") int folderID){
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            if (userID != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            int scrapFolderId = scrapProvider.getFolderID(scrapID ,userID);

            if(scrapFolderId == 0) {
                scrapService.addScrapToFolder(scrapID, userID, folderID);
                return new BaseResponse<>("추가 완료");
            }
            else {
                scrapService.deleteScrapToFolder(scrapID, userID);
                return new BaseResponse<>("삭제 완료");
            }
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 상품 스크랩 API
     * [POST] /app/scraps/users/:userId/product-pages/:productPageId
     */
    @ResponseBody
    @PostMapping("/users/{userID}/product-pages/{productPageID}")
    public BaseResponse<String> scrapProductPage(@PathVariable("userID") int userID, @PathVariable("productPageID") int productPageID) {
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            if (userID != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            String state = scrapProvider.getScrapStateProduct(userID, productPageID);

            if(state == null) {
                scrapService.createScrapProduct(userID, productPageID);
                return new BaseResponse<>("스크랩 추가");
            }
            else {
                if(state.equals("INACTIVE")){
                    scrapService.addScrapProduct(userID, productPageID);
                    return new BaseResponse<>("스크랩 추가");
                }
                else{
                    scrapService.deleteScrapProduct(userID, productPageID);
                    return new BaseResponse<>("스크랩 해제");
                }
            }
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 사진 스크랩 API
     * [POST] /app/scraps/users/:userId/photos/:photoId
     */
    @ResponseBody
    @PostMapping("/users/{userID}/photos/{photoID}")
    public BaseResponse<String> scrapPhoto(@PathVariable("userID") int userID, @PathVariable("photoID") int photoID) {
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            if (userID != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            String state = scrapProvider.getScrapStatePhoto(userID, photoID);

            if(state == null) {
                scrapService.createScrapPhoto(userID, photoID);
                return new BaseResponse<>("스크랩 추가");
            }
            else {
                if(state.equals("INACTIVE")){
                    scrapService.addScrapPhoto(userID, photoID);
                    return new BaseResponse<>("스크랩 추가");
                }
                else{
                    scrapService.deleteScrapPhoto(userID, photoID);
                    return new BaseResponse<>("스크랩 해제");
                }
            }
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
