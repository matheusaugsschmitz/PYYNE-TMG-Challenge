package com.tmg.codingchallenge.cachechallenge.service;

import com.tmg.codingchallenge.cachechallenge.dto.NewEntryRequestDto;

public interface CacheService {


    void pushEntry(NewEntryRequestDto requestDto);

    String getValue(String key);

    void removeEntryByKey(String key);
}
