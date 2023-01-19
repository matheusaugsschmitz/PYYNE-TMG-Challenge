package com.tmg.codingchallenge.unittest.cachechallenge.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tmg.codingchallenge.cachechallenge.api.CacheApiController;
import com.tmg.codingchallenge.cachechallenge.dto.NewEntryRequestDto;
import com.tmg.codingchallenge.cachechallenge.service.CacheService;
import com.tmg.codingchallenge.global.dto.ArgumentErrorDto;
import com.tmg.codingchallenge.global.dto.ArgumentExceptionResponseDto;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(CacheApiController.class)
class CacheApiControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CacheService cacheService;

    @Captor
    private ArgumentCaptor<NewEntryRequestDto> newEntryRequestDtoArgumentCaptor;

    @Test
    void postEntryWithoutTTL_withSuccessResponse() throws Exception {
        testPostCacheWithSuccessResponse("name", "John", null);
    }

    @Test
    void postEntryWithValidTTL_withSuccessResponse() throws Exception {
        testPostCacheWithSuccessResponse("name", "John", 1L);
    }

    @Test
    void postEntryWithNegativeTTL_returningBadRequest() throws Exception {
        testPostCacheWithInvalidArgument("name", "John", -150L, "ttl", "When informed, TTL value must be a greater than ZERO!");
    }

    @Test
    void postEntryWithTTLZeroed_returningBadRequest() throws Exception {
        testPostCacheWithInvalidArgument("name", "John", 0L, "ttl", "When informed, TTL value must be a greater than ZERO!");
    }

    @Test
    void postEntryWithEmptyKey_returningBadRequest() throws Exception {
        testPostCacheWithInvalidArgument("", "John", null, "key", "Entry key cannot be null or empty!");
    }

    @Test
    void postEntryWithNullKey_returningBadRequest() throws Exception {
        testPostCacheWithInvalidArgument(null, "John", null, "key", "Entry key cannot be null or empty!");
    }

    @Test
    void postEntryWithNullValue_returningBadRequest() throws Exception {
        testPostCacheWithInvalidArgument("name", null, null, "value", "Entry value cannot be null!");
    }

    @Test
    void getEntry_withSuccessResponse() throws Exception {
        // Arrange
        String entryKey = "name";

        String expectedEntryValue = "John";
        given(cacheService.getValue(entryKey))
                .willReturn(expectedEntryValue);

        // Act
        MvcResult mvcResult = mvc.perform(get("/cache/" + entryKey + "/value")).andReturn();

        // Assert
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(200, response.getStatus());
        assertEquals(expectedEntryValue, response.getContentAsString());
        verify(cacheService).getValue(entryKey);
        verifyNoMoreInteractions(cacheService);
    }

    @Test
    void getEntryWithNoValueFound_withSuccessResponse() throws Exception {
        // Arrange
        String entryKey = "name";

        // Act
        MvcResult mvcResult = mvc.perform(get("/cache/" + entryKey + "/value")).andReturn();

        // Assert
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(200, response.getStatus());
        assertEquals("", response.getContentAsString());
        verify(cacheService).getValue(entryKey);
        verifyNoMoreInteractions(cacheService);
    }

    @Test
    void getEntryWithEmptyKey_returningBadRequest() throws Exception {
        // Arrange
        ObjectMapper objectMapper = new ObjectMapper();
        String urlPath = "/cache/ /value";

        // Act
        MvcResult mvcResult = mvc.perform(get(urlPath)).andReturn();

        // Assert
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(400, response.getStatus());
        String responseContentAsString = response.getContentAsString();
        assertNotEquals("", responseContentAsString);

        ArgumentExceptionResponseDto responseDto = objectMapper.readValue(responseContentAsString, ArgumentExceptionResponseDto.class);
        assertEquals("uri=" + urlPath, URLDecoder.decode(responseDto.getDetail(), Charset.defaultCharset()));

        List<ArgumentErrorDto> argumentsErrors = responseDto.getArgumentsErrors();
        assertEquals(1, argumentsErrors.size());

        ArgumentErrorDto argumentErrorDto = argumentsErrors.get(0);
        assertEquals("key", argumentErrorDto.getField());
        assertEquals("Key cannot be null or empty!", argumentErrorDto.getMessage());

        verifyNoInteractions(cacheService);
    }

    @Test
    void deleteEntryForNonExistentKey_withSuccess() throws Exception {
        // Arrange
        String entryKey = "delete_test_key";

        // Act
        MvcResult mvcResult = mvc.perform(delete("/cache/" + entryKey)).andReturn();

        // Assert
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(200, response.getStatus());
        assertEquals("", response.getContentAsString());
        verify(cacheService).removeEntryByKey(entryKey);
        verifyNoMoreInteractions(cacheService);
    }

    @Test
    void deleteEntryForExistentKey_withSuccess() throws Exception {
        // Arrange
        String entryKey = "delete_test_key";
        NewEntryRequestDto requestDto = new NewEntryRequestDto(entryKey, "random_value", null);
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(requestDto);
        mvc.perform(post("/cache")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andReturn();

        // Act
        MvcResult mvcResult = mvc.perform(delete("/cache/" + entryKey)).andReturn();

        // Assert
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(200, response.getStatus());
        assertEquals("", response.getContentAsString());

        verify(cacheService).pushEntry(any());
        verify(cacheService).removeEntryByKey(entryKey);
        verifyNoMoreInteractions(cacheService);
    }

    @Test
    void deleteEntryWithInvalidKey_returningBadRequest() throws Exception {
        // Arrange
        String entryKey = " ";

        // Act
        MvcResult mvcResult = mvc.perform(delete("/cache/" + entryKey)).andReturn();

        // Assert
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(400, response.getStatus());
        String responseContentAsString = response.getContentAsString();
        assertNotEquals("", responseContentAsString);

        ArgumentExceptionResponseDto responseDto = new ObjectMapper().readValue(responseContentAsString, ArgumentExceptionResponseDto.class);
        assertEquals("uri=/cache/" + entryKey, URLDecoder.decode(responseDto.getDetail(), Charset.defaultCharset()));

        List<ArgumentErrorDto> argumentsErrors = responseDto.getArgumentsErrors();
        assertEquals(1, argumentsErrors.size());

        ArgumentErrorDto argumentErrorDto = argumentsErrors.get(0);
        assertEquals("key", argumentErrorDto.getField());
        assertEquals("Key cannot be null or empty!", argumentErrorDto.getMessage());
    }

    private void testPostCacheWithSuccessResponse(String cacheEntryKey, String cacheEntryValue, Long ttl) throws Exception {
        // Arrange
        NewEntryRequestDto requestDto = new NewEntryRequestDto(cacheEntryKey, cacheEntryValue, ttl);

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(requestDto);

        // Act
        MvcResult mvcResult = mvc.perform(post("/cache")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andReturn();

        // Assert
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(200, response.getStatus());
        assertEquals("", response.getContentAsString());

        verify(cacheService).pushEntry(newEntryRequestDtoArgumentCaptor.capture());
        NewEntryRequestDto capturedEntryRequestDto = newEntryRequestDtoArgumentCaptor.getValue();
        assertThat(capturedEntryRequestDto).usingRecursiveComparison().isEqualTo(requestDto);

        verifyNoMoreInteractions(cacheService);
    }


    private void testPostCacheWithInvalidArgument(String cacheEntryKey, String cacheEntryValue, Long ttl, String fieldWithError, String expectedErrorMessage) throws Exception {
        // Arrange
        NewEntryRequestDto requestDto = new NewEntryRequestDto(cacheEntryKey, cacheEntryValue, ttl);

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(requestDto);

        // Act
        MvcResult mvcResult = mvc.perform(post("/cache")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andReturn();

        // Assert
        MockHttpServletResponse response = mvcResult.getResponse();
        assertInvalidArgumentResponse(fieldWithError, expectedErrorMessage, objectMapper, response);
        verifyNoInteractions(cacheService);
    }

    private void assertInvalidArgumentResponse(String fieldWithError, String expectedErrorMessage, ObjectMapper objectMapper, MockHttpServletResponse response) throws UnsupportedEncodingException, JsonProcessingException {
        assertEquals(400, response.getStatus());
        String responseContentAsString = response.getContentAsString();
        assertNotEquals("", responseContentAsString);

        ArgumentExceptionResponseDto responseDto = objectMapper.readValue(responseContentAsString, ArgumentExceptionResponseDto.class);
        assertEquals("uri=/cache", responseDto.getDetail());

        List<ArgumentErrorDto> argumentsErrors = responseDto.getArgumentsErrors();
        assertEquals(1, argumentsErrors.size());

        ArgumentErrorDto argumentErrorDto = argumentsErrors.get(0);
        assertEquals(fieldWithError, argumentErrorDto.getField());
        assertEquals(expectedErrorMessage, argumentErrorDto.getMessage());
        verifyNoInteractions(cacheService);
    }
}
