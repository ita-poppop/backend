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
        summary     = "리뷰 댓글 목록 조회",
        description = "트리 구조로 모든 댓글·대댓글을 반환합니다."
)
@ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200", description = "댓글 목록 조회 성공",
                content      = @Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema    = @Schema(implementation = ApiResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "404", description = "대상 리뷰를 찾을 수 없음",
                content      = @Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema    = @Schema(implementation = ApiResponse.class))
        )
})
public @interface GetComments {}
