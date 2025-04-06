package com.project.crawlerservice.service;

import com.project.crawlerservice.dto.ExchangeRateDTO;
import com.project.crawlerservice.entity.ExchangeRateEntity;
import com.project.crawlerservice.enums.Currency;
import com.project.crawlerservice.repository.ExchangeRateRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExchangeRateService {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private ExchangeRateRepository exchangeRateRepository;

    public void save(List<ExchangeRateDTO> exchangeRateDTOList){
        exchangeRateRepository.saveAll(exchangeRateDTOList.stream().map(data -> mapper.map(data, ExchangeRateEntity.class)).toList());
    }

    public Optional<ExchangeRateDTO> findByExchangeRate(Currency currency){
        return findAllExchangeRate().stream().filter(f -> f.getCurrency().equals(currency)).findFirst();
    }

    @Cacheable(value = "findAllExchangeRate", sync = true)
    public List<ExchangeRateDTO> findAllExchangeRate(){
        return exchangeRateRepository.findAll().stream()
                .map(data -> mapper.map(data, ExchangeRateDTO.class)).toList();
    }


}
