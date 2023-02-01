package com.tmg.codingchallenge.data;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Objects.nonNull;

@Slf4j
@Component
public class CacheRepository {
    private final Map<String, CacheEntry> cacheMap = new ConcurrentHashMap<>();

    public Optional<String> getOptionalValueByKey(String key) {
        CacheEntry cacheEntry = cacheMap.get(key);

        if (nonNull(cacheEntry) && nonNull(cacheEntry.getExpiresAt())
                && LocalDateTime.now().isAfter(cacheEntry.getExpiresAt())) {
            cacheMap.remove(key, cacheEntry);
            cacheEntry = null;
        }

        return Optional.ofNullable(cacheEntry).map(CacheEntry::getValue);
    }

    public void save(CacheEntry cacheEntry) {
        log.debug("Key {} added to cache with tll {}.", cacheEntry.getKey(), cacheEntry.getTtl());
        cacheMap.put(cacheEntry.getKey(), cacheEntry);
    }

    public void deleteByKey(String key) {
        if (nonNull(cacheMap.remove(key))) {
            log.debug("Key {} removed from cache.", key);
        }
    }

    public void deleteByExpirationTime(LocalDateTime time) {
        cacheMap.values()
                .stream()
                .filter(cacheEntry -> nonNull(cacheEntry.getExpiresAt()) && cacheEntry.getExpiresAt().isBefore(time))
                .forEach(cacheEntry -> cacheMap.remove(cacheEntry.getKey(), cacheEntry));
    }

}
