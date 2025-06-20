package com.example.poppop.domain.comment.swagger;

import com.example.poppop.global.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Operation(summary = "댓글 좋아요 토글",
        description = "댓글에 좋아요를 누르거나 취소합니다.")
@ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode="200",description="토글 성공",
                content=@Content(mediaType="application/json",
                        schema=@Schema(implementation= ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode="404",description="댓글/회원 정보 없음",
                content=@Content(schema=@Schema(implementation=ApiResponse.class)))
})
public @interface ToggleCommentLike {}
