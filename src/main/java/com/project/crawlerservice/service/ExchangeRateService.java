package com.project.crawlerservice.service;

import com.project.crawlerservice.dto.ExchangeRateDTO;
import com.project.crawlerservice.entity.ExchangeRateEntity;
import com.project.crawlerservice.entity.ExchangeRateHistoryEntity;
import com.project.crawlerservice.repository.ExchangeRateHistoryRepository;
import com.project.crawlerservice.repository.ExchangeRateRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ExchangeRateService {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private ExchangeRateRepository exchangeRateRepository;

    @Autowired
    private ExchangeRateHistoryRepository exchangeRateHistoryRepository;

    public void save(List<ExchangeRateDTO> exchangeRateDTOList){
        List<ExchangeRateHistoryEntity> exchangeRateHistoryEntityList = new ArrayList<>();
        List<ExchangeRateEntity> exchangeRateEntityList = exchangeRateRepository.saveAll(exchangeRateDTOList.stream()
                .map(data -> mapper.map(data, ExchangeRateEntity.class)).toList());
        exchangeRateEntityList.forEach(dataEntity -> exchangeRateHistoryEntityList.add(new ExchangeRateHistoryEntity(null,dataEntity.getCurrency(),dataEntity.getBuy(),dataEntity.getSell(), dataEntity.getLastUpdateDate())));
        exchangeRateHistoryRepository.saveAll(exchangeRateHistoryEntityList);
    }

}
