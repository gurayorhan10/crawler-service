package com.project.crawlerservice.scheduled;

import com.project.crawlerservice.dto.DataDTO;
import com.project.crawlerservice.entity.enums.Currency;
import com.project.crawlerservice.entity.enums.Type;
import com.project.crawlerservice.service.DataService;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
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
public class CoinCrawlerBatch {

    private static final Integer DATA_SIZE = 250;
    private static final Integer LAST_PAGE = 3;
    private static final String WEB_SITE = "https://coinmarketcap.com";

    @Autowired
    private DataService dataService;

    @Scheduled(cron = "0 */1 * * * *")
    public void coin(){
        log.info("Started coin crawler.");
        int i = 0;
        int size = 0;
        do {
            log.debug("Coin page " + i + " searching...");
            try {
                i++;
                List<String> codes = new ArrayList<>();
                Document document = Jsoup.connect(WEB_SITE + "/?page="+i).get();
                Elements table = document.getElementsByClass("cmc-table");
                Elements tbody = table.get(0).getElementsByTag("tbody");
                Elements tr = tbody.get(0).getElementsByTag("tr");
                List<DataDTO> dataDTOList = new ArrayList<>();
                for (int j = 0; j < tr.size(); j++) {
                    size++;
                    String code;
                    String name;
                    BigDecimal value;
                    Elements td = tr.get(j).getElementsByTag("td");
                    if(j < 10){
                        code = td.get(2).getElementsByClass("coin-item-symbol").get(0).childNodes().get(0).toString();
                        name = td.get(2).getElementsByClass("coin-item-name").get(0).childNodes().get(0).toString();
                        value = new BigDecimal(td.get(3).getElementsByTag("span").get(0).childNodes().get(0).toString().trim().replace("$", "").replace(",", "")).setScale(5, RoundingMode.HALF_UP);
                    }else{
                        code = td.get(2).getElementsByTag("span").get(2).childNodes().get(0).toString();
                        name = td.get(2).getElementsByTag("span").get(1).childNodes().get(0).toString();
                        value = new BigDecimal(td.get(3).childNodes().get(2).toString().trim().replace(",", "")).setScale(5, RoundingMode.HALF_UP);
                    }
                    dataDTOList.add(new DataDTO(code.toUpperCase().trim(),name.trim(),Type.COIN,value,Currency.USD,Boolean.TRUE,new Date()));
                    codes.add(code.trim());
                    if(size >= DATA_SIZE){
                        break;
                    }
                }
                if(!CollectionUtils.isEmpty(dataDTOList)){
                    log.debug("Page " + i + " coin list: " + codes);
                    dataService.save(dataDTOList);
                }
            }catch (Exception e){
                log.error("Coin page " + i + " error: " + e.getLocalizedMessage());
            }
            log.debug("Coin page " + i + " ended.");
        }while (i <= LAST_PAGE && size <= DATA_SIZE);
        log.info("Ended coin crawler.");
    }

}