package com.project.crawlerservice.job.interest.step;

import com.project.crawlerservice.dto.InterestDTO;
import com.project.crawlerservice.job.interest.dto.InterestCalculationProcessorDTO;
import com.project.crawlerservice.job.interest.dto.InterestCalculationWriterDTO;
import com.project.crawlerservice.service.InterestService;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

public class InterestCalculationProcessor implements ItemProcessor<InterestCalculationProcessorDTO, InterestCalculationWriterDTO> {

    @Autowired
    private InterestService interestService;


    @Override
    public InterestCalculationWriterDTO process(InterestCalculationProcessorDTO interestCalculationProcessorDTO) {
        InterestCalculationWriterDTO interestCalculationWriterDTO = new InterestCalculationWriterDTO();
        List<InterestDTO> interestDTOList = interestService.findFutureCalculationDateComeByUsername(interestCalculationProcessorDTO.getUsername());
        interestDTOList.forEach(interestDTO -> {
            interestDTO.setCalculationDate(interestDTO.getFutureCalculationDate());
            BigDecimal pieceToHundred = interestDTO.getPiece().divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
            BigDecimal ratioToYearDay = interestDTO.getRatio().divide(BigDecimal.valueOf(365), 2, RoundingMode.HALF_UP);
            BigDecimal interest = pieceToHundred.multiply(ratioToYearDay).multiply(BigDecimal.valueOf(interestDTO.getDay()));
            BigDecimal interestTax = interest.multiply(interestDTO.getTaxRatio()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
            interestDTO.setPiece(interestDTO.getPiece().add(interest.subtract(interestTax)));
            interestDTO.setFutureCalculationDate(new Date());
      });
        interestCalculationWriterDTO.setInterestDTOList(interestDTOList);
        return interestCalculationWriterDTO;
    }

}
