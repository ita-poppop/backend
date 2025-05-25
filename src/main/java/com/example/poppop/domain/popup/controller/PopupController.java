package com.example.poppop.domain.popup.controller;

import com.example.poppop.domain.memeber.entity.CustomOAuth2User;
import com.example.poppop.domain.popup.dto.*;
import com.example.poppop.domain.popup.dto.request.PopupLocationRequestDto;
import com.example.poppop.domain.popup.dto.request.PopupSearchRequestDto;
import com.example.poppop.domain.popup.service.PopupService;
import com.example.poppop.global.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/popups")
public class PopupController {
    private final PopupService popupService;

    // 팝업 상세 조회 api
    @GetMapping("/{popupId}")
    public ApiResponse<PopupDetailDto> getDetailPopup(
            @PathVariable Long popupId,
            @AuthenticationPrincipal CustomOAuth2User oauth2User
    ) {
        PopupDetailDto detailPopup = popupService.getDetailPopup(popupId);
        popupService.incrementViewCount(popupId, oauth2User);
        return ApiResponse.success(detailPopup);
    }

    // 오픈 예정 팝업
    @GetMapping("/planned")
    public ApiResponse<List<PopupPlannedDto>> getPlannedPopups(
            @RequestParam @Valid Integer page,
            @RequestParam @Valid Integer size) {
        List<PopupPlannedDto> plannedPopups = popupService.getPlannedPopup(page, size);
        return ApiResponse.success(plannedPopups);
    }

    // 트랜드 팝업 조회
    @GetMapping("/trend")
    public ApiResponse<List<PopupTrendDto>> getTrendPopups(
            @RequestParam @Valid Integer page,
            @RequestParam @Valid Integer size
    ) {
        List<PopupTrendDto> trendPopups = popupService.getTrendPopups(page, size);
        return ApiResponse.success(trendPopups);
    }

    // 팝업 검색 우선은 %like%로 검색하도록 이후에 실시간 검색,검색어 자동완성으로 개선
    @GetMapping("/search")
    public ApiResponse<List<PopupSearchDto>> getSearchedPopups(
            @RequestBody PopupSearchRequestDto requestDto,
            @RequestParam @Valid Integer page,
            @RequestParam @Valid Integer size
    ) {
        List<PopupSearchDto> popupSearchedDtos = popupService.getSearchedPopups(page, size, requestDto);
        return ApiResponse.success(popupSearchedDtos);
    }

/*    // 위치 기반 팝업 검색
    @GetMapping()/"nearby")
    public ApiResponse<List<PopupLocationDto>> getNearbyPopups(
            @RequestBody
    )*/

/*    // ex) 서울 강남구 검색시 주변 팝업 반환
    public ApiResponse<List<PopupSearchedNearbyDto>> getSearchedNearbyPopups(
            @RequestBody PopupLocationRequestDto popupLocationRequestDto,
            @RequestParam @Valid Integer page,
            @RequestParam @Valid Integer size
    ) {
        popupService.getSearchedNearbyPopups(popupLocationRequestDto, page, size);
    }*/
}
