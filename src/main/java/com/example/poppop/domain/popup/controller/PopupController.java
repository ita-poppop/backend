package com.example.poppop.domain.popup.controller;

import com.example.poppop.domain.popup.dto.*;
import com.example.poppop.domain.popup.service.PopupService;
import com.example.poppop.global.auth.model.PopPopOAuth2User;
import com.example.poppop.global.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/popups")
@Tag(name = "팝업 API", description = "팝업 관련 API")
public class PopupController {
    private final PopupService popupService;

    // 팝업 상세 조회 api
    @Operation(summary = "팝업 상세 조회 API")
    @GetMapping("/{popupId}")
    public ApiResponse<PopupDetailDto> getDetailPopup(
            @PathVariable Long popupId,
            @AuthenticationPrincipal PopPopOAuth2User user
    ) {
        PopupDetailDto detailPopup = popupService.getDetailPopup(popupId);
        popupService.incrementViewCount(popupId, user);
        return ApiResponse.success(detailPopup);
    }

    // 오픈 예정 팝업
    @Operation(summary = "오픈 예정 팝업 조회 API")
    @GetMapping("/planned")
    public ApiResponse<List<PopupPlannedDto>> getPlannedPopups(
            @RequestParam @Valid Integer page,
            @RequestParam @Valid Integer size) {
        List<PopupPlannedDto> plannedPopups = popupService.getPlannedPopup(page, size);
        return ApiResponse.success(plannedPopups);
    }

    // 트랜드 팝업 조회
    @Operation(summary = "트랜드 팝업 조회 API")
    @GetMapping("/trend")
    public ApiResponse<List<PopupTrendDto>> getTrendPopups(
            @RequestParam @Valid Integer page,
            @RequestParam @Valid Integer size
    ) {
        List<PopupTrendDto> trendPopups = popupService.getTrendPopups(page, size);
        return ApiResponse.success(trendPopups);
    }

    // 팝업 검색 우선은 %like%로 검색하도록 이후에 실시간 검색,검색어 자동완성으로 개선
    @Operation(
            summary = "팝업 검색 조회 API",
            description = "팝업 이름 또는 EX) 서울 강남구 와 같은 위치를 받아 팝업을 반환합니다")
    @GetMapping("/search")
    public ApiResponse<List<PopupSearchedNearbyDto>> getSearchedPopups(
            @RequestParam String content,
            @RequestParam @Valid Integer page,
            @RequestParam @Valid Integer size
    ) throws UnsupportedEncodingException {
        List<PopupSearchedNearbyDto> popupSearchedNearbyDtos = popupService.getSearchedPopups(content, page, size);
        return ApiResponse.success(popupSearchedNearbyDtos);
    }

/*    @Operation(
            summary = "현재 자신의 위치 기반 팝업 조회 API",
            description = "팝업 검색창 진입시 현재 위치를 기반으로 팝업을 조회합니다")
    @GetMapping("/location")
    public ApiResponse<List<PopupSearchDto>> getPopupsByLocation() {
        return null;
    }*/

}
