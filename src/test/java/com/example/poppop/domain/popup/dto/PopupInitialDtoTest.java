package com.example.poppop.domain.popup.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PopupInitialDtoTest {
    @Test
    @DisplayName("복사, &nbsp;, 여러 줄 공백, 탭, 줄바꿈 모두 제거")
    void testRemoveLocationNbsp_AllCases() {
        String input = "서울특별시 송파구 올림픽로 240 롯데월드\t롯데백화점 잠실점\n지하1층 더 크라운&nbsp;\n\t\t\t\t\t\t\t\t복사";
        String expected = "서울특별시 송파구 올림픽로 240 롯데월드 롯데백화점 잠실점 지하1층 더 크라운";
        assertEquals(expected, PopupInitialDto.removeLocationNbsp(input));
    }

    @Test
    @DisplayName("복사만 제거")
    void testRemoveLocationNbsp_OnlyBoksa() {
        String input = "서울특별시 강남구 언주로 164길 21-3 복사";
        String expected = "서울특별시 강남구 언주로 164길 21-3";
        assertEquals(expected, PopupInitialDto.removeLocationNbsp(input));
    }

    @Test
    @DisplayName("여러 공백만 제거")
    void testRemoveLocationNbsp_OnlySpaces() {
        String input = "서울특별시   강남구   언주로 164길 21-3";
        String expected = "서울특별시 강남구 언주로 164길 21-3";
        assertEquals(expected, PopupInitialDto.removeLocationNbsp(input));
    }

    @Test
    @DisplayName("null 입력")
    void testRemoveLocationNbsp_Null() {
        assertNull(PopupInitialDto.removeLocationNbsp(null));
    }

    @Test
    @DisplayName("복사, &nbsp;가 없는 정상 주소")
    void testRemoveLocationNbsp_Normal() {
        String input = "서울특별시 중구 세종대로 110";
        String expected = "서울특별시 중구 세종대로 110";
        assertEquals(expected, PopupInitialDto.removeLocationNbsp(input));
    }
}