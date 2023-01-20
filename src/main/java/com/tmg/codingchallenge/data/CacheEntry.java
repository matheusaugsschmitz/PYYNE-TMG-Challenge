package com.tmg.codingchallenge.data;

import lombok.Getter;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static java.util.Objects.isNull;

@Getter
public class CacheEntry {

    private final String key;
    private final String value;
    private final Long ttl;
    private final LocalDateTime expiresAt;

    public CacheEntry(String key, String value, Long ttl) {
        this.key = key;
        this.value = value;

        if (!isNull(ttl) && ttl.compareTo(0L) > 0) {
            this.ttl = ttl;
            this.expiresAt = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plus(ttl, ChronoUnit.SECONDS);
        } else {
            this.ttl = null;
            this.expiresAt = null;
        }
    }

}
