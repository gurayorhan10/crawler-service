package com.project.crawlerservice.job.exchangerate.tasklet;

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

@Slf4j
public class ExchangeRateTasklet implements Tasklet {

    private static final String WEB_SITE = "http://www.denizbank.com";

    @Autowired
    private DataService dataService;

    @Autowired
    private ExchangeRateService exchangeRateService;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
        try {
            List<String> codes = new ArrayList<>();
            List<DataDTO> dataDTOList = new ArrayList<>();
            List<ExchangeRateDTO> exchangeRateDTOList = new ArrayList<>();
            exchangeRateDTOList.add(new ExchangeRateDTO(Currency.TL, "Türk Lirası", BigDecimal.ONE, BigDecimal.ONE, new Date()));
            dataDTOList.add(new DataDTO(Currency.TL.name(), "Türk Lirası", Type.MONEY, BigDecimal.ONE, BigDecimal.ONE, Currency.TL, Boolean.TRUE, new Date()));
            codes.add(Currency.TL.name());
            Document document = Jsoup.connect(WEB_SITE + "/yatirim/piyasalar/doviz").get();
            Elements tbody = document.getElementsByClass("render-list");
            Elements tr = tbody.get(0).getElementsByTag("tr");
            for (Element element : tr) {
                String code = "";
                try {
                    Elements td = element.getElementsByTag("td");
                    code = td.get(0).getElementsByClass("title flex-row d-flex align-items-center").get(0).getElementsByTag("b").get(0).childNodes().get(0).toString();
                    String name = td.get(0).getElementsByClass("name").get(0).childNodes().get(0).toString().trim();
                    BigDecimal buy = new BigDecimal(td.get(1).getElementsByTag("span").get(0).childNodes().get(0).toString().trim().replace(",", "")).setScale(5, RoundingMode.HALF_UP);
                    BigDecimal sell = new BigDecimal(td.get(2).getElementsByTag("span").get(0).childNodes().get(0).toString().trim().replace(",", "")).setScale(5, RoundingMode.HALF_UP);
                    BigDecimal divide = (buy.add(sell)).divide(BigDecimal.valueOf(2), 5, RoundingMode.HALF_UP);
                    exchangeRateDTOList.add(new ExchangeRateDTO(Currency.valueOf(code.trim()), name.trim(), buy, sell, new Date()));
                    dataDTOList.add(new DataDTO(Currency.valueOf(code.trim()).name(), name.trim(), Type.MONEY, divide, divide, Currency.valueOf(code.trim()), Boolean.TRUE, new Date()));
                    codes.add(code.trim());
                } catch (Exception e) {
                    log.error("Exchange rate not exist: " + code.trim());
                }
            }
            if (!CollectionUtils.isEmpty(exchangeRateDTOList)) {
                log.debug("Exchange rate list: " + codes);
                exchangeRateService.save(exchangeRateDTOList);
                dataService.save(Type.MONEY,dataDTOList);
            }
        } catch (Exception e) {
            log.error("Exchange rate page error: " + e.getLocalizedMessage());
        }
        return RepeatStatus.FINISHED;

    }

}