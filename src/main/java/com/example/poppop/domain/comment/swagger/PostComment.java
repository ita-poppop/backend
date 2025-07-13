package com.example.poppop.domain.comment.swagger;

import com.example.poppop.global.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Operation(
        summary     = "댓글 등록",
        description = "parentId 가 없으면 1차 댓글, 있으면 대댓글로 저장합니다."
)
@ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200", description = "댓글 등록 성공",
                content      = @Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema    = @Schema(implementation = ApiResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "400", description = "요청 값 검증 실패",
                content      = @Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema    = @Schema(implementation = ApiResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "404", description = "리뷰/부모 댓글/회원 없음",
                content      = @Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema    = @Schema(implementation = ApiResponse.class))
        )
})
public @interface PostComment {}
