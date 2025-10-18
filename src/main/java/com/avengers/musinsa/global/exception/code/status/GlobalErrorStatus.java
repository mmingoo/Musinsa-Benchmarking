package com.avengers.musinsa.global.exception.code.status;


import com.avengers.musinsa.global.exception.code.BaseCodeDto;
import com.avengers.musinsa.global.exception.code.BaseErrorCodeInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum GlobalErrorStatus implements BaseErrorCodeInterface {

    // 가장 일반적인 응답
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON400", "잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON401", "인증이 필요합니다."),
    _VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "COMMON402", "Validation Error입니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),
    _NOT_FOUND(HttpStatus.NOT_FOUND, "COMMON404", "요청한 정보를 찾을 수 없습니다."),
    _METHOD_ARGUMENT_ERROR(HttpStatus.BAD_REQUEST, "COMMON405", "Argument Type이 올바르지 않습니다."),
    _INTERNAL_PAGE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "페이지 에러, 0 이상의 페이지를 입력해주세요"),

    // S3 관련 에러
    _S3_UPLOAD_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "S3_5001", "파일 업로드에 실패했습니다."),
    _S3_DELETE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "S3_5002", "파일 삭제에 실패했습니다."),
    _FALIED_READ_FILE(HttpStatus.BAD_REQUEST, "FILE002","파일을 읽는 중 문제가 발생하였습니다."),

    // HASHTAG 에러
    _HASHTAG_ISBLANK(HttpStatus.BAD_REQUEST, "HASHTAG_4001", "공백은 해시태그로 등록할 수 없습니다."),
    _INVALID_HASHTAG_LENGTH(HttpStatus.BAD_REQUEST, "HASHTAG_4002", "해시태그의 길이는 15자 이하여야 합니다."),

    // CATEGORY 에러
    _INVALID_CATEGORY(HttpStatus.BAD_REQUEST, "CATEGORY_4001", "유효하지 않은 카테고리입니다."),

    // 정렬 기준 Enum 에러
    _SORT_TYPE_NOT_VALID(HttpStatus.BAD_REQUEST, "SORT_4001", "유효하지 않은 정렬 기준입니다."),

    // 필터 에러
    _FILTER_NOT_VALID(HttpStatus.BAD_REQUEST, "FILTER_4001", "잘못된 필터입니다."),

    // For test
    TEMP_EXCEPTION(HttpStatus.BAD_REQUEST, "TEMP4001", "예외처리 테스트입니다."),

    // 로그인 타입 관련
    _INVALID_LOGIN_TYPE(HttpStatus.BAD_REQUEST, "LOGIN_4001", "유효하지 않은 로그인 타입입니다."),
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
