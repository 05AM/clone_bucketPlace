package com.example.demo.src.file;

import java.util.List;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.BaseResponseStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.example.demo.config.BaseResponseStatus.REQUEST_ERROR;

@RestController
@RequestMapping("/app/files")
public class FileController {

    @Autowired
    private final FileService fileService;

    public FileController(FileService fileService){
        this.fileService = fileService;
    }

    /**
     * upload 메소드
     * @param multipartFileList
     * @return
     * @throws Exception
     */
    @PostMapping("")
    public ResponseEntity<Object> uploadFiles(MultipartFile[] multipartFileList, @RequestParam("fileType") String fileType) throws Exception {
        // fileType이 없으면 Error Bad Request
        // 프사 : profileImage
        // 사진 : photo
        // 상품 배너 : productPageBanner
        // 상품 정보 : productInfoImage
        if(fileType.equals("profileImage") || fileType.equals("photo") || fileType.equals("reviewImage") || fileType.equals("productPageBanner") || fileType.equals("productInfoImage")) {
            List<String> imagePathList = fileService.uploadFiles(multipartFileList, fileType);

            return new ResponseEntity<Object>(imagePathList, HttpStatus.OK);
        } else {
            return new ResponseEntity<Object>(new BaseException(REQUEST_ERROR).getStatus().getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("")
    public BaseResponse<Object> deleteFile(
            @RequestParam(value = "fileKeyName") String fileKeyName) {
        String result = fileService.deleteFile(fileKeyName);

        if(result.equals("delete process success")) {
            return new BaseResponse<>();
        } else {
            return new BaseResponse<>(new BaseException(BaseResponseStatus.DELETE_FILE_NOT_FOUND).getStatus());
        }
    }
}