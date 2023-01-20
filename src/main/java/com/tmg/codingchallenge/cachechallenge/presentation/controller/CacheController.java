package com.tmg.codingchallenge.cachechallenge.presentation.controller;

import com.tmg.codingchallenge.cachechallenge.presentation.api.CacheApi;
import com.tmg.codingchallenge.cachechallenge.presentation.service.CacheService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/cache")
public class CacheController implements CacheApi {

    private final CacheService cacheService;

    @Override
    @PostMapping
    public void postEntry(@RequestBody @Valid NewCacheEntryRequestDto requestDto) {
        cacheService.pushEntry(requestDto);
    }

    @Override
    @GetMapping("/{key}/value")
    public String getEntryValue(@PathVariable @NotBlank(message = "Key cannot be null or empty!") String key) {
        return cacheService.getValue(key);
    }

    @Override
    @DeleteMapping("/{key}")
    public void deleteEntry(@PathVariable @NotBlank(message = "Key cannot be null or empty!") String key) {
        cacheService.removeEntryByKey(key);
    }

}
