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

    // TODO: put on README file a obs about not using Shedlock because of this particular test, but in real environments, if happens to use Schedule the Shedlock will help with concurrency problems

    /**
     * Cron expression "* * * * * *" = Runs every second.
     */
    @Scheduled(cron = "* * * * * *")
    public void removeExpiredCacheEntries() {
        log.debug("Executing Cache Expiring Job");
        service.clearExpiredCacheEntries();
    }
}
