package com.project.crawlerservice.job.interest.step;

import com.project.crawlerservice.dto.AssetDTO;
import com.project.crawlerservice.dto.InterestDTO;
import com.project.crawlerservice.enums.Type;
import com.project.crawlerservice.job.interest.dto.InterestCalculationWriterDTO;
import com.project.crawlerservice.service.AssetService;
import com.project.crawlerservice.service.ExchangeRateService;
import com.project.crawlerservice.service.InterestService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Slf4j
public class InterestCalculationWriter implements ItemWriter<InterestCalculationWriterDTO> {

    @Autowired
    private AssetService assetService;

    @Autowired
    private InterestService interestService;

    @Autowired
    private ExchangeRateService exchangeRateService;

    @Override
    public void write(Chunk<? extends InterestCalculationWriterDTO> chunk) {
        Map<String, AssetDTO> assetDTOMap = new HashMap<>();
        List<InterestDTO> interestDTOList = new ArrayList<>();
        chunk.forEach(interestCalculationWriterDTO -> interestCalculationWriterDTO.getInterestDTOList().forEach(interestDTO -> {
            if (Boolean.TRUE.equals(interestDTO.getAgain())) {
                interestDTO.setFutureCalculationDate(DateUtils.addDays(new Date(), interestDTO.getDay()));
            } else {
                AssetDTO newAssetDTO = new AssetDTO();
                newAssetDTO.setType(Type.MONEY);
                newAssetDTO.setLastUpdateDate(new Date());
                newAssetDTO.setPiece(interestDTO.getPiece());
                newAssetDTO.setUsername(interestDTO.getUsername());
                newAssetDTO.setCurrency(interestDTO.getCurrency());
                newAssetDTO.setAverage(interestDTO.getExchangeRate());
                newAssetDTO.setCode(interestDTO.getCurrency().name());
                String key = newAssetDTO.getUsername()+"-"+newAssetDTO.getCode()+"-"+newAssetDTO.getType().name();
                Optional<AssetDTO> optionalAssetDTO = Optional.ofNullable(assetDTOMap.getOrDefault(key,null));
                optionalAssetDTO = optionalAssetDTO.isPresent() ? optionalAssetDTO : assetService.findById(newAssetDTO.getUsername(), newAssetDTO.getCode(), newAssetDTO.getType());
                if (optionalAssetDTO.isPresent()) {
                    AssetDTO assetDTO = optionalAssetDTO.get();
                    BigDecimal currentPiece = assetDTO.getPiece().multiply(assetDTO.getAverage()).setScale(5, RoundingMode.HALF_UP);
                    BigDecimal addPiece = newAssetDTO.getPiece().multiply(newAssetDTO.getAverage()).setScale(5, RoundingMode.HALF_UP);
                    BigDecimal lastAverage = currentPiece.add(addPiece).divide(assetDTO.getPiece().add(newAssetDTO.getPiece()), 5, RoundingMode.HALF_UP);
                    assetDTO.setPiece(newAssetDTO.getPiece().add(assetDTO.getPiece()));
                    assetDTO.setLastUpdateDate(new Date());
                    assetDTO.setAverage(lastAverage);
                    assetDTOMap.put(key,assetDTO);
                }else{
                    newAssetDTO.setAverage(newAssetDTO.getAverage());
                    newAssetDTO.setPiece(newAssetDTO.getPiece());
                    assetDTOMap.put(key,newAssetDTO);
                }
            }
            interestDTOList.add(interestDTO);
        }));
        assetService.saveAll(assetDTOMap.values().stream().toList());
        interestService.saveAll(interestDTOList);
    }

}