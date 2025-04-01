package com.project.crawlerservice.service;

import com.project.crawlerservice.dto.DataDTO;
import com.project.crawlerservice.entity.DataEntity;
import com.project.crawlerservice.entity.DataHistoryEntity;
import com.project.crawlerservice.repository.DataHistoryRepository;
import com.project.crawlerservice.repository.DataRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DataService {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private DataRepository dataRepository;

    @Autowired
    private DataHistoryRepository dataHistoryRepository;

    public void save(List<DataDTO> dataDTOList){
        List<DataHistoryEntity> dataHistoryEntityList = new ArrayList<>();
        List<DataEntity> dataEntityList = dataRepository.saveAll(dataDTOList.stream()
                .map(data -> mapper.map(data, DataEntity.class)).toList());
        dataEntityList.forEach(dataEntity -> dataHistoryEntityList.add(new DataHistoryEntity(null,dataEntity.getDataEmbeddableId().getCode(),dataEntity.getDataEmbeddableId().getType(),dataEntity.getValue(), dataEntity.getLastUpdateDate())));
        dataHistoryRepository.saveAll(dataHistoryEntityList);
    }

}
