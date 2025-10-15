package com.avengers.musinsa.global.base;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.avengers.musinsa.global.exception.code.status.SuccessStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"timestamp", "success" ,"code", "message", "result"})
public class BaseResponse<T> {

    private final LocalDateTime timestamp = LocalDateTime.now();
    private final boolean success;
    private final String code;
    private final String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T result;


    //성공한 경우 응답 생성
    public static <T> BaseResponse<T> onSuccess(T result) {
        return new BaseResponse<>(true, SuccessStatus._OK.getCode(), SuccessStatus._OK.getMessage(), result);
    }

    // 실패한 경우 응답 생성
    public static <T> BaseResponse<T> onFailure(String code, String message, T data) {
        return new BaseResponse<>(false, code, message, data);
    }

}
