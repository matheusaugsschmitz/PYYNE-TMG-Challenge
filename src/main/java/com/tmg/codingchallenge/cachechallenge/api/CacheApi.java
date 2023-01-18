package com.tmg.codingchallenge.cachechallenge.api;

import com.tmg.codingchallenge.cachechallenge.dto.NewEntryRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;


@Tag(name = "KeyValueStorage", description = "In-memory key-value storage challenge endpoints")
public interface CacheApi {

    @Operation(summary = "Register a new entry to the key-value in-memory storage system.", tags = "KeyValueStorage")
    void postEntry(@RequestBody @Valid NewEntryRequestDto requestDto);

    @Operation(summary = "Retrieve the value of a specific entry in the key-value in-memory storage system querying but it's key.", tags = "KeyValueStorage")
    String getEntryValue(@PathVariable @NotBlank(message = "Key cannot be null or empty!") String key);

    @Operation(summary = "Remove a specific entry in the key-value in-memory storage system querying but it's key.", tags = "KeyValueStorage")
    void deleteEntry(@PathVariable @NotBlank(message = "Key cannot be null or empty!") String key);
}
