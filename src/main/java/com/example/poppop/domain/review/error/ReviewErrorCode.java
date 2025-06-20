package com.example.poppop.domain.review.error;

import com.example.poppop.global.error.BaseError;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ReviewErrorCode implements BaseError {

    MEMBER_NOT_FOUND   (HttpStatus.NOT_FOUND, "REVIEW_001", "회원 정보를 찾을 수 없습니다."),
    POPUP_NOT_FOUND    (HttpStatus.NOT_FOUND, "REVIEW_002", "팝업 정보를 찾을 수 없습니다."),
    REVIEW_NOT_FOUND   (HttpStatus.NOT_FOUND, "REVIEW_003", "리뷰를 찾을 수 없습니다."),
    IMAGE_REQUIRED     (HttpStatus.BAD_REQUEST, "REVIEW_004", "리뷰에는 최소 1장의 이미지가 필요합니다."),
    INVALID_PERMISSION (HttpStatus.FORBIDDEN,  "REVIEW_005", "권한이 없습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}

