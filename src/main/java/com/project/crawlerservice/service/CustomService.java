package com.project.crawlerservice.service;

import com.project.crawlerservice.dto.AssetDataDTO;
import com.project.crawlerservice.repository.jdbc.CustomJdbcRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomService {

    @Autowired
    private CustomJdbcRepository customJdbcRepository;

    public List<AssetDataDTO> findAssetDataDailyByUsername(String username){
        return customJdbcRepository.findAssetDataDailyByUsername(username);
    }

    public List<AssetDataDTO> findAssetDataHourlyByUsername(String username){
        return customJdbcRepository.findAssetDataHourlyByUsername(username);
    }

}
