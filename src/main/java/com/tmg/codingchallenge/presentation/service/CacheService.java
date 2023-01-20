package com.tmg.codingchallenge.presentation.service;

import com.tmg.codingchallenge.presentation.controller.NewCacheEntryRequestDto;

public interface CacheService {


    void pushEntry(NewCacheEntryRequestDto requestDto);

    String getValue(String key);

    void removeEntryByKey(String key);
}
