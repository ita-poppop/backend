package com.example.poppop.global.s3.service;

import com.example.poppop.global.error.exception.CustomException;
import com.example.poppop.global.s3.error.S3ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Utilities;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Client s3Client;
    private final S3Utilities s3Utilities;

    @Value("${aws.s3.bucket}")
    private String bucket;

    /**
     * S3에 파일 업로드 후, 공개 URL 반환
     */
    public String uploadFile(MultipartFile file, String dir) {
        try {
            String key = dir + "/" + UUID.randomUUID() + "-" + file.getOriginalFilename();

            // 1) 업로드 요청 빌드
            PutObjectRequest por = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .contentType(file.getContentType())
                    .contentLength(file.getSize())
//                    .acl(ObjectCannedACL.PUBLIC_READ)  // public 읽기 허용 (ACL 사용 시)
                    .build();

            // 2) 업로드
            s3Client.putObject(por,
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize())
            );

            // 3) 공개 URL 생성
            GetUrlRequest gur = GetUrlRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .build();
            return s3Utilities.getUrl(gur).toExternalForm();

        } catch (IOException e) {
            throw new CustomException(S3ErrorCode.TMP_ERROR);
        }
    }

    /**
     * S3에서 파일 삭제
     */
    public void deleteFile(String fileUrl) {
        // URL에서 버킷 이름 뒤의 key 추출
        String key = fileUrl.substring(
                fileUrl.indexOf(bucket) + bucket.length() + 1
        );
        DeleteObjectRequest dor = DeleteObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();
        s3Client.deleteObject(dor);
    }
}
