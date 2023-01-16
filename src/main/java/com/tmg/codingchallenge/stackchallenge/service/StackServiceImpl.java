package com.tmg.codingchallenge.stackchallenge.service;

import com.tmg.codingchallenge.stackchallenge.repository.StackRepository;
import com.tmg.codingchallenge.stackchallenge.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StackServiceImpl implements StackService {

    private final StackRepository repository;

    @Override
    public void pushItem(String newItem) {
        repository.pushItem(newItem);
    }

    @Override
    public String popItem() {
        return StringUtils.coalesce(repository.popTopItem(), "");
    }
}
