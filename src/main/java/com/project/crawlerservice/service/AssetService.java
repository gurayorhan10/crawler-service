package com.project.crawlerservice.service;

import com.project.crawlerservice.dto.AssetDTO;
import com.project.crawlerservice.dto.InterestDTO;
import com.project.crawlerservice.entity.AssetEntity;
import com.project.crawlerservice.entity.InterestEntity;
import com.project.crawlerservice.entity.embeddable.AssetEmbeddableId;
import com.project.crawlerservice.enums.Type;
import com.project.crawlerservice.repository.AssetRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AssetService {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private AssetRepository assetRepository;

    public Optional<AssetDTO> findById(String username, String code, Type type){
        Optional<AssetEntity> optional = assetRepository.findById(new AssetEmbeddableId(username,code,type));
        return optional.map(assetEntity -> mapper.map(assetEntity, AssetDTO.class));
    }

    public List<AssetDTO> findByUsername(String username){
        return assetRepository.findByAssetEmbeddableId_Username(username).stream().map(assetEntity -> mapper.map(assetEntity, AssetDTO.class)).collect(Collectors.toList());
    }

    public void delete(String username, String code, Type type){
        assetRepository.deleteById(new AssetEmbeddableId(username,code,type));
    }

    public void saveAll(List<AssetDTO> assetDTOList){
        List<AssetEntity> assetEntityList = new ArrayList<>();
        assetDTOList.forEach(f -> assetEntityList.add(mapper.map(f, AssetEntity.class)));
        assetRepository.saveAll(assetEntityList);
    }

}
