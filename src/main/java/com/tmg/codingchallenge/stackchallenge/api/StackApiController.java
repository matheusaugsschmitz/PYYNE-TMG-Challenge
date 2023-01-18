package com.tmg.codingchallenge.stackchallenge.api;

import com.tmg.codingchallenge.stackchallenge.service.StackService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stack")
@RequiredArgsConstructor
public class StackApiController implements StackApi {

    private final StackService stackService;

    @Override
    @PostMapping(consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public void stackItem(@RequestBody @NotBlank(message = "Item cannot be null or empty!")  String newItem) {
        stackService.pushItem(newItem);
    }

    @Override
    @GetMapping(produces = MediaType.TEXT_PLAIN_VALUE)
    public String getTopItem() {
        return stackService.popItem();
    }
}
