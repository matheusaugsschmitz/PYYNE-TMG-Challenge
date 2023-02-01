package com.tmg.codingchallenge.data;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;

@Slf4j
@Component
public class CacheRepository {
    private final Map<String, CacheEntry> cacheMap = new ConcurrentHashMap<>();
    private final Map<LocalDateTime, Map<String, CacheEntry>> expirationMap = new ConcurrentHashMap<>();

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
        if (!isNull(cacheEntry.getExpiresAt())) {
            saveExpirationEntry(cacheEntry);
        }
    }

    private void saveExpirationEntry(CacheEntry cacheEntry) {
        Map<String, CacheEntry> expirationEntry = getExpirationEntry(cacheEntry);
        expirationEntry.put(cacheEntry.getKey(), cacheEntry);
        log.debug("Key {} added to expiration map with tll {}.", cacheEntry.getKey(), cacheEntry.getTtl());
    }

    public void deleteByKey(String key) {
        if (nonNull(cacheMap.remove(key))) {
            log.debug("Key {} removed from cache.", key);
        }
    }

    public void deleteByExpirationTime(LocalDateTime time) {
        expirationMap.keySet()
                .stream()
                .filter(time::isAfter)
                .map(expirationMap::remove)
                .map(Map::values)
                .flatMap(Collection::stream)
                .forEach(cacheEntry -> cacheMap.remove(cacheEntry.getKey(), cacheEntry));
    }

    private Map<String, CacheEntry> getExpirationEntry(CacheEntry cacheEntry) {
        Map<String, CacheEntry> expirationEntry = expirationMap.get(cacheEntry.getExpiresAt());
        if (isNull(expirationEntry)) {
            expirationEntry = new ConcurrentHashMap<>();
            expirationMap.put(cacheEntry.getExpiresAt(), expirationEntry);
        }
        return expirationEntry;
    }

}
