package com.example.poppop.global.s3.error;

import com.example.poppop.global.error.BaseError;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum S3ErrorCode implements BaseError {

    TMP_ERROR(HttpStatus.BAD_REQUEST, "S3-0000", "파일 형식이 올바르지 않습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
