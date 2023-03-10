package com.tmg.codingchallenge.presentation.service;

import com.tmg.codingchallenge.presentation.controller.NewCacheEntryRequestDto;
import com.tmg.codingchallenge.data.CacheEntry;
import com.tmg.codingchallenge.data.CacheRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CacheServiceImpl implements CacheService {

    private final CacheRepository repository;

    @Override
    public void pushEntry(NewCacheEntryRequestDto requestDto) {
        final String cacheKey = requestDto.getKey();
        removeEntryByKey(cacheKey);
        final CacheEntry newCacheEntry = new CacheEntry(cacheKey, requestDto.getValue(), requestDto.getTtl());
        repository.save(newCacheEntry);
    }

    @Override
    public String getValue(String key) {
        return repository.getOptionalValueByKey(key)
                .orElse(Strings.EMPTY);
    }

    @Override
    public void removeEntryByKey(String key) {
        repository.deleteByKey(key);
    }

}
