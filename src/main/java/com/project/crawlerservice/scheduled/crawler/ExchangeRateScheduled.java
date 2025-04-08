package com.project.crawlerservice.scheduled.crawler;

import com.project.crawlerservice.dto.DataDTO;
import com.project.crawlerservice.dto.ExchangeRateDTO;
import com.project.crawlerservice.enums.Currency;
import com.project.crawlerservice.enums.Type;
import com.project.crawlerservice.service.DataService;
import com.project.crawlerservice.service.ExchangeRateService;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class ExchangeRateScheduled {

    private static final String WEB_SITE = "http://www.denizbank.com";

    @Autowired
    private DataService dataService;

    @Autowired
    private ExchangeRateService exchangeRateService;


    @Scheduled(cron = "*/30 * * * * 1-5")
    public void exchangeRateScheduled(){
        try {
            List<DataDTO> dataDTOList = new ArrayList<>();
            List<ExchangeRateDTO> exchangeRateDTOList = new ArrayList<>();
            Document document = Jsoup.connect(WEB_SITE + "/yatirim/piyasalar/doviz").get();
            Elements tbody = document.getElementsByClass("render-list");
            Elements tr = tbody.get(0).getElementsByTag("tr");
            for (Element element : tr) {
                Currency currency = null;
                try {
                    Elements td = element.getElementsByTag("td");
                    try{
                        currency = Currency.valueOf(td.get(0).getElementsByClass("title flex-row d-flex align-items-center")
                                .get(0).getElementsByTag("b").get(0).childNodes().get(0).toString().trim());
                    }catch (Exception e){
                        continue;
                    }
                    String name = td.get(0).getElementsByClass("name").get(0).childNodes().get(0).toString().trim();
                    BigDecimal buy = new BigDecimal(td.get(1).getElementsByTag("span").get(0).childNodes().get(0)
                            .toString().trim().replace(",", "")).setScale(5, RoundingMode.HALF_UP);
                    BigDecimal sell = new BigDecimal(td.get(2).getElementsByTag("span").get(0).childNodes().get(0)
                            .toString().trim().replace(",", "")).setScale(5, RoundingMode.HALF_UP);
                    BigDecimal divide = (buy.add(sell)).divide(BigDecimal.valueOf(2), 5, RoundingMode.HALF_UP);
                    exchangeRateDTOList.add(new ExchangeRateDTO(currency, name, buy, sell, new Date()));
                    dataDTOList.add(new DataDTO(currency.name(), name, Type.MONEY, divide, divide, Currency.TL, Boolean.TRUE, new Date()));
                } catch (Exception e) {
                    log.error("Exchange rate parse error: " + (Objects.isNull(currency) ? "Empty" : currency.name()));
                }
            }
            dataDTOList.add(new DataDTO(Currency.TL.name(), "Türk Lirası", Type.MONEY, BigDecimal.ONE, BigDecimal.ONE, Currency.TL, Boolean.TRUE, new Date()));
            exchangeRateDTOList.add(new ExchangeRateDTO(Currency.TL, "Türk Lirası", BigDecimal.ONE, BigDecimal.ONE, new Date()));
            exchangeRateService.save(exchangeRateDTOList);
            dataService.save(Type.MONEY,dataDTOList);
        } catch (Exception e) {
            log.error("Exchange rate error: " + e.getLocalizedMessage());
        }
    }

}