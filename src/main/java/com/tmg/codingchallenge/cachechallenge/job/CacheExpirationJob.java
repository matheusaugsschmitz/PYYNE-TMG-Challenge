package com.tmg.codingchallenge.cachechallenge.job;

import com.tmg.codingchallenge.cachechallenge.service.CacheExpirationService;
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
     * Cron expression "* * * * * *" = Runs every second.
     */
    @Scheduled(cron = "* * * * * *")
    public void removeExpiredCacheEntries() {
        log.debug("Executing Cache Expiring Job");
        service.clearExpiredCacheEntries();
    }
}
