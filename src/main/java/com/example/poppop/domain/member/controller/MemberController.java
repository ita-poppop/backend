package com.example.poppop.domain.member.controller;

import com.example.poppop.domain.member.dto.MemberRequest;
import com.example.poppop.domain.member.dto.MemberResponse;
import com.example.poppop.domain.member.entity.Member;
import com.example.poppop.domain.member.service.MemberService;
import com.example.poppop.global.auth.dto.PopPopUserDetails;
import com.example.poppop.global.auth.dto.TokenDto;
import com.example.poppop.global.auth.dto.UserInfo;
import com.example.poppop.global.auth.model.PopPopOAuth2User;
import com.example.poppop.global.auth.service.JwtService;
import com.example.poppop.global.auth.service.JwtTokenProvider;
import com.example.poppop.global.common.ApiResponse;
import com.example.poppop.global.error.GlobalErrorCode;
import com.example.poppop.global.error.exception.CustomException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/member")
@Slf4j
@Tag(name = "멤버 API", description = "회원가입 및 회원 조회")
public class MemberController {
    private final MemberService memberService;
    private final JwtService jwtService;
    private final JwtTokenProvider jwtTokenProvider;

    // 테스트용 회원가입 api
    @Operation(
            summary = "회원가입",
            description = "UserInfo(소셜/필수정보)를 받아 회원을 생성합니다. 이미 존재하면 기존 회원을 반환합니다."
    )
    @PostMapping("/signup")
    public ApiResponse<TokenDto> signup(@RequestBody @Valid UserInfo userInfo, HttpServletResponse response) {
        TokenDto tokenDto = memberService.signupAndAuthenticate(userInfo, response);
        return ApiResponse.success(tokenDto);
    }

    // 현재 로그인한 회원 정보 조회
    @Operation(
            summary = "내 정보 조회",
            description = "JWT 인증된 사용자의 회원 정보를 반환합니다."
    )
    @GetMapping
    public ApiResponse<MemberResponse> getMyInfo(@AuthenticationPrincipal PopPopOAuth2User user) {
        log.info(user.toString());
        MemberResponse response = memberService.getMemberResponse(user);
        return ApiResponse.success(response);
    }

    // 2. 로그아웃 (RefreshToken 헤더 기반)
    @PostMapping("/logout")
    @Operation(
            summary = "로그아웃",
            description = "access 토큰을 받아 로그아웃(토큰 무효화) 처리합니다."
    )
    public ApiResponse<Object> logout(@RequestHeader("Authorization") String token) {
        jwtService.logout(token);
        return ApiResponse.success("로그아웃이 완료되었습니다.");
    }

/*    // 3. 토큰 리프레시 (RefreshToken 헤더 기반, 새 토큰을 헤더로 반환)
    @PostMapping("/refresh")
    @Operation(
            summary = "토큰 리프레시",
            description = "RefreshToken 헤더를 받아 Access/Refresh 토큰을 재발급하고, 새 토큰을 헤더에 담아 반환합니다."
    )
    public ApiResponse<Object> refresh(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = request.getHeader("RefreshToken");
        TokenDto tokenDto = jwtService.tockenRefresh(refreshToken);

        // 새 토큰을 헤더에 담아 응답
        response.setHeader("Authorization", "Bearer " + tokenDto.getAccessTocken());
        response.setHeader("RefreshToken", tokenDto.getRefreshTocken());

        return ApiResponse.success("토큰 리프레시 성공");
    }*/
}
