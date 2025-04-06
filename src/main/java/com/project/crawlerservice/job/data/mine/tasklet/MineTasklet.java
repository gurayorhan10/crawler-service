package com.project.crawlerservice.job.data.mine.tasklet;

import com.project.crawlerservice.dto.DataDTO;
import com.project.crawlerservice.enums.Currency;
import com.project.crawlerservice.enums.Type;
import com.project.crawlerservice.service.DataService;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Slf4j
public class MineTasklet implements Tasklet {

    private static final String WEB_SITE = "https://www.hangikredi.com";

    @Autowired
    private DataService dataService;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext){
        try {
            List<String> goldCodes = new ArrayList<>();
            List<String> silverCodes = new ArrayList<>();
            List<DataDTO> goldDataDTOList = new ArrayList<>();
            List<DataDTO> silverDataDTOList = new ArrayList<>();
            Document document = Jsoup.connect(WEB_SITE + "/yatirim-araclari/altin-fiyatlari").get();
            Elements tbody = document.getElementsByClass("odd:bg-white even:bg-purple-100");
            for (Element element : tbody) {
                String code = "";
                try{
                    Elements td = element.getElementsByTag("td");
                    code = getKey(td.get(0).getElementsByTag("span").get(0).childNodes().get(0).toString().toUpperCase().trim());
                    String name = td.get(0).getElementsByTag("span").get(0).childNodes().get(0).toString();
                    BigDecimal buy = new BigDecimal(td.get(1).getElementsByTag("span").get(0).childNodes().get(0).toString().trim().replace(".","").replace(",",".")).setScale(5, RoundingMode.HALF_UP);
                    BigDecimal sell = new BigDecimal(td.get(2).getElementsByTag("span").get(0).childNodes().get(0).toString().trim().replace(".","").replace(",",".")).setScale(5, RoundingMode.HALF_UP);
                    BigDecimal divide = (buy.add(sell)).divide(BigDecimal.valueOf(2), 5, RoundingMode.HALF_UP);
                    if(!code.equals("GRAM_GUMUS")){
                        goldDataDTOList.add(new DataDTO(code,name.trim(),Type.GOLD,divide,divide,Currency.TL,Boolean.TRUE,new Date()));
                        goldCodes.add(code);
                    }else{
                        silverDataDTOList.add(new DataDTO(code,name.trim(),Type.SILVER,divide,divide,Currency.TL,Boolean.TRUE,new Date()));
                        silverCodes.add(code);
                    }
                }catch (Exception e){
                    log.error("Mine " + code + " parse error: " + e.getLocalizedMessage());
                }
            }
            if(!CollectionUtils.isEmpty(goldDataDTOList)){
                log.debug("Gold list: " + goldCodes);
                dataService.save(Type.GOLD,goldDataDTOList);
            }
            if(!CollectionUtils.isEmpty(silverDataDTOList)){
                log.debug("Silver list: " + silverCodes);
                dataService.save(Type.SILVER,silverDataDTOList);
            }
        }catch (Exception e){
            log.error("Mine page error: " + e.getLocalizedMessage());
        }
        return RepeatStatus.FINISHED;
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