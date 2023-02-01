package com.tmg.codingchallenge.unittest.cachechallenge.repository;

import com.tmg.codingchallenge.data.CacheEntry;
import com.tmg.codingchallenge.data.CacheRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;

@ExtendWith({MockitoExtension.class})
class CacheRepositoryTest {

    @InjectMocks
    private CacheRepository repository;

    @AfterEach
    void clearAllStoredEntries() {
        Map<String, CacheEntry> cacheEntryMap = getCacheEntryMap();
        cacheEntryMap.clear();
    }

    @Test
    void saveNameWithoutTTL_withSuccess() {
        // Arrange
        String cacheEntryKey = "name";
        String cacheEntryValue = "Brandon";
        CacheEntry cacheEntry = new CacheEntry(cacheEntryKey, cacheEntryValue, null);
        Map<String, CacheEntry> cacheEntryMap = getCacheEntryMap();

        // Act
        repository.save(cacheEntry);

        // Assert
        assertEquals(1, cacheEntryMap.size());

        CacheEntry nameCacheEntry = cacheEntryMap.get(cacheEntryKey);
        assertEquals(cacheEntryKey, nameCacheEntry.getKey());
        assertEquals(cacheEntryValue, nameCacheEntry.getValue());
        assertNull(nameCacheEntry.getTtl());
        assertNull(nameCacheEntry.getExpiresAt());
    }

    @Test
    void getOptionalValueByKeyWithoutTTL_withSuccess() {
        // Arrange
        String cacheEntryValue = "Victor";
        String cacheEntryKey = "name";
        CacheEntry cacheEntry = new CacheEntry(cacheEntryKey, cacheEntryValue, null);
        Map<String, CacheEntry> cacheEntryMap = getCacheEntryMap();
        cacheEntryMap.put(cacheEntryKey, cacheEntry);

        // Act
        Optional<String> response = repository.getOptionalValueByKey(cacheEntryKey);

        // Assert
        assertTrue(response.isPresent());
        assertEquals(cacheEntryValue, response.get());
    }


    @Test
    void getOptionalValueByKeyWithTTL_withSuccess() {
        // Arrange
        String cacheEntryValue = "Garry";
        String cacheEntryKey = "name";
        CacheEntry cacheEntry = new CacheEntry(cacheEntryKey, cacheEntryValue, 150L);
        Map<String, CacheEntry> cacheEntryMap = getCacheEntryMap();
        cacheEntryMap.put(cacheEntryKey, cacheEntry);

        // Act
        Optional<String> response = repository.getOptionalValueByKey(cacheEntryKey);

        // Assert
        assertTrue(response.isPresent());
        assertEquals(cacheEntryValue, response.get());
    }

    @Test
    void getOptionalValueByKeyWithoutTTL_withNoValueFound() {
        // Arrange
        String cacheEntryKey = "name";

        // Act
        Optional<String> response = repository.getOptionalValueByKey(cacheEntryKey);

        // Assert
        assertTrue(response.isEmpty());
    }


    @Test
    void getOptionalValueByKeyWithTTLExpired_withNoEmptyReturn() {
        // Arrange
        String cacheEntryValue = "Garry";
        String cacheEntryKey = "name";
        LocalDateTime localDateTimeForExpiredEntry = getLocalDateTimeForExpiredEntry();
        CacheEntry cacheEntry;
        try (MockedStatic<LocalDateTime> mock = mockStatic(LocalDateTime.class)) {
            mock.when(LocalDateTime::now).thenReturn(localDateTimeForExpiredEntry);

            cacheEntry = new CacheEntry(cacheEntryKey, cacheEntryValue, 150L);
        }

        Map<String, CacheEntry> cacheEntryMap = getCacheEntryMap();
        cacheEntryMap.put(cacheEntryKey, cacheEntry);

        // Act
        Optional<String> response = repository.getOptionalValueByKey(cacheEntryKey);

        // Assert
        assertTrue(response.isEmpty());
    }


    @Test
    void saveNameWithTTL_withSuccess() {
        // Arrange
        String cacheEntryKey = "name";
        String cacheEntryValue = "Larry";
        long cacheEntryTTL = 30L;
        CacheEntry cacheEntry = new CacheEntry(cacheEntryKey, cacheEntryValue, cacheEntryTTL);
        Map<String, CacheEntry> cacheEntryMap = getCacheEntryMap();

        // Act
        repository.save(cacheEntry);

        // Assert
        assertEquals(1, cacheEntryMap.size());

        CacheEntry nameCacheEntry = cacheEntryMap.get(cacheEntryKey);
        assertEquals(cacheEntry, nameCacheEntry);
    }

