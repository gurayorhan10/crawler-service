package com.project.crawlerservice.service;

import com.project.crawlerservice.dto.ExchangeRateDTO;
import com.project.crawlerservice.entity.ExchangeRateEntity;
import com.project.crawlerservice.repository.ExchangeRateRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExchangeRateService {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private ExchangeRateRepository exchangeRateRepository;

    public void save(List<ExchangeRateDTO> exchangeRateDTOList){
        exchangeRateRepository.saveAll(exchangeRateDTOList.stream().map(data -> mapper.map(data, ExchangeRateEntity.class)).toList());
    }

}
