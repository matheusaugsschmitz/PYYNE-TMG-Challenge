package com.tmg.codingchallenge.stackchallenge.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static java.util.Objects.isNull;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StringUtils {

    public static String coalesce(String... parameters) {
        for (String parameter : parameters) {
            if (!isNull(parameter))
                return parameter;
        }
        return null;
    }
}
