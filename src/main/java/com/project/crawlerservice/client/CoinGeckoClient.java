package com.project.crawlerservice.client;

import com.project.crawlerservice.client.dto.CoinMarketDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "coinGeckoClient", url = "https://api.coingecko.com/api/v3")
public interface CoinGeckoClient {

    @GetMapping("/coins/markets")
    List<CoinMarketDTO> getCoins(@RequestParam("vs_currency") String currency, @RequestParam("per_page") int perPage, @RequestParam("page") int page);

}
