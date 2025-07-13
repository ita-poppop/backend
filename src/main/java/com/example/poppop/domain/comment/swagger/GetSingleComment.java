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
        summary     = "댓글 단건 조회",
        description = "해당 리뷰의 특정 댓글을 조회하며, 모든 대댓글을 함께 반환합니다."
)
@ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description  = "댓글 조회 성공",
                content      = @Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema    = @Schema(implementation = ApiResponse.class)
                )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "404",
                description  = "리뷰 또는 댓글을 찾을 수 없음",
                content      = @Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema    = @Schema(implementation = ApiResponse.class)
                )
        )
})
public @interface GetSingleComment {
}
