package com.project.crawlerservice.repository;

import com.project.crawlerservice.entity.ExchangeRateHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExchangeRateHistoryRepository extends JpaRepository<ExchangeRateHistoryEntity, Long> {

}