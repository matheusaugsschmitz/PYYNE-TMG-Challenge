package com.tmg.codingchallenge.presentation.job;

import com.tmg.codingchallenge.presentation.service.CacheExpirationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CacheExpirationJob {

    private final CacheExpirationService service;

    /**
     * Cron expression "0 0/30 * * * ?" = Runs at minutes 0 and 30 from each hour.
     */
    @Scheduled(cron = "0 0/30 * * * ?")
    public void removeExpiredCacheEntries() {
        log.debug("Executing Cache Expiring Job");
        service.clearExpiredCacheEntries();
    }
}
