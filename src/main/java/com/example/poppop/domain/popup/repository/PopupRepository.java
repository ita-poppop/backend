package com.example.poppop.domain.popup.repository;

import com.example.poppop.domain.popup.entity.Popup;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface PopupRepository extends JpaRepository<Popup, Long> {

    // startDate가 오늘 이후인 팝업을 오름차순 정렬해서 페이징 조회
    @Query("SELECT p FROM Popup p WHERE p.startDate BETWEEN :now AND :end order by p.startDate asc")
    List<Popup> findPlannedPopups(@Param("now") LocalDate now, @Param("end") LocalDate end , Pageable pageable);

    @Query("select p FROM Popup p order by p.viewCount desc")
    List<Popup> findTrendPopups(Pageable pageable);

    @Query("select p from Popup p where p.title like %:title%")
    List<Popup> findSearchedPopups(Pageable pageable,String title);
    /*@Modifying
    @Transactional
    @Query("UPDATE Post p SET p.viewCnt = p.viewCnt + 1 WHERE p.postId = :postId")
    void incrementViewCount(@Param("postId") Long postId);*/
}