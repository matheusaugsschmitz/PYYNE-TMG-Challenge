package com.tmg.codingchallenge.presentation.service;

import com.tmg.codingchallenge.data.CacheRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class CacheExpirationServiceImpl implements CacheExpirationService {

    private final CacheRepository repository;

    @Override
    public void clearExpiredCacheEntries() {
        repository.deleteByExpirationTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
    }
}
