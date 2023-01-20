package com.tmg.codingchallenge.cachechallenge.presentation.controller;

import com.tmg.codingchallenge.cachechallenge.presentation.api.StackApi;
import com.tmg.codingchallenge.cachechallenge.presentation.service.StackService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stack")
@RequiredArgsConstructor
public class StackController implements StackApi {

    private final StackService stackService;

    @Override
    @PostMapping
    public void stackItem(@RequestBody @Valid NewStackItemRequestDto newItem) {
        stackService.pushItem(newItem.getNewItem());
    }

    @Override
    @GetMapping(produces = MediaType.TEXT_PLAIN_VALUE)
    public String getTopItem() {
        return stackService.popItem();
    }
}
