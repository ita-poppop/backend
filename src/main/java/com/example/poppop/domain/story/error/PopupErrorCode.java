package com.example.poppop.domain.story.error;

import com.example.poppop.global.error.BaseError;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PopupErrorCode implements BaseError {

    MEMBER_NOT_FOUND   (HttpStatus.NOT_FOUND, "STORY_001", "회원 정보를 찾을 수 없습니다."),
    POPUP_NOT_FOUND    (HttpStatus.NOT_FOUND, "STORY_002", "팝업 정보를 찾을 수 없습니다."),
    STORY_NOT_FOUND   (HttpStatus.NOT_FOUND, "STORY_003", "스토리를 찾을 수 없습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}

