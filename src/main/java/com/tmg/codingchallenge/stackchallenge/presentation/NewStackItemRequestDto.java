package com.tmg.codingchallenge.stackchallenge.presentation;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NewStackItemRequestDto implements Serializable {

    @NotBlank(message = "Item cannot be null or empty!")
    private String newItem;
}
