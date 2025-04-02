package com.project.crawlerservice.scheduled;

import com.project.crawlerservice.dto.DataDTO;
import com.project.crawlerservice.entity.enums.Currency;
import com.project.crawlerservice.entity.enums.Type;
import com.project.crawlerservice.service.DataService;
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
import java.util.Locale;

@Slf4j
@Component
public class MineCrawlerBatch {

    @PostConstruct
    void init(){
        gold();
    }

    private static final String WEB_SITE = "https://www.hangikredi.com";

    @Autowired
    private DataService dataService;

    @Scheduled(cron = "0 */5 * * * *")
    public void gold(){
        log.info("Started mine crawler.");
        try {
            List<String> codes = new ArrayList<>();
            List<DataDTO> dataDTOList = new ArrayList<>();
            Document document = Jsoup.connect(WEB_SITE + "/yatirim-araclari/altin-fiyatlari").get();
            Elements tbody = document.getElementsByClass("odd:bg-white even:bg-purple-100");
            for (Element element : tbody) {
                Elements td = element.getElementsByTag("td");
                String code = getKey(td.get(0).getElementsByTag("span").get(0).childNodes().get(0).toString().toUpperCase().trim());
                String name = td.get(0).getElementsByTag("span").get(0).childNodes().get(0).toString();
                BigDecimal buy = new BigDecimal(td.get(1).getElementsByTag("span").get(0).childNodes().get(0).toString().trim().replace(".","").replace(",",".")).setScale(5, RoundingMode.HALF_UP);
                BigDecimal sell = new BigDecimal(td.get(2).getElementsByTag("span").get(0).childNodes().get(0).toString().trim().replace(".","").replace(",",".")).setScale(5, RoundingMode.HALF_UP);
                try{
                    BigDecimal divide = (buy.add(sell)).divide(BigDecimal.valueOf(2), 5, RoundingMode.HALF_UP);
                    if(!code.equals("GRAM_GUMUS")){
                        dataDTOList.add(new DataDTO(code,name.trim(), Type.GOLD, divide,Currency.TL,Boolean.TRUE,new Date()));
                        codes.add(code);
                    }else{
                        dataDTOList.add(new DataDTO(code,name.trim(), Type.SILVER, divide,Currency.TL,Boolean.TRUE,new Date()));
                        codes.add(code);
                    }
                }catch (Exception e){
                    log.error("Mine not exist: " + code);
                }
            }
            if(!CollectionUtils.isEmpty(dataDTOList)){
                log.debug("Mine list: " + codes);
                dataService.save(dataDTOList);
            }
        }catch (Exception e){
            log.error("Mine page error: " + e.getLocalizedMessage());
        }
        log.info("Ended mine crawler.");
    }

    public static String getKey(String str){
        char[] charArray = str.toCharArray();
        StringBuilder result = new StringBuilder();
        for (char c : charArray) {
            if (Character.isDigit(c) || Character.isAlphabetic(c)) {
                result.append(c);
            } else if (c == ' ') {
                result.append('_');
            }
        }
        return result.toString()
                .replace("ç", "c")
                .replace("ı", "i")
                .replace("ğ", "g")
                .replace("ö", "o")
                .replace("ş", "s")
                .replace("ü", "u")
                .replace("Ç", "C")
                .replace("İ", "I")
                .replace("Ğ", "G")
                .replace("Ö", "O")
                .replace("Ş", "S")
                .replace("Ü", "U")
                .toUpperCase(Locale.ENGLISH);
    }

}