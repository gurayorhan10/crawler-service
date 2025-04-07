package com.project.crawlerservice.scheduled.crawler;

import com.project.crawlerservice.dto.DataDTO;
import com.project.crawlerservice.enums.Currency;
import com.project.crawlerservice.enums.Type;
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
public class StockScheduled {

    private static final Integer LAST_PAGE = 31;
    private static final String WEB_SITE = "http://www.bloomberght.com";

    @Autowired
    private DataService dataService;

    @Scheduled(cron = "0 50 9 * * 1-5")
    @Scheduled(cron = "0 55 9 * * 1-5")
    @Scheduled(cron = "0 */5 10-18 * * 1-5")
    public void stockScheduled(){
        int i = 1;
        do {
            log.debug("Stock page " + i + " searching...");
            try {
                List<String> codes = new ArrayList<>();
                Document document = Jsoup.connect(WEB_SITE + "/borsa/hisseler/"+i).get();
                Elements tbody = document.getElementsByTag("tbody");
                Elements tr = tbody.get(0).getElementsByTag("tr");
                List<DataDTO> dataDTOList = new ArrayList<>();
                for (Element element : tr) {
                    String code = "";
                    try {
                        Elements td = element.getElementsByTag("td");
                        code = td.get(0).getElementsByClass("font-bold").get(0).childNodes().get(0).toString();
                        String name = td.get(0).getElementsByClass("text-ellipses line-clamp-1 text-sm").get(0).childNodes().get(0).toString();
                        BigDecimal value = new BigDecimal(td.get(1).getElementsByClass("lastPrice").get(0).childNodes().get(0).toString().trim().replace(".", "").replace(",", ".")).setScale(5, RoundingMode.HALF_UP);
                        dataDTOList.add(new DataDTO(code.toUpperCase().trim(),name.trim(), Type.STOCK,value,value, Currency.TL,Boolean.TRUE,new Date()));
                        codes.add(code.trim());
                    }catch (Exception e){
                        log.error("Stock " + code + " parse error: " + e.getLocalizedMessage());
                    }
                }
                if(!CollectionUtils.isEmpty(dataDTOList)){
                    log.debug("Stock page " + i + " list: " + codes);
                    dataService.save(Type.STOCK,dataDTOList);
                }
            }catch (Exception e){
                log.error("Stock page " + i + " error: " + e.getLocalizedMessage());
            }
            log.debug("Stock page " + i + " ended.");
            i++;
        }while (i <= LAST_PAGE);
    }

}
