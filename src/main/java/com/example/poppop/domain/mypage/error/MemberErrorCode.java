package com.example.poppop.domain.mypage.error;

import com.example.poppop.global.error.BaseError;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MemberErrorCode implements BaseError {
    MEMBER_NOT_FOUND   (HttpStatus.NOT_FOUND, "MPE_001", "회원 정보를 찾을 수 없습니다."),
    POPUP_NOT_FOUND    (HttpStatus.NOT_FOUND, "MPE_002", "팝업 정보를 찾을 수 없습니다."),
    FAVORITE_NOT_FOUND (HttpStatus.NOT_FOUND, "MPE_003", "즐겨찾기 정보를 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String     code;
    private final String     message;
}
