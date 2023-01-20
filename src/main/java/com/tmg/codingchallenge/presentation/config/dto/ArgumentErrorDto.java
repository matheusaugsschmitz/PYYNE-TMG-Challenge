package com.tmg.codingchallenge.presentation.config.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArgumentErrorDto implements Serializable {

    private String field;
    private String message;
}

