package com.tmg.codingchallenge.cachechallenge.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;


@Tag(name = "KeyValueStorage", description = "In-memory key-value storage challenge endpoints")
public interface CacheApi {

    @Operation(summary = "Register a new entry to the key-value in-memory storage system.", tags = "KeyValueStorage")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Invalid request body")
    })
    void postEntry(@Parameter(description = "Cache entry data.", required = true) @RequestBody @Valid NewEntryRequestDto requestDto);

    @Operation(summary = "Retrieve the value of a specific entry in the key-value in-memory storage system querying but it's key. Returns an empty String if the key doesn't exists.", tags = "KeyValueStorage")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Invalid path variable value")
    })
    String getEntryValue(@Parameter(description = "Key used for consulting value in store.", required = true) @PathVariable @NotBlank(message = "Key cannot be null or empty!") String key);

    @Operation(summary = "Remove a specific entry in the key-value in-memory storage system querying but it's key.", tags = "KeyValueStorage")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Invalid path variable value")
    })
    void deleteEntry(@Parameter(description = "Key used for consulting entry in store to be removed.", required = true) @PathVariable @NotBlank(message = "Key cannot be null or empty!") String key);
}
