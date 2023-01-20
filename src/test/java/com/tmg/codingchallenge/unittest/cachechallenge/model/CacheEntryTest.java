package com.tmg.codingchallenge.unittest.cachechallenge.model;

import com.tmg.codingchallenge.data.CacheEntry;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mockStatic;

class CacheEntryTest {

    @Test
    void constructorWithoutTTL_withSuccess() {
        // Arrange
        String key = "name";
        String value = "Johana";

        // Act
        CacheEntry cacheEntry = new CacheEntry(key, value, null);

        // Assert
        assertEquals(key, cacheEntry.getKey());
        assertEquals(value, cacheEntry.getValue());
        assertNull(cacheEntry.getTtl());
        assertNull(cacheEntry.getExpiresAt());
    }

    @Test
    void constructorWithPositiveTTL_withSuccess() {
        // Arrange
        String key = "name";
        String value = "Johana";
        Long ttl = 30L;
        LocalDateTime mockedTime = LocalDateTime.now();
        LocalDateTime expectedExpirationTime = mockedTime.plus(ttl, ChronoUnit.SECONDS).truncatedTo(ChronoUnit.SECONDS);

        CacheEntry cacheEntry;
        try (MockedStatic<LocalDateTime> mockedStatic = mockStatic(LocalDateTime.class)) {
            mockedStatic.when(LocalDateTime::now).thenReturn(mockedTime);

            // Act
            cacheEntry = new CacheEntry(key, value, ttl);
        }

        // Assert
        assertEquals(key, cacheEntry.getKey());
        assertEquals(value, cacheEntry.getValue());
        assertEquals(ttl, cacheEntry.getTtl());
        assertEquals(expectedExpirationTime, cacheEntry.getExpiresAt());
    }

    @Test
    void constructorWithZeroedTTL_withSuccess() {
        // Arrange
        String key = "name";
        String value = "Johana";

        // Act
        CacheEntry cacheEntry = new CacheEntry(key, value, 0L);

        // Assert
        assertEquals(key, cacheEntry.getKey());
        assertEquals(value, cacheEntry.getValue());
        assertNull(cacheEntry.getTtl());
        assertNull(cacheEntry.getExpiresAt());
    }

    @Test
    void constructorWithNegativeTTL_withSuccess() {
        // Arrange
        String key = "name";
        String value = "Johana";

        // Act
        CacheEntry cacheEntry = new CacheEntry(key, value, -5L);

        // Assert
        assertEquals(key, cacheEntry.getKey());
        assertEquals(value, cacheEntry.getValue());
        assertNull(cacheEntry.getTtl());
        assertNull(cacheEntry.getExpiresAt());
    }

}
