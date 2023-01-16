package com.tmg.codingchallenge.cachechallenge.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
public class NewEntryRequestDto implements Serializable {

    @NotBlank(message = "Entry key cannot be null or empty!")
    private String key;
    @NotNull(message = "Entry value cannot be null!")
    private String value;
    @Positive(message = "When informed, TTL value must be a greater than ZERO!")
    private Long ttl;
}
