package com.project.crawlerservice.service;

import com.project.crawlerservice.dto.DataDTO;
import com.project.crawlerservice.entity.DataEntity;
import com.project.crawlerservice.entity.DataHistoryEntity;
import com.project.crawlerservice.enums.Type;
import com.project.crawlerservice.repository.DataHistoryRepository;
import com.project.crawlerservice.repository.DataRepository;
import org.apache.commons.lang.time.DateUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DataService {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private DataRepository dataRepository;

    @Autowired
    private DataHistoryRepository dataHistoryRepository;

    public void save(Type type, List<DataDTO> dataDTOList){
        List<DataHistoryEntity> dataHistoryEntityList = new ArrayList<>();
        List<DataEntity> dataEntityList = dataRepository.findByDataEmbeddableId_CodeInAndDataEmbeddableId_Type(dataDTOList.stream().map(DataDTO::getCode).toList(),type);
        dataDTOList.forEach(dataDTO -> {
            Optional<DataEntity> dataEntity = dataEntityList.stream().filter(f -> dataDTO.getCode().equals(f.getDataEmbeddableId().getCode()) & dataDTO.getType().equals(type)).findFirst();
            if(dataEntity.isPresent()){
                if(DateUtils.truncate(dataEntity.get().getLastUpdateDate(), java.util.Calendar.DAY_OF_MONTH).getTime() != DateUtils.truncate(dataDTO.getLastUpdateDate(), java.util.Calendar.DAY_OF_MONTH).getTime()){
                    dataEntity.get().setDailyValue(dataEntity.get().getValue());
                }
                dataEntity.get().setLastUpdateDate(dataDTO.getLastUpdateDate());
                dataEntity.get().setValue(dataDTO.getValue());
            }else{
                dataEntityList.add(mapper.map(dataDTO, DataEntity.class));
            }
        });
        dataRepository.saveAll(dataEntityList).forEach(dataEntity -> dataHistoryEntityList
                .add(new DataHistoryEntity(null,dataEntity.getDataEmbeddableId().getCode(),dataEntity.getDataEmbeddableId().getType(),dataEntity.getValue(), dataEntity.getLastUpdateDate())));
        dataHistoryRepository.saveAll(dataHistoryEntityList);
    }

}