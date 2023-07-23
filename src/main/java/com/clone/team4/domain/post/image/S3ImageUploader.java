package com.clone.team4.domain.post.image;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Component
public class S3ImageUploader {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;

    private final ImageValidator imageValidator;

    public List<String> storeFile(List<MultipartFile> multiPartFileList) {
        List<String> imageUrls = new ArrayList<>();

        for (MultipartFile multipartFile : multiPartFileList) {
            imageValidator.validateImageFile(multipartFile);

            multipartFile.getOriginalFilename();

            // 기존의 파일명
            String originalFileName = multipartFile.getOriginalFilename();

            // 저장될 파일명 (UUID)
            String storeFileName = createStoreFileName(originalFileName);

            ObjectMetadata objectMetadata = createObjectMetadata(multipartFile);
            try {
                InputStream inputStream = multipartFile.getInputStream();
                amazonS3.putObject(new PutObjectRequest(bucket, storeFileName, inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            imageUrls.add(amazonS3.getUrl(bucket, storeFileName).toString());
        }
        return imageUrls;
    }

    private ObjectMetadata createObjectMetadata(MultipartFile multipartFile) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(multipartFile.getContentType());
        objectMetadata.setContentLength(multipartFile.getSize());
        return objectMetadata;
    }

    private String createStoreFileName(String originalFileName) {
        String ext = extractExt(originalFileName);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }

    // 파일명의 확장자 파싱
    private String extractExt(String originalFileName) {
        int pos = originalFileName.lastIndexOf(".");
        String ext = originalFileName.substring(pos + 1);
        return ext;
    }
}
