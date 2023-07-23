package com.clone.team4.domain.post.image;

import java.io.IOException;

import org.apache.tika.Tika;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class ImageValidator {

    private final Tika tika = new Tika();

    public void validateImageFile(MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
            throw new IllegalArgumentException("잘못된 요청입니다.");
        }
        if (!isImageFile(multipartFile)) {
            throw new IllegalArgumentException("이미지 파일이 아닙니다.");
        }
    }

    // 파일명의 확장자는 위변조 가능. MIME Type으로 비교
    private boolean isImageFile(MultipartFile multipartFile) {
        try {
            String mimeType = tika.detect(multipartFile.getInputStream());
            if (!mimeType.startsWith("image")) {
                return false;
            }
        } catch (IOException e) {
            log.error("이미지 저장 실패 {}", e);
            throw new RuntimeException("이미지 저장에 실패하였습니다.", e);
        }
        return true;
    }
}
