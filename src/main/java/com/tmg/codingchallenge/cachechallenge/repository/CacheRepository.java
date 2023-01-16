package com.tmg.codingchallenge.cachechallenge.repository;

import com.tmg.codingchallenge.cachechallenge.model.CacheEntry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Objects.isNull;
import static java.util.Optional.ofNullable;

@Slf4j
@Component
public class CacheRepository {
    private final Map<String, CacheEntry> cacheMap = new ConcurrentHashMap<>();
    private final Map<LocalDateTime, Map<String, CacheEntry>> expirationMap = new ConcurrentHashMap<>();

    public Optional<String> getOptionalValueByKey(String key) {
        return Optional.ofNullable(cacheMap.get(key))
                .map(CacheEntry::getValue);
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
        final CacheEntry cacheEntryFound = cacheMap.remove(key);
        removeExpirationEntry(cacheEntryFound);
        log.debug("Key {} removed from cache.", key);
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

    private void removeExpirationEntry(CacheEntry cacheEntry) {
        ofNullable(cacheEntry)
                .map(CacheEntry::getExpiresAt)
                .map(expirationMap::get)
                .ifPresent(expirationEntry -> {
                    expirationEntry.remove(cacheEntry.getKey());
                    if(expirationEntry.isEmpty())
                        expirationMap.remove(cacheEntry.getExpiresAt());
                });
    }

}
