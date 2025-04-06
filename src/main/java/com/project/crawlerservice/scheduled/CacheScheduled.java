package com.project.crawlerservice.scheduled;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Objects;

@Slf4j
@Component
public class CacheScheduled {

    @Autowired
    private CacheManager cacheManager;

    @Scheduled(cron = "0 */5 * * * *")
    public void cacheClear() {
        cacheManager.getCacheNames().parallelStream().forEach(name -> Objects.requireNonNull(cacheManager.getCache(name)).clear());
    }

}
