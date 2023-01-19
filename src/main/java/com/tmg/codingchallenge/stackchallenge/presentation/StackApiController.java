package com.tmg.codingchallenge.stackchallenge.presentation;

import com.tmg.codingchallenge.stackchallenge.service.StackService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stack")
@RequiredArgsConstructor
public class StackApiController implements StackApi {

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
