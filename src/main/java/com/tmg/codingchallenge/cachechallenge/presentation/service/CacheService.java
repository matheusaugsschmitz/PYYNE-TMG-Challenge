package com.tmg.codingchallenge.cachechallenge.presentation.service;

import com.tmg.codingchallenge.cachechallenge.presentation.controller.NewCacheEntryRequestDto;

public interface CacheService {


    void pushEntry(NewCacheEntryRequestDto requestDto);

    String getValue(String key);

    void removeEntryByKey(String key);
}
