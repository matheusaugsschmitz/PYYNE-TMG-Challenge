package com.tmg.codingchallenge.unittest.cachechallenge.service;

import com.tmg.codingchallenge.cachechallenge.dto.NewEntryRequestDto;
import com.tmg.codingchallenge.cachechallenge.model.CacheEntry;
import com.tmg.codingchallenge.cachechallenge.repository.CacheRepository;
import com.tmg.codingchallenge.cachechallenge.service.CacheServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CacheServiceImplTest {

    @InjectMocks
    private CacheServiceImpl service;

    @Mock
    private CacheRepository cacheRepository;

    @Captor
    private ArgumentCaptor<CacheEntry> entryArgumentCaptor;

    @Test
    void pushEntryToNewKeyWithoutTTL_withSuccess() {
        // Arrange
        String entryKey = "name";
        String entryValue = "Josias";

        NewEntryRequestDto requestDto = new NewEntryRequestDto(entryKey, entryValue, null);

        // Act
        service.pushEntry(requestDto);

        // Assert
        verify(cacheRepository).deleteByKey(entryKey);
        verify(cacheRepository).save(entryArgumentCaptor.capture());

        CacheEntry savedCacheEntry = entryArgumentCaptor.getValue();
        assertEquals(entryKey, savedCacheEntry.getKey());
        assertEquals(entryValue, savedCacheEntry.getValue());
        assertNull(savedCacheEntry.getTtl());
        assertNull(savedCacheEntry.getExpiresAt());

        verifyNoMoreInteractions(cacheRepository);
    }

    @Test
    void pushEntryToNewKeyWithTTL_withSuccess() {
        // Arrange
        String entryKey = "name";
        String entryValue = "Jonas";
        Long entryTTL = 50L;
        NewEntryRequestDto requestDto = new NewEntryRequestDto(entryKey, entryValue, entryTTL);

        LocalDateTime mockedTime = LocalDateTime.now();
        LocalDateTime expectedExpirationTime = mockedTime.truncatedTo(ChronoUnit.SECONDS).plus(entryTTL, ChronoUnit.SECONDS);

        try (MockedStatic<LocalDateTime> mockedStatic = mockStatic(LocalDateTime.class)) {
            mockedStatic.when(LocalDateTime::now).thenReturn(mockedTime);

            // Act
            service.pushEntry(requestDto);
        }

        // Assert
        verify(cacheRepository).deleteByKey(entryKey);
        verify(cacheRepository).save(entryArgumentCaptor.capture());

        CacheEntry savedCacheEntry = entryArgumentCaptor.getValue();
        assertEquals(entryKey, savedCacheEntry.getKey());
        assertEquals(entryValue, savedCacheEntry.getValue());
        assertEquals(entryTTL, savedCacheEntry.getTtl());
        assertEquals(expectedExpirationTime, savedCacheEntry.getExpiresAt());

        verifyNoMoreInteractions(cacheRepository);
    }

    @Test
    void removeEntryByKey_withSuccess() {
        // Arrange
        String entryKey = "name";

        // Act
        service.removeEntryByKey(entryKey);

        // Assert
        verify(cacheRepository).deleteByKey(entryKey);
        verifyNoMoreInteractions(cacheRepository);
    }

    @Test
    void getValueFromExistentEntry_withSuccess() {
        // Arrange
        String entryKey = "name";
        String entryValue = "Ronald";

        given(cacheRepository.getOptionalValueByKey(entryKey))
                .willReturn(Optional.of(entryValue));

        // Act
        String response = service.getValue(entryKey);
        assertEquals(entryValue, response);
    }

    @Test
    void getValueFromNonExistentEntry_withSuccess() {
        // Arrange
        String entryKey = "name";

        given(cacheRepository.getOptionalValueByKey(entryKey))
                .willReturn(Optional.empty());

        // Act
        String response = service.getValue(entryKey);
        assertEquals("", response);
    }

}
