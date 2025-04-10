package com.project.crawlerservice.scheduled.crawler;

import com.project.crawlerservice.client.CoinGeckoClient;
import com.project.crawlerservice.client.dto.CoinMarketDTO;
import com.project.crawlerservice.dto.DataDTO;
import com.project.crawlerservice.enums.Currency;
import com.project.crawlerservice.enums.Type;
import com.project.crawlerservice.service.DataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Slf4j
@Component
public class CoinScheduled {

    @Autowired
    private DataService dataService;

    @Autowired
    private CoinGeckoClient coinGeckoClient;


    @Scheduled(cron = "*/30 * * * * *")
    public void coinScheduled(){
        List<DataDTO> dataDTOList = new ArrayList<>();
        List<CoinMarketDTO> coinMarketDTOList = new ArrayList<>();
        coinMarketDTOList.addAll(coinGeckoClient.getCoins("try",250,1));
        coinMarketDTOList.addAll(coinGeckoClient.getCoins("try",250,2));
        coinMarketDTOList.forEach(coinMarketDTO -> {
            if(coinMarketDTO.getCurrent_price().setScale(5, RoundingMode.HALF_UP).compareTo(BigDecimal.ZERO) > 0){
                dataDTOList.add(new DataDTO(coinMarketDTO.getSymbol().toUpperCase(Locale.ENGLISH),coinMarketDTO.getName(),Type.COIN,coinMarketDTO.getCurrent_price(),coinMarketDTO.getCurrent_price(),Currency.TL,Boolean.TRUE,new Date()));
            }
        });
        if(!CollectionUtils.isEmpty(dataDTOList)){
            dataService.save(Type.COIN,dataDTOList);
        }
    }

}
