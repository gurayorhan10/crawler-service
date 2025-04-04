package com.project.crawlerservice.job.interest.dto;

import com.project.crawlerservice.dto.InterestDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class InterestCalculationWriterDTO {
    private List<InterestDTO> interestDTOList = new ArrayList<>();
}
