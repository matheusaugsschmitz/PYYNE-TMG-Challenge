package com.tmg.codingchallenge.endtoendtest.stackchallenge;

import com.tmg.codingchallenge.stackchallenge.api.StackApiController;
import com.tmg.codingchallenge.stackchallenge.dto.NewStackItemRequestDto;
import com.tmg.codingchallenge.stackchallenge.model.CustomStack;
import com.tmg.codingchallenge.stackchallenge.repository.StackRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * These tests were made to validate the final result with predefined inputs.
 * I have decided to do these test for the challenge purpose only, in real applications I would not take this approach since it would be difficult to maintain in complex or bigger softwares.
 */
@SpringBootTest
class StackApiControllerEndToEndTest {

    @Autowired
    private StackApiController controller;

    @Autowired
    private StackRepository stackRepository;


    @Test
    void getTopItem_withSuccess() {
        // Arrange
        String item = "4WD";
        CustomStack<String> stack = getStack();
        stack.pushItem(item);

        // Act
        String response = controller.getTopItem();

        // Assert
        assertEquals(item, response);
        assertEquals(0, stack.getSize());
    }

    @Test
    void getTopItemWithNoValueFound_withSuccess() {
        // Arrange
        CustomStack<String> stack = getStack();

        // Act
        String response = controller.getTopItem();

        // Assert
        assertEquals("", response);
        assertEquals(0, stack.getSize());
    }

    @Test
    void stackItem_withSuccess() {
        // Arrange
        String item = "AWD";
        NewStackItemRequestDto requestDto = new NewStackItemRequestDto(item);
        CustomStack<String> stack = getStack();

        // Act
        controller.stackItem(requestDto);

        // Assert
        assertEquals(1, stack.getSize());
        assertEquals(item, stack.pop());
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
