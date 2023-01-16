package com.tmg.codingchallenge.cachechallenge.service;

import com.tmg.codingchallenge.cachechallenge.repository.CacheRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CacheExpirationServiceImpl implements CacheExpirationService {

    private final CacheRepository repository;

    @Override
    public void clearExpiredCacheEntries() {
        repository.deleteByExpirationTime(LocalDateTime.now());
    }
}
