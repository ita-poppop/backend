package com.example.poppop.global.auth.handler;

import com.example.poppop.global.common.ApiResponse;
import com.example.poppop.global.error.GlobalErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.oauth2.sdk.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    private final ObjectMapper objectMapper;

    /**
     * 권한 없는 사용자 접근 처리
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        ApiResponse<ErrorResponse> apiResponse = ApiResponse.fail(
                GlobalErrorCode.INVALID_PERMISSION,
                "권한 없는 사용자 접근 에러"
        );
        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
    }

}
