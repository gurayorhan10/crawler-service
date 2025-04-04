package com.project.crawlerservice.repository;

import com.project.crawlerservice.entity.AssetEntity;
import com.project.crawlerservice.entity.embeddable.AssetEmbeddableId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssetRepository extends JpaRepository<AssetEntity, AssetEmbeddableId> {
    List<AssetEntity> findByAssetEmbeddableId_Username(String username);
}