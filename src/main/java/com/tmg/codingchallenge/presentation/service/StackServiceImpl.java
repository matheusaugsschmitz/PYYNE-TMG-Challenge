package com.tmg.codingchallenge.presentation.service;

import com.tmg.codingchallenge.data.StackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static java.util.Objects.isNull;

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
        return coalesce(repository.popTopItem(), "");
    }

    private String coalesce(String... parameters) {
        for (String parameter : parameters) {
            if (!isNull(parameter))
                return parameter;
        }
        return null;
    }
}
