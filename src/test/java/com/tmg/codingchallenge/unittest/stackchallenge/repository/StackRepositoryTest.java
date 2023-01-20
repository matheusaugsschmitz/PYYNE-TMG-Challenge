package com.tmg.codingchallenge.unittest.stackchallenge.repository;

import com.tmg.codingchallenge.data.CustomStack;
import com.tmg.codingchallenge.data.StackRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class StackRepositoryTest {

    @InjectMocks
    private StackRepository stackRepository;

    @Test
    void pushItem_withSuccess() {
        // Arrange
        String stackItem = "Lada Niva";

        // Act
        stackRepository.pushItem(stackItem);

        // Assert
        CustomStack<String> stack = getStack();
        assertEquals(1, stack.getSize());
        assertEquals(stackItem, stack.pop());
    }

    @Test
    void popItem_withSuccess() {
        // Arrange
        String stackItem = "Lada Niva";
        CustomStack<String> stack = getStack();
        stack.pushItem(stackItem);

        // Act
        String response = stackRepository.popTopItem();

        // Assert
        assertEquals(0, stack.getSize());
        assertEquals(stackItem, response);
    }

    private CustomStack<String> getStack() {
        try {
            Field privateField = StackRepository.class.getDeclaredField("stack");
            privateField.setAccessible(true);
            return (CustomStack<String>) privateField.get(stackRepository);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
