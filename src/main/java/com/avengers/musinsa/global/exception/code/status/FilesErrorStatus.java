package com.avengers.musinsa.global.exception.code.status;

import com.avengers.musinsa.global.exception.code.BaseCodeDto;
import com.avengers.musinsa.global.exception.code.BaseErrorCodeInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum FilesErrorStatus implements BaseErrorCodeInterface {

    // 가장 일반적인 응답
    _NOT_FOUND_FILE_URL(HttpStatus.NOT_FOUND, "FILE404", "요청한 파일을 Url로 찾을 수 없습니다."),
    ;

    private final HttpStatus httpStatus;
    private final boolean isSuccess = false;
    private final String code;
    private final String message;

    @Override
    public BaseCodeDto getErrorCode() {
        return BaseCodeDto.builder()
                .httpStatus(httpStatus)
                .isSuccess(isSuccess)
                .code(code)
                .message(message)
                .build();
    }
}
