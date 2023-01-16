package com.tmg.codingchallenge.unittest.cachechallenge.service;

import com.tmg.codingchallenge.cachechallenge.repository.CacheRepository;
import com.tmg.codingchallenge.cachechallenge.service.CacheExpirationServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CacheExpirationServiceImplTest {

    @InjectMocks
    private CacheExpirationServiceImpl service;

    @Mock
    private CacheRepository cacheRepository;

    @Test
    void clearExpiredCacheEntries_withSuccess() {
        // Arrange
        LocalDateTime mockedDate = LocalDateTime.now();

        try (MockedStatic<LocalDateTime> mockedStatic = mockStatic(LocalDateTime.class)) {
            mockedStatic.when(LocalDateTime::now).thenReturn(mockedDate);

            // Act
            service.clearExpiredCacheEntries();
        }

        // Assert
        verify(cacheRepository).deleteByExpirationTime(mockedDate);
    }
}
