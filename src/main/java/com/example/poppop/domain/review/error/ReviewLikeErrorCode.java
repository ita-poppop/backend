package com.example.poppop.domain.review.error;

import com.example.poppop.global.error.BaseError;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ReviewLikeErrorCode implements BaseError {

    MEMBER_NOT_FOUND  (HttpStatus.NOT_FOUND, "RL_001", "회원 정보를 찾을 수 없습니다."),
    REVIEW_NOT_FOUND  (HttpStatus.NOT_FOUND, "RL_002", "리뷰를 찾을 수 없습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
