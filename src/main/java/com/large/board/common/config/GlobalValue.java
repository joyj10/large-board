package com.large.board.common.config;

import lombok.experimental.UtilityClass;

@UtilityClass
public class GlobalValue {
    public static final String[] WHITE_LIST = {
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/auth/login",
            "/users/sign-up"
    };
}
