package com.tmg.codingchallenge.unittest.stackchallenge.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tmg.codingchallenge.global.dto.ArgumentErrorDto;
import com.tmg.codingchallenge.global.dto.ArgumentExceptionResponseDto;
import com.tmg.codingchallenge.cachechallenge.presentation.controller.StackController;
import com.tmg.codingchallenge.cachechallenge.presentation.controller.NewStackItemRequestDto;
import com.tmg.codingchallenge.cachechallenge.presentation.service.StackService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.UnsupportedEncodingException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(StackController.class)
class StackControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private StackService stackService;

    @Test
    void getTopItem_withSuccessResponse() throws Exception {
        // Arrange
        String expectedResponseItem = "Jan 10th";
        given(stackService.popItem())
                .willReturn(expectedResponseItem);

        // Act
        MvcResult mvcResult = mvc.perform(get("/stack")).andReturn();

        // Assert
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(200, response.getStatus());
        assertEquals(expectedResponseItem, response.getContentAsString());
        verify(stackService).popItem();
        verifyNoMoreInteractions(stackService);
    }

    @Test
    void getTopItemWithNoValueFound_withSuccessResponse() throws Exception {
        // Arrange
        String expectedResponseItem = "";
        given(stackService.popItem())
                .willReturn(expectedResponseItem);

        // Act
        MvcResult mvcResult = mvc.perform(get("/stack")).andReturn();

        // Assert
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(200, response.getStatus());
        assertEquals("", response.getContentAsString());
        verify(stackService).popItem();
        verifyNoMoreInteractions(stackService);
    }

    @Test
    void stackItem_withSuccessResponse() throws Exception {
        // Arrange
        String newItem = "Valid Item Name";
        NewStackItemRequestDto requestDto = new NewStackItemRequestDto(newItem);

        ObjectMapper objectMapper = new ObjectMapper();

        // Act
        MvcResult mvcResult = mvc.perform(post("/stack")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andReturn();

        // Assert
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(200, response.getStatus());
        assertEquals("", response.getContentAsString());
        verify(stackService).pushItem(newItem);
        verifyNoMoreInteractions(stackService);
    }

    @Test
    void stackItemWithNullItem_returningBadRequest() throws Exception {
        // Arrange
        NewStackItemRequestDto requestDto = new NewStackItemRequestDto(null);

        ObjectMapper objectMapper = new ObjectMapper();

        // Act
        MvcResult mvcResult = mvc.perform(post("/stack")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andReturn();

        // Assert
        validateResponseForStackItemWithBadRequest(mvcResult);
    }

    @Test
    void stackItemWithEmptyBody_returningBadRequest() throws Exception {
        // Arrange
        String newItem = "";
        NewStackItemRequestDto requestDto = new NewStackItemRequestDto(newItem);

        ObjectMapper objectMapper = new ObjectMapper();

        // Act
        MvcResult mvcResult = mvc.perform(post("/stack")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andReturn();

        // Assert
        validateResponseForStackItemWithBadRequest(mvcResult);
    }

    private void validateResponseForStackItemWithBadRequest(MvcResult mvcResult) throws UnsupportedEncodingException, JsonProcessingException {
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(400, response.getStatus());
        String responseContentAsString = response.getContentAsString();
        assertNotEquals("", responseContentAsString);

        ArgumentExceptionResponseDto responseDto = new ObjectMapper().readValue(responseContentAsString, ArgumentExceptionResponseDto.class);
        assertEquals("uri=/stack", responseDto.getDetail());

        List<ArgumentErrorDto> argumentsErrors = responseDto.getArgumentsErrors();
        assertEquals(1, argumentsErrors.size());

        ArgumentErrorDto argumentErrorDto = argumentsErrors.get(0);
        assertEquals("newItem", argumentErrorDto.getField());
        assertEquals("Item cannot be null or empty!", argumentErrorDto.getMessage());
        verifyNoInteractions(stackService);
    }

}
