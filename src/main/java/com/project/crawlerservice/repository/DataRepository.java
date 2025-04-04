package com.project.crawlerservice.repository;

import com.project.crawlerservice.entity.DataEntity;
import com.project.crawlerservice.entity.embeddable.DataEmbeddableId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DataRepository extends JpaRepository<DataEntity, DataEmbeddableId> {

}