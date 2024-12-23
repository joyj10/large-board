package com.large.board.common.exception.handler;

import com.large.board.common.code.ErrorCode;
import com.large.board.common.exception.BoardServerException;
import com.large.board.common.exception.NoticeException;
import com.large.board.dto.response.CommonResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Log4j2
@RestControllerAdvice
public class CustomExceptionHandler {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public CommonResponse<Void> handleException(Exception e) {
        log.error("Exception occurred : {}", e.getMessage(), e);
        return CommonResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), ErrorCode.INTERNAL_SERVER_ERROR);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(BoardServerException.class)
    public CommonResponse<Void> handleBoardServerException(BoardServerException e) {
        log.error("BoardServerException occurred : {}", e.getMessage(), e);
        return CommonResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), ErrorCode.BOARD_SERVER_ERROR);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public CommonResponse<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        StringBuilder errorMessage = new StringBuilder();
        e.getBindingResult().getAllErrors().forEach(error -> {
            errorMessage.append(error.getDefaultMessage()).append(" ");
        });

        log.error("Validation failed: {}", errorMessage.toString());
        return CommonResponse.error(HttpStatus.BAD_REQUEST, errorMessage.toString(), ErrorCode.INVALID_INPUT);
    }

    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public CommonResponse<Void> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.error("Method not supported: {}", e.getMessage());
        return CommonResponse.error(HttpStatus.METHOD_NOT_ALLOWED,e.getMessage(), ErrorCode.METHOD_NOT_ALLOWED);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public CommonResponse<Void> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.error("HttpMessageNotReadableException occurred: {}", e.getMessage(), e);

        return CommonResponse.error(HttpStatus.BAD_REQUEST, e.getMessage(), ErrorCode.INVALID_INPUT);
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    public CommonResponse<Void> handleAccessDeniedException(AccessDeniedException e) {
        log.error("Access denied: {}", e.getMessage());
        return CommonResponse.error(HttpStatus.FORBIDDEN,e.getMessage(), ErrorCode.UNAUTHORIZED);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException.class)
    public CommonResponse<Void> handleEntityNotFoundException(EntityNotFoundException e) {
        log.error("Entity not found: {}", e.getMessage());
        e.getMessage();
        return CommonResponse.error(HttpStatus.NOT_FOUND, e.getMessage(), ErrorCode.ENTITY_NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public CommonResponse<Void> handleInvalidArgumentException(IllegalArgumentException e) {
        log.error("Invalid argument: {}", e.getMessage());
        return CommonResponse.error(HttpStatus.BAD_REQUEST, e.getMessage(), ErrorCode.INVALID_INPUT);
    }

    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    @ExceptionHandler(NoticeException.class)
    public CommonResponse<Void> handleNoticeException(NoticeException e) {
        log.error("Notice Exception: {}", e.getMessage());
        return CommonResponse.error(HttpStatus.BAD_GATEWAY, e.getMessage(), ErrorCode.EXTERNAL_SERVER_ERROR);
    }
}
