package com.tmg.codingchallenge.stackchallenge.repository;

import com.tmg.codingchallenge.stackchallenge.model.CustomStack;
import org.springframework.stereotype.Component;

@Component
public class StackRepository {

    private final CustomStack<String> stack = new CustomStack<>();

    public String popTopItem() {
        return stack.pop();
    }

    public void pushItem(String newItem) {
        stack.pushItem(newItem);
    }
}
