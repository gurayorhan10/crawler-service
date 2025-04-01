package com.project.crawlerservice.repository;

import com.project.crawlerservice.entity.DataHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DataHistoryRepository extends JpaRepository<DataHistoryEntity, Long> {

}