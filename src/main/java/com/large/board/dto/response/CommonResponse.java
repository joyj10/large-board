package com.large.board.dto.response;

import com.large.board.common.code.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommonResponse<T> {

    private int status;
    private String code;
    private String message;
    private T requestBody;

    private String errorCode;

    public static<T> CommonResponse<T> ok(T requestBody) {
        HttpStatus httpStatus = HttpStatus.OK;
        return new CommonResponse<>(httpStatus.value(), httpStatus.name(), null, requestBody, null);
    }

    public static<T> CommonResponse<T> error(HttpStatus httpStatus, String message, ErrorCode errorCode) {
        return new CommonResponse<>(httpStatus.value(), httpStatus.name(), message, null, errorCode.getCode());
    }
}
