package com.project.crawlerservice.service;

import com.project.crawlerservice.dto.InterestDTO;
import com.project.crawlerservice.entity.InterestEntity;
import com.project.crawlerservice.repository.InterestRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InterestService {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private InterestRepository interestRepository;

    public List<InterestDTO> findFutureCalculationDateComeByUsername(String username){
        List<InterestDTO> interestDTOList = new ArrayList<>();
        interestRepository.findFutureCalculationDateComeByUsername(username)
                .forEach(interestEntity -> interestDTOList.add(mapper.map(interestEntity, InterestDTO.class)));
        return interestDTOList;
    }

    public void saveAll(List<InterestDTO> interestDTOList){
        List<InterestEntity> interestEntityList = new ArrayList<>();
        interestDTOList.forEach(f -> interestEntityList.add(mapper.map(f, InterestEntity.class)));
        interestRepository.saveAll(interestEntityList);
    }

}
