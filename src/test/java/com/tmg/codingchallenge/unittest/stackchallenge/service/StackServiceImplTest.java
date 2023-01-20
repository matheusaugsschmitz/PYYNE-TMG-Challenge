package com.tmg.codingchallenge.unittest.stackchallenge.service;

import com.tmg.codingchallenge.data.StackRepository;
import com.tmg.codingchallenge.presentation.service.StackServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class StackServiceImplTest {

    @InjectMocks
    private StackServiceImpl stackService;

    @Mock
    private StackRepository stackRepository;

    @Test
    void pushItem_withSuccess() {
        // Arrange
        String stackItem = "R34";

        // Act
        stackService.pushItem(stackItem);

        // Assert
        verify(stackRepository).pushItem(stackItem);
        verifyNoMoreInteractions(stackRepository);
    }

    @Test
    void pushItemWithNullValue_withSuccess() {
        // Arrange

        // Act
        stackService.pushItem(null);

        // Assert
        verify(stackRepository).pushItem(null);
        verifyNoMoreInteractions(stackRepository);
    }

    @Test
    void popItemFromStackWithItems_withSuccess() {
        // Arrange
        String stackItem = "E56 M3";
        given(stackRepository.popTopItem())
                .willReturn(stackItem);

        // Act
        String response = stackService.popItem();

        // Assert
        assertEquals(stackItem, response);

        verify(stackRepository).popTopItem();
        verifyNoMoreInteractions(stackRepository);
    }

    @Test
    void popItemFromStackWithNoItems_withSuccess() {
        // Arrange
        given(stackRepository.popTopItem())
                .willReturn(null);

        // Act
        String response = stackService.popItem();

        // Assert
        assertEquals("", response);

        verify(stackRepository).popTopItem();
        verifyNoMoreInteractions(stackRepository);
    }
}
