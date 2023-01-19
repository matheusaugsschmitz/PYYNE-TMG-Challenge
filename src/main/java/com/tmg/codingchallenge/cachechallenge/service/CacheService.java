package com.tmg.codingchallenge.cachechallenge.service;

import com.tmg.codingchallenge.cachechallenge.presentation.NewEntryRequestDto;

public interface CacheService {


    void pushEntry(NewEntryRequestDto requestDto);

    String getValue(String key);

    void removeEntryByKey(String key);
}