    @Test
    void deleteByKeyWithJustOneKeyAndNoTTL_withSuccess() {
        // Arrange
        String cacheEntryKey = "name";
        String cacheEntryValue = "Jonathan";
        CacheEntry cacheEntry = new CacheEntry(cacheEntryKey, cacheEntryValue, null);

        Map<String, CacheEntry> cacheEntryMap = getCacheEntryMap();
        cacheEntryMap.put(cacheEntryKey, cacheEntry);

        // Act
        repository.deleteByKey(cacheEntryKey);

        // Assert
        assertTrue(cacheEntryMap.isEmpty());
    }

    @Test
    void deleteByKeyWithJustOneKeyAndTTL_withSuccess() {
        // Arrange
        String cacheEntryKey = "name";
        String cacheEntryValue = "Carl";
        long cacheEntryTTL = 30L;
        CacheEntry cacheEntry = new CacheEntry(cacheEntryKey, cacheEntryValue, cacheEntryTTL);

        Map<String, CacheEntry> cacheEntryMap = getCacheEntryMap();
        cacheEntryMap.put(cacheEntryKey, cacheEntry);

        // Act
        repository.deleteByKey(cacheEntryKey);

        // Assert
        assertTrue(cacheEntryMap.isEmpty());
    }

    @Test
    void deleteByKeyWithMultipleKeysAndNoTTL_withSuccess() {
        // Arrange
        Map<String, CacheEntry> cacheEntryMap = getCacheEntryMap();

        String firstCacheEntryKey = "name";
        String firstCacheEntryValue = "Jonathan";
        CacheEntry firstCacheEntry = new CacheEntry(firstCacheEntryKey, firstCacheEntryValue, null);
        cacheEntryMap.put(firstCacheEntryKey, firstCacheEntry);

        String secondCacheEntryKey = "age";
        String secondCacheEntryValue = "23";
        CacheEntry secondCacheEntry = new CacheEntry(secondCacheEntryKey, secondCacheEntryValue, null);
        cacheEntryMap.put(secondCacheEntryKey, secondCacheEntry);

        // Act
        repository.deleteByKey(firstCacheEntryKey);

        // Assert
        assertEquals(1, cacheEntryMap.size());
        assertEquals(secondCacheEntry, cacheEntryMap.get(secondCacheEntryKey));
    }

    @Test
    void deleteByKeyWithMultipleKeysAndTTLInTheOneToBeDeleted_withSuccess() {
        // Arrange
        Map<String, CacheEntry> cacheEntryMap = getCacheEntryMap();

        String firstCacheEntryKey = "name";
        String firstCacheEntryValue = "Jonathan";
        CacheEntry firstCacheEntry = new CacheEntry(firstCacheEntryKey, firstCacheEntryValue, 30L);
        cacheEntryMap.put(firstCacheEntryKey, firstCacheEntry);

        String secondCacheEntryKey = "age";
        String secondCacheEntryValue = "23";
        CacheEntry secondCacheEntry = new CacheEntry(secondCacheEntryKey, secondCacheEntryValue, null);
        cacheEntryMap.put(secondCacheEntryKey, secondCacheEntry);

        // Act
        repository.deleteByKey(firstCacheEntryKey);

        // Assert
        assertEquals(1, cacheEntryMap.size());
        assertEquals(secondCacheEntry, cacheEntryMap.get(secondCacheEntryKey));
    }

    @Test
    void deleteByKeyWithMultipleKeysAndTTLInTheOneToWillNotBeRemoved_withSuccess() {
        // Arrange
        Map<String, CacheEntry> cacheEntryMap = getCacheEntryMap();

        String firstCacheEntryKey = "name";
        String firstCacheEntryValue = "Jonathan";
        CacheEntry firstCacheEntry = new CacheEntry(firstCacheEntryKey, firstCacheEntryValue, 30L);
        cacheEntryMap.put(firstCacheEntryKey, firstCacheEntry);

        String secondCacheEntryKey = "age";
        String secondCacheEntryValue = "23";
        CacheEntry secondCacheEntry = new CacheEntry(secondCacheEntryKey, secondCacheEntryValue, null);
        cacheEntryMap.put(secondCacheEntryKey, secondCacheEntry);

        // Act
        repository.deleteByKey(secondCacheEntryKey);

        // Assert
        assertEquals(1, cacheEntryMap.size());
        assertEquals(firstCacheEntry, cacheEntryMap.get(firstCacheEntryKey));
    }

