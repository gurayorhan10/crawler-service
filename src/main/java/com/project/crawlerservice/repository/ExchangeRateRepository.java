package com.project.crawlerservice.repository;

import com.project.crawlerservice.entity.ExchangeRateEntity;
import com.project.crawlerservice.entity.enums.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExchangeRateRepository extends JpaRepository<ExchangeRateEntity, Type> {

}