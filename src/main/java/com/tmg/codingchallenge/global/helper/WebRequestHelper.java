package com.tmg.codingchallenge.global.helper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.WebRequest;

import java.util.Optional;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WebRequestHelper {

    public static String getUriOrigin(WebRequest request) {
        return Optional
                .ofNullable(request.getHeader("X-Request-Origin"))
                .map(r -> "uri=" + r)
                .orElse(request.getDescription(false));

    }
}
