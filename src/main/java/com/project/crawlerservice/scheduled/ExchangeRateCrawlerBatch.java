package com.project.crawlerservice.scheduled;

import com.project.crawlerservice.dto.DataDTO;
import com.project.crawlerservice.dto.ExchangeRateDTO;
import com.project.crawlerservice.entity.enums.Currency;
import com.project.crawlerservice.entity.enums.Type;
import com.project.crawlerservice.service.DataService;
import com.project.crawlerservice.service.ExchangeRateService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class ExchangeRateCrawlerBatch {

    @PostConstruct
    void init(){
        exchangeRate();
    }

    private static final String WEB_SITE = "http://www.denizbank.com";

    @Autowired
    private ExchangeRateService exchangeRateService;

    @Scheduled(cron = "0 */5 * * * *")
    public void exchangeRate(){
        log.info("Started exchange rate crawler.");
        try {
            List<String> codes = new ArrayList<>();
            List<ExchangeRateDTO> exchangeRateDTOList = new ArrayList<>();
            exchangeRateDTOList.add(new ExchangeRateDTO(Currency.TL,"Türk Lirası",BigDecimal.ONE,BigDecimal.ONE,new Date()));
            codes.add(Currency.TL.name());
            Document document = Jsoup.connect(WEB_SITE + "/yatirim/piyasalar/doviz").get();
            Elements tbody = document.getElementsByClass("render-list");
            Elements tr = tbody.get(0).getElementsByTag("tr");
            for (Element element : tr) {
                Elements td = element.getElementsByTag("td");
                String code = td.get(0).getElementsByClass("title flex-row d-flex align-items-center").get(0).getElementsByTag("b").get(0).childNodes().get(0).toString();
                String name = td.get(0).getElementsByClass("name").get(0).childNodes().get(0).toString().trim();
                BigDecimal buy = new BigDecimal(td.get(1).getElementsByTag("span").get(0).childNodes().get(0).toString().trim().replace(",", "")).setScale(5, RoundingMode.HALF_UP);
                BigDecimal sell = new BigDecimal(td.get(2).getElementsByTag("span").get(0).childNodes().get(0).toString().trim().replace(",", "")).setScale(5, RoundingMode.HALF_UP);
                try{
                    exchangeRateDTOList.add(new ExchangeRateDTO(Currency.valueOf(code.trim()),name.trim(),buy,sell,new Date()));
                    codes.add(code.trim());
                }catch (Exception e){
                    log.error("Exchange rate not exist: " + code.trim());
                }
            }
            if(!CollectionUtils.isEmpty(exchangeRateDTOList)){
                log.debug("Exchange rate list: " + codes);
                exchangeRateService.save(exchangeRateDTOList);
            }
        }catch (Exception e){
            log.error("Exchange rate page error: " + e.getLocalizedMessage());
        }
        log.info("Ended exchange rate crawler.");
    }

}