    @Test
    void deleteByKeyWithMultipleKeysAndTTLInMoreThanOne_withSuccess() {
        // Arrange
        Map<String, CacheEntry> cacheEntryMap = getCacheEntryMap();

        String firstCacheEntryKey = "name";
        String firstCacheEntryValue = "Jonathan";
        CacheEntry firstCacheEntry = new CacheEntry(firstCacheEntryKey, firstCacheEntryValue, 30L);
        cacheEntryMap.put(firstCacheEntryKey, firstCacheEntry);

        String secondCacheEntryKey = "age";
        String secondCacheEntryValue = "23";
        CacheEntry secondCacheEntry = new CacheEntry(secondCacheEntryKey, secondCacheEntryValue, 20L);
        cacheEntryMap.put(secondCacheEntryKey, secondCacheEntry);

        // Act
        repository.deleteByKey(secondCacheEntryKey);

        // Assert
        assertEquals(1, cacheEntryMap.size());
        assertEquals(firstCacheEntry, cacheEntryMap.get(firstCacheEntryKey));
    }

    @Test
    void deleteByExpirationTimeWithJustOneKeyAndNoTTL_withSuccess() {
        // Arrange
        String cacheEntryKey = "name";
        String cacheEntryValue = "Jonathan";
        CacheEntry cacheEntry = new CacheEntry(cacheEntryKey, cacheEntryValue, null);

        Map<String, CacheEntry> cacheEntryMap = getCacheEntryMap();
        cacheEntryMap.put(cacheEntryKey, cacheEntry);

        // Act
        repository.deleteByExpirationTime(LocalDateTime.now());

        // Assert
        assertEquals(1, cacheEntryMap.size());
        assertEquals(cacheEntry, cacheEntryMap.get(cacheEntryKey));
    }

    @Test
    void deleteByExpirationTimeWithJustOneKeyAndValidTTL_withSuccess() {
        // Arrange
        String cacheEntryKey = "name";
        String cacheEntryValue = "Carl";
        long cacheEntryTTL = 500L;
        CacheEntry cacheEntry = new CacheEntry(cacheEntryKey, cacheEntryValue, cacheEntryTTL);

        Map<String, CacheEntry> cacheEntryMap = getCacheEntryMap();
        cacheEntryMap.put(cacheEntryKey, cacheEntry);

        // Act
        repository.deleteByExpirationTime(LocalDateTime.now());

        // Assert
        assertEquals(1, cacheEntryMap.size());
        assertEquals(cacheEntry, cacheEntryMap.get(cacheEntryKey));
    }

    @Test
    void deleteByExpirationTimeWithMultipleEntriesButNoneOfThemWithTTL_withSuccess() {
        // Arrange
        Map<String, CacheEntry> cacheEntryMap = getCacheEntryMap();

        String firstCacheEntryKey = "name";
        String firstCacheEntryValue = "Jonathan";
        CacheEntry firstCacheEntry = new CacheEntry(firstCacheEntryKey, firstCacheEntryValue, null);
        cacheEntryMap.put(firstCacheEntryKey, firstCacheEntry);

        String secondCacheEntryKey = "age";
        String secondCacheEntryValue = "23";
        CacheEntry secondCacheEntry = new CacheEntry(secondCacheEntryKey, secondCacheEntryValue, null);
        cacheEntryMap.put(secondCacheEntryKey, secondCacheEntry);

        // Act
        repository.deleteByExpirationTime(LocalDateTime.now());

        // Assert
        assertEquals(2, cacheEntryMap.size());
        assertEquals(firstCacheEntry, cacheEntryMap.get(firstCacheEntryKey));
        assertEquals(secondCacheEntry, cacheEntryMap.get(secondCacheEntryKey));
    }

    @Test
    void deleteByExpirationTimeWithMultipleEntriesButNoneOfThemWithExpiredTTL_withSuccess() {
        // Arrange
        Map<String, CacheEntry> cacheEntryMap = getCacheEntryMap();

        String firstCacheEntryKey = "name";
        String firstCacheEntryValue = "Jonathan";
        CacheEntry firstCacheEntry = new CacheEntry(firstCacheEntryKey, firstCacheEntryValue, 500L);
        cacheEntryMap.put(firstCacheEntryKey, firstCacheEntry);

        String secondCacheEntryKey = "age";
        String secondCacheEntryValue = "23";
        CacheEntry secondCacheEntry = new CacheEntry(secondCacheEntryKey, secondCacheEntryValue, 300L);
        cacheEntryMap.put(secondCacheEntryKey, secondCacheEntry);

        // Act
        repository.deleteByExpirationTime(LocalDateTime.now());

        // Assert
        assertEquals(2, cacheEntryMap.size());
        assertEquals(firstCacheEntry, cacheEntryMap.get(firstCacheEntryKey));
        assertEquals(secondCacheEntry, cacheEntryMap.get(secondCacheEntryKey));
    }

