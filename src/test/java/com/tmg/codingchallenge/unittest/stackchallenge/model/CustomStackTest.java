package com.tmg.codingchallenge.unittest.stackchallenge.model;

import com.tmg.codingchallenge.stackchallenge.model.CustomStack;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.*;

class CustomStackTest {

    @Test
    void pushItemInStringStack_withSuccess() {
        // Arrange
        String stackItem = "Lada Laika";
        CustomStack<String> stack = new CustomStack<>();

        // Act
        stack.pushItem(stackItem);

        // Assert
        assertEquals(1, stack.getSize());
        assertEquals(stackItem, stack.pop());
    }

    @Test
    void pushItemInLongStack_withSuccess() {
        // Arrange
        Long numericValue = 2601L;
        CustomStack<Long> stack = new CustomStack<>();

        // Act
        stack.pushItem(numericValue);

        // Assert
        assertEquals(1, stack.getSize());
        assertEquals(numericValue, stack.pop());
    }

    @Test
    void pushItemInStringStackWithNullValue_expectingIllegalArgumentException() {
        // Arrange
        CustomStack<String> stack = new CustomStack<>();

        // Act Definition
        Executable act = () -> stack.pushItem(null);

        // Assert
        String exceptionMessage = assertThrows(IllegalArgumentException.class, act).getMessage();

        assertEquals("Stack Item cannot be null!", exceptionMessage);
        assertEquals(0, stack.getSize());
        assertNull(stack.pop());
    }

    @Test
    void popWithSingleItemOnStack_withSuccess() {
        // Arrange
        CustomStack<String> stack = new CustomStack<>();
        String firstItem = "Silvia S14";
        stack.pushItem(firstItem);

        // Act
        String poppedItem = stack.pop();

        // Assert
        assertEquals(0, stack.getSize());
        assertEquals(firstItem, poppedItem);
    }

    @Test
    void popWithTwoItemsOnStack_withSuccess() {
        // Arrange
        CustomStack<String> stack = new CustomStack<>();
        String firstItem = "Silvia S14";
        stack.pushItem(firstItem);

        String secondItem = "Silvia S14";
        stack.pushItem(secondItem);

        // Act
        String poppedItem = stack.pop();

        // Assert
        assertEquals(1, stack.getSize());
        assertEquals(firstItem, poppedItem);
    }

    @Test
    void popWithNoItemsOnStack_withSuccess() {
        // Arrange
        CustomStack<String> stack = new CustomStack<>();

        // Act
        String poppedItem = stack.pop();

        // Assert
        assertEquals(0, stack.getSize());
        assertNull(poppedItem);
    }

}
