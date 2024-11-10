package com.large.board.common.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    INVALID_INPUT("INVALID_INPUT", "The input parameters are invalid."),
    USER_NOT_FOUND("USER_NOT_FOUND", "User not found."),
    UNAUTHORIZED("UNAUTHORIZED", "You are not authorized to access this resource."),
    INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR", "An unexpected error occurred."),
    BOARD_SERVER_ERROR("BOARD_SERVER_ERROR", "Board server error occurred."),
    METHOD_NOT_ALLOWED("METHOD_NOT_ALLOWED", "The HTTP method is not allowed."),
    FORBIDDEN("FORBIDDEN", "You do not have permission to access this resource."),
    ENTITY_NOT_FOUND("ENTITY_NOT_FOUND", "The requested entity was not found."),
    INVALID_ARGUMENT("INVALID_ARGUMENT", "The argument provided is invalid."),
    IO_EXCEPTION("IO_EXCEPTION", "An error occurred while performing I/O operations."),
    UNAUTHORIZED_ACCESS("UNAUTHORIZED_ACCESS", "Authentication failed, you are unauthorized."),
    ;

    private final String code;
    private final String message;
}
