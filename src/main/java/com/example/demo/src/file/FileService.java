package com.example.demo.src.file;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional()
public class FileService {
    private String S3Bucket = "bucketplace"; // Bucket 이름
    final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Autowired
    AmazonS3Client amazonS3Client;

    public List<String> uploadFiles(MultipartFile[] multipartFileList, String fileType) throws Exception {
        List<String> imagePathList = new ArrayList<>();

        String uploadFilePath = fileType;

        if(fileType.equals("profileImage") || fileType.equals("photo")) {
            uploadFilePath += ("/" + getFolderName());
        }

        for(MultipartFile multipartFile: multipartFileList) {
            String originalName = multipartFile.getOriginalFilename();  // 파일 이름
            String uploadFileName = getUuidFileName(originalName);      // 중복을 막기 위해 uuid로 문자열 생성

            long size = multipartFile.getSize(); // 파일 크기

            ObjectMetadata objectMetaData = new ObjectMetadata();
            objectMetaData.setContentType(multipartFile.getContentType());
            objectMetaData.setContentLength(size);

            String keyName = uploadFilePath + "/" + uploadFileName; // ex) 파일구분/년/월/일/파일.확장자

            // S3에 업로드
            amazonS3Client.putObject(
                    new PutObjectRequest(S3Bucket, keyName, multipartFile.getInputStream(), objectMetaData)
                            .withCannedAcl(CannedAccessControlList.PublicRead)
            );

            String imagePath = amazonS3Client.getUrl(S3Bucket, keyName).toString(); // 접근가능한 URL 가져오기
            imagePathList.add(imagePath);
        }

        return imagePathList;
    }

    /**
     * S3에 업로드된 파일 삭제
     */
    public String deleteFile(String fileKeyName) {

        String result = "delete process success";

        try {
            boolean isObjectExist = amazonS3Client.doesObjectExist(S3Bucket, fileKeyName);
            if (isObjectExist) {
                amazonS3Client.deleteObject(S3Bucket, fileKeyName);
            } else {
                result = "file not found";
            }
        } catch (Exception e) {
            logger.debug("Delete File failed", e);
        }

        return result;
    }


    /**
     * UUID 파일명 반환
     */
    public String getUuidFileName(String fileName) {
        String ext = fileName.substring(fileName.indexOf(".") + 1);
        return UUID.randomUUID().toString() + "." + ext;
    }

    /**
     * 년/월/일 폴더명 반환
     */
    private String getFolderName() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        String str = sdf.format(date);
        return str.replace("-", "/");
    }
}
