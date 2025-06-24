package com.example.poppop.domain.comment.error;

import com.example.poppop.global.error.BaseError;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommentErrorCode implements BaseError {
    MEMBER_NOT_FOUND   (HttpStatus.NOT_FOUND,  "COMMENT_001", "회원 정보를 찾을 수 없습니다."),
    REVIEW_NOT_FOUND   (HttpStatus.NOT_FOUND,  "COMMENT_002", "리뷰를 찾을 수 없습니다."),
    COMMENT_NOT_FOUND  (HttpStatus.NOT_FOUND,  "COMMENT_003", "댓글을 찾을 수 없습니다."),
    INVALID_PARENT     (HttpStatus.BAD_REQUEST,"COMMENT_004", "부모 댓글이 대상 리뷰에 속하지 않습니다."),
    INVALID_PERMISSION (HttpStatus.FORBIDDEN,  "COMMENT_005", "이 요청을 수행할 권한이 없습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
