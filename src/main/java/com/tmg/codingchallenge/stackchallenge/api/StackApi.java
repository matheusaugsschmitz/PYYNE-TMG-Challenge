package com.tmg.codingchallenge.stackchallenge.api;

import com.tmg.codingchallenge.stackchallenge.dto.NewStackItemRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Stack", description = "In-memory stack challenge endpoints")
public interface StackApi {

    @Operation(summary = "Push an item to the top of the stack", tags = "Stack")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Invalid request body")
    })
    void stackItem(@Parameter(description = "New item to be added to the stack. Empty values are not allowed for this parameter!", required = true) @RequestBody @Valid NewStackItemRequestDto newItem);

    @Operation(summary = "Remove and retrieve the top item of the stack. If there is no item in the stack, the response will be an empty string", tags = "Stack")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE))
    })
    String getTopItem();
}
