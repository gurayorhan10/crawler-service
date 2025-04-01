package com.project.crawlerservice.scheduled;

import com.project.crawlerservice.dto.DataDTO;
import com.project.crawlerservice.entity.enums.Currency;
import com.project.crawlerservice.entity.enums.Type;
import com.project.crawlerservice.service.DataService;
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
public class FundCrawlerBatch {

    private static final String WEB_SITE = "http://www.bloomberght.com";

    @Autowired
    private DataService dataService;

    @Scheduled(cron = "0 0 7 * * *")
    public void fund(){
        log.info("Started fund crawler.");
        try {
            List<String> codes = new ArrayList<>();
            Document document = Jsoup.connect(WEB_SITE + "/yatirim-fonlari/fon-karsilastirma").get();
            Elements tbody = document.getElementsByTag("tbody");
            Elements tr = tbody.get(0).getElementsByTag("tr");
            List<DataDTO> dataDTOList = new ArrayList<>();
            for (Element element : tr) {
                Elements td = element.getElementsByTag("td");
                String code = td.get(0).getElementsByClass("font-bold").get(0).childNodes().get(0).toString();
                String name = td.get(1).getElementsByTag("a").get(0).childNodes().get(0).toString();
                BigDecimal value = new BigDecimal(td.get(2).getElementsByTag("a").get(0).childNodes().get(0).toString().trim().replace(",", ".")).setScale(5, RoundingMode.HALF_UP);
                dataDTOList.add(new DataDTO(code.toUpperCase().trim(),name.trim(),Type.FUND,value,Currency.TL,Boolean.TRUE,new Date()));
                codes.add(code.trim());
            }
            if(!CollectionUtils.isEmpty(dataDTOList)){
                log.debug("Fund list: " + codes);
                dataService.save(dataDTOList);
            }
        }catch (Exception e){
            log.error("Fund page error: " + e.getLocalizedMessage());
        }
        log.info("Ended fund crawler.");
    }

}