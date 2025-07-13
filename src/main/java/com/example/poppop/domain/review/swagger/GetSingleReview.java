package com.example.poppop.domain.review.swagger;

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
        summary = "리뷰 단건 조회",
        description = "팝업 ID와 리뷰 ID를 받아 해당 리뷰의 상세 정보(좋아요 개수, 댓글 개수 포함)를 조회합니다."
)
@ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "리뷰 상세 조회 성공",
                content = @Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = ApiResponse.class)
                )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "404",
                description = "팝업 또는 리뷰를 찾을 수 없음",
                content = @Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = ApiResponse.class)
                )
        )
})
public @interface GetSingleReview {
}