package com.tmg.codingchallenge.endtoendtest.cachechallenge;

import com.tmg.codingchallenge.data.CacheEntry;
import com.tmg.codingchallenge.data.CacheRepository;
import com.tmg.codingchallenge.presentation.controller.CacheController;
import com.tmg.codingchallenge.presentation.controller.NewCacheEntryRequestDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * These tests were made to validate the final result with predefined inputs.
 * I have decided to do these test for the challenge purpose only, in real applications I would not take this approach since it would be difficult to maintain in complex or bigger softwares.
 */
@SpringBootTest
class CacheControllerEndToEndTest {

    @Autowired
    private CacheController controller;

    @Autowired
    private CacheRepository cacheRepository;

    @AfterEach
    void clearRepositoryData() {
        getCacheEntryMap().clear();
    }

    @Test
    void addNameWithoutTTL_withSuccess() {
        // Arrange
        String cacheEntryKey = "name";
        String cacheEntryValue = "John";
        NewCacheEntryRequestDto requestDto = new NewCacheEntryRequestDto(cacheEntryKey, cacheEntryValue, null);
        Map<String, CacheEntry> cacheEntryMap = getCacheEntryMap();

        // Act
        controller.postEntry(requestDto);

        // Assert
        assertEquals(1, cacheEntryMap.size());

        CacheEntry nameCacheEntry = cacheEntryMap.get(cacheEntryKey);
        assertEquals(cacheEntryKey, nameCacheEntry.getKey());
        assertEquals(cacheEntryValue, nameCacheEntry.getValue());
        assertNull(nameCacheEntry.getTtl());
        assertNull(nameCacheEntry.getExpiresAt());
    }

    @Test
    void getNameWithoutTTL_withSuccess() {
        // Arrange
        String cacheEntryValue = "John";
        String cacheEntryKey = "name";
        NewCacheEntryRequestDto requestDto = new NewCacheEntryRequestDto(cacheEntryKey, cacheEntryValue, null);
        controller.postEntry(requestDto);

        // Act
        String response = controller.getEntryValue(cacheEntryKey);

        // Assert
        assertEquals(cacheEntryValue, response);
    }

    @Test
    void getNameWithoutTTL_withNoValueFound() {
        // Arrange
        String cacheEntryKey = "name";

        // Act
        String response = controller.getEntryValue(cacheEntryKey);

        // Assert
        assertEquals("", response);
    }


    @Test
    void addNameWithTTL_withSuccess() {
        // Arrange
        String cacheEntryKey = "name";
        String cacheEntryValue = "Larry";
        long cacheEntryTTL = 30L;
        NewCacheEntryRequestDto requestDto = new NewCacheEntryRequestDto(cacheEntryKey, cacheEntryValue, cacheEntryTTL);
        Map<String, CacheEntry> cacheEntryMap = getCacheEntryMap();
        LocalDateTime beforeCreationTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

        // Act
        controller.postEntry(requestDto);

        // Assert
        long maximumSecondsInMinute = ChronoField.SECOND_OF_MINUTE.range().getMaximum();
        LocalDateTime afterCreationTime = LocalDateTime.now().with(ChronoField.SECOND_OF_MINUTE, maximumSecondsInMinute);

        assertEquals(1, cacheEntryMap.size());

        CacheEntry nameCacheEntry = cacheEntryMap.get(cacheEntryKey);
        assertEquals(cacheEntryKey, nameCacheEntry.getKey());
        assertEquals(cacheEntryValue, nameCacheEntry.getValue());
        assertEquals(cacheEntryTTL, nameCacheEntry.getTtl());
        assertThat(nameCacheEntry.getExpiresAt()).isBetween(beforeCreationTime.plus(cacheEntryTTL, ChronoUnit.SECONDS), afterCreationTime.plus(cacheEntryTTL, ChronoUnit.SECONDS));
    }


    @Test
    void getNameWithTTL_withSuccess() {
        // Arrange
        String cacheEntryKey = "name";
        String cacheEntryValue = "Larry";
        long cacheEntryTTL = 30L;
        NewCacheEntryRequestDto requestDto = new NewCacheEntryRequestDto(cacheEntryKey, cacheEntryValue, cacheEntryTTL);

        controller.postEntry(requestDto);

        // Act
        String response = controller.getEntryValue(cacheEntryKey);

        // Assert
        assertEquals(cacheEntryValue, response);

        // Post test
        controller.deleteEntry(cacheEntryKey);
    }

    private Map<String, CacheEntry> getCacheEntryMap() {
        try {
            Field privateField = CacheRepository.class.getDeclaredField("cacheMap");
            privateField.setAccessible(true);
            return (Map<String, CacheEntry>) privateField.get(cacheRepository);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
