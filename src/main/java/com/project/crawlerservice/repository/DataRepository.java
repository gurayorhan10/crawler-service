package com.project.crawlerservice.repository;

import com.project.crawlerservice.entity.DataEntity;
import com.project.crawlerservice.entity.embeddable.DataEmbeddableId;
import com.project.crawlerservice.enums.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DataRepository extends JpaRepository<DataEntity, DataEmbeddableId> {
    List<DataEntity> findByDataEmbeddableId_Type(Type type);
    List<DataEntity> findByDataEmbeddableId_CodeInAndDataEmbeddableId_Type(List<String> codes,Type type);
}