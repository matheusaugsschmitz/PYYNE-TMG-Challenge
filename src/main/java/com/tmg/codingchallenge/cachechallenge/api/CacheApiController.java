package com.tmg.codingchallenge.cachechallenge.api;

import com.tmg.codingchallenge.cachechallenge.dto.NewEntryRequestDto;
import com.tmg.codingchallenge.cachechallenge.service.CacheService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/cache")
public class CacheApiController implements CacheApi {

    private final CacheService cacheService;

    @Override
    @PostMapping
    public void postEntry(@RequestBody @Valid NewEntryRequestDto requestDto) {
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
