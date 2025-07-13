/*
package com.example.poppop.domain.popup.service;

import com.example.poppop.domain.popup.entity.Popup;
import com.example.poppop.domain.popup.repository.PopupRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(properties = {
        "naver.geocoding.client-id=ro8kabizfn",
        "naver.geocoding.client-secret=buhLpIaY3B586tOIFZHlkEoW2UYLOM19jxEADpc7",
        "app.data-init.enabled=false"  // 중요: CommandLineRunner 비활성화
})
class PopupGeoServiceTest {

    @Autowired
    private PopupGeoService popupGeoService;

    @Autowired
    private PopupRepository popupRepository;

    @Test
    public void testGetLatLng_성공케이스() throws Exception {
        // Given
        String testAddress = "서울특별시 강남구 테헤란로 123";

        try {
            // When
            BigDecimal[] latLng = popupGeoService.getLatLng(testAddress);

            // Then
            assertNotNull(latLng, "위도 경도 값이 null이면 안됩니다.");
            assertEquals(2, latLng.length, "위도 경도 배열 길이는 2여야 합니다.");
            assertNotNull(latLng[0], "경도 값이 null이면 안됩니다.");
            assertNotNull(latLng[1], "위도 값이 null이면 안됩니다.");

            System.out.println("✅ 테스트 성공!");
            System.out.println("주소: " + testAddress);
            System.out.println("경도: " + latLng[0] + ", 위도: " + latLng[1]);

            // 서울 지역 좌표 범위 검증
            assertTrue(latLng[0].doubleValue() > 126 && latLng[0].doubleValue() < 128,
                    "경도가 서울 범위에 있어야 합니다. 실제값: " + latLng[0]);
            assertTrue(latLng[1].doubleValue() > 37 && latLng[1].doubleValue() < 38,
                    "위도가 서울 범위에 있어야 합니다. 실제값: " + latLng[1]);

        } catch (Exception e) {
            System.out.println("❌ 테스트 실패: " + e.getMessage());
            throw e;
        }
    }

    @Test
    public void testDbGetLatLng() throws Exception {
        List<Popup> popups = popupRepository.findAll().stream()
                .limit(5)
                .toList();
        try {
            if (popups.size() <= 0) {
                System.out.println("db에서 팝업 데이터 찾을 수 없음");
                return;
            }
            for (Popup popup : popups) {
                BigDecimal[] latLng = popupGeoService.getLatLng(popup.getLocation());
                // Then
                assertNotNull(latLng, "위도 경도 값이 null이면 안됩니다.");
                assertEquals(2, latLng.length, "위도 경도 배열 길이는 2여야 합니다.");
                assertNotNull(latLng[0], "경도 값이 null이면 안됩니다.");
                assertNotNull(latLng[1], "위도 값이 null이면 안됩니다.");

                System.out.println("✅ 테스트 성공!");
                System.out.println("주소: " + popup.getLocation());
                System.out.println("경도: " + latLng[0] + ", 위도: " + latLng[1]);
            }
        }catch (Exception e) {
            System.out.println("❌ 테스트 실패: " + e.getMessage());
            throw e;
        }
    }

    @Test
    public void testGetLatLng_여러주소() throws Exception {
        String[] testAddresses = {
                "서울특별시 강남구 테헤란로 123",
                "경기도 성남시 분당구 불정로 6",
                "부산광역시 해운대구 해운대해변로 264",
                "대구광역시 중구 동성로2가 1"
        };

        for (String address : testAddresses) {
            BigDecimal[] latLng = popupGeoService.getLatLng(address);

            assertNotNull(latLng, "주소 '" + address + "'의 위도 경도 값이 null이면 안됩니다.");
            assertEquals(2, latLng.length, "위도 경도 배열 길이는 2여야 합니다.");

            System.out.println("주소: " + address + " → 경도: " + latLng[0] + ", 위도: " + latLng[1]);
        }
    }

    @Test
    public void testGetLatLng_잘못된주소() throws Exception {
        // Given
        String invalidAddress = "존재하지않는주소123456";

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            popupGeoService.getLatLng(invalidAddress);
        }, "잘못된 주소는 예외를 발생시켜야 합니다.");
    }

    @Test
    public void testGetLatLng_빈주소() throws Exception {
        // Given
        String emptyAddress = "";

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            popupGeoService.getLatLng(emptyAddress);
        }, "빈 주소는 예외를 발생시켜야 합니다.");
    }

}*/
