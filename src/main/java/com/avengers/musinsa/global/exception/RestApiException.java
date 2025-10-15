package com.avengers.musinsa.global.exception;

import com.avengers.musinsa.global.exception.code.BaseCodeDto;
import com.avengers.musinsa.global.exception.code.BaseErrorCodeInterface;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RestApiException extends RuntimeException {

    private final BaseErrorCodeInterface errorCode; //추상화 시킨 인터페이스를 받아서 사용

    //추상화 시킨 ErrorCode의 getErrorCode()를 사용하여 ErrorCode를 반환
    public BaseCodeDto getErrorCode() {
        return this.errorCode.getErrorCode();
    }
}
