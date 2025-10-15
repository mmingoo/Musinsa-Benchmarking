package com.avengers.musinsa.global.exception.code.status;

import com.avengers.musinsa.global.exception.code.BaseCodeDto;
import com.avengers.musinsa.global.exception.code.BaseSuccessCodeInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessStatus implements BaseSuccessCodeInterface {
    // For test
    _OK(HttpStatus.OK, "COMMON200", "성공입니다.")
    ;

    private final HttpStatus httpStatus;
    private final boolean isSuccess = true;
    private final String code;
    private final String message;

    @Override
    public BaseCodeDto getSuccessCode() {
        return BaseCodeDto.builder()
                .httpStatus(httpStatus)
                .isSuccess(isSuccess)
                .code(code)
                .message(message)
                .build();
    }
}