    @Test
    void deleteByExpirationTimeWithMultipleKeysAndTTLExpiredInOne_withSuccess() {
        // Arrange
        Map<String, CacheEntry> cacheEntryMap = getCacheEntryMap();

        String firstCacheEntryKey = "name";
        String firstCacheEntryValue = "Jonathan";
        CacheEntry firstCacheEntry;
        LocalDateTime localDateTimeForExpiredEntry = getLocalDateTimeForExpiredEntry();
        try (MockedStatic<LocalDateTime> mock = mockStatic(LocalDateTime.class)) {
            mock.when(LocalDateTime::now).thenReturn(localDateTimeForExpiredEntry);

            firstCacheEntry = new CacheEntry(firstCacheEntryKey, firstCacheEntryValue, 50L);
        }
        cacheEntryMap.put(firstCacheEntryKey, firstCacheEntry);

        String secondCacheEntryKey = "age";
        String secondCacheEntryValue = "23";
        CacheEntry secondCacheEntry = new CacheEntry(secondCacheEntryKey, secondCacheEntryValue, 300L);
        cacheEntryMap.put(secondCacheEntryKey, secondCacheEntry);

        // Act
        repository.deleteByExpirationTime(LocalDateTime.now());

        // Assert
        assertEquals(1, cacheEntryMap.size());
        assertEquals(secondCacheEntry, cacheEntryMap.get(secondCacheEntryKey));
    }

    @Test
    void deleteByExpirationTimeWithMultipleKeysAndTTLExpiredInOneButExpiringAnotherTime_withSuccess() {
        // Arrange
        Map<String, CacheEntry> cacheEntryMap = getCacheEntryMap();

        String firstCacheEntryKey = "name";
        String firstCacheEntryValue = "Jonathan";
        CacheEntry firstCacheEntry;
        LocalDateTime localDateTimeForExpiredEntry = getLocalDateTimeForExpiredEntry();
        try (MockedStatic<LocalDateTime> mock = mockStatic(LocalDateTime.class)) {
            mock.when(LocalDateTime::now).thenReturn(localDateTimeForExpiredEntry);

            firstCacheEntry = new CacheEntry(firstCacheEntryKey, firstCacheEntryValue, 50L);
        }
        cacheEntryMap.put(firstCacheEntryKey, firstCacheEntry);

        String secondCacheEntryKey = "age";
        String secondCacheEntryValue = "23";
        CacheEntry secondCacheEntry = new CacheEntry(secondCacheEntryKey, secondCacheEntryValue, 300L);
        cacheEntryMap.put(secondCacheEntryKey, secondCacheEntry);

        // Act
        repository.deleteByExpirationTime(LocalDateTime.now());

        // Assert
        assertEquals(1, cacheEntryMap.size());
        assertEquals(secondCacheEntry, cacheEntryMap.get(secondCacheEntryKey));
    }

    @Test
    void deleteByExpirationTimeWithMultipleEntriesAndAllOfThemExpired_withSuccess() {
        // Arrange
        Map<String, CacheEntry> cacheEntryMap = getCacheEntryMap();

        String firstCacheEntryKey = "name";
        String firstCacheEntryValue = "Jonathan";

        String secondCacheEntryKey = "age";
        String secondCacheEntryValue = "23";

        CacheEntry firstCacheEntry, secondCacheEntry;

        LocalDateTime localDateTimeForExpiredEntry = getLocalDateTimeForExpiredEntry();
        try (MockedStatic<LocalDateTime> mock = mockStatic(LocalDateTime.class)) {
            mock.when(LocalDateTime::now).thenReturn(localDateTimeForExpiredEntry);

            firstCacheEntry = new CacheEntry(firstCacheEntryKey, firstCacheEntryValue, 50L);
            secondCacheEntry = new CacheEntry(secondCacheEntryKey, secondCacheEntryValue, 90L);
        }
        cacheEntryMap.put(firstCacheEntryKey, firstCacheEntry);
        cacheEntryMap.put(secondCacheEntryKey, secondCacheEntry);

        // Act
        repository.deleteByExpirationTime(LocalDateTime.now());

        // Assert
        assertTrue(cacheEntryMap.isEmpty());
    }

    private LocalDateTime getLocalDateTimeForExpiredEntry() {
        return LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).minus(10, ChronoUnit.MINUTES);
    }

    private Map<String, CacheEntry> getCacheEntryMap() {
        try {
            Field privateField = CacheRepository.class.getDeclaredField("cacheMap");
            privateField.setAccessible(true);
            return (Map<String, CacheEntry>) privateField.get(repository);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
