package com.project.crawlerservice.repository;

import com.project.crawlerservice.entity.InterestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterestRepository extends JpaRepository<InterestEntity, Long> {
    @Query(value = "SELECT I.* FROM DEV.INTEREST I WHERE DATEDIFF(I.FUTURE_CALCULATION_DATE,CURDATE()) <= 0 AND DATEDIFF(I.CALCULATION_DATE,I.FUTURE_CALCULATION_DATE) != 0 AND I.USERNAME = :username", nativeQuery = true)
    List<InterestEntity> findFutureCalculationDateComeByUsername(String username);
}