package com.example.poppop.domain.popup.repository;

import com.example.poppop.domain.popup.entity.Popup;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface PopupRepository extends JpaRepository<Popup, Long> {

    // startDate가 오늘 이후인 팝업을 오름차순 정렬해서 페이징 조회
    @Query("SELECT p FROM Popup p WHERE p.startDate BETWEEN :now AND :end order by p.startDate asc")
    List<Popup> findPlannedPopups(@Param("now") LocalDate now, @Param("end") LocalDate end , Pageable pageable);

    @Query("select p FROM Popup p order by p.viewCount desc")
    List<Popup> findTrendPopups(Pageable pageable);

    // 팝업 제목으로 검색 (부분 일치)
    @Query("SELECT p FROM Popup p WHERE p.title LIKE CONCAT('%', :title, '%') ORDER BY p.startDate ASC")
    List<Popup> findSearchedPopups(@Param("title") String title, Pageable pageable);

    @Query(
            value = "SELECT *, (6371 * acos(cos(radians(:lat)) * cos(radians(latitude)) * cos(radians(longitude) - radians(:lng)) + sin(radians(:lat)) * sin(radians(latitude)))) AS distance " +
                    "FROM popup " +
                    "HAVING distance <= :radius " +
                    "ORDER BY distance",
            countQuery = "SELECT count(*) FROM popup " +
                    "WHERE (6371 * acos(cos(radians(:lat)) * cos(radians(latitude)) * cos(radians(longitude) - radians(:lng)) + sin(radians(:lat)) * sin(radians(latitude)))) <= :radius",
            nativeQuery = true
    )
    List<Popup> findPopupsWithinRadius(
            @Param("lat") BigDecimal lat,
            @Param("lng") BigDecimal lng,
            @Param("radius") double radius,
            Pageable pageable
    );

}