package growthon.withtail_be.global.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseCode {
    // Common
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", "서버 에러, 관리자에게 문의 바랍니다."),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "BAD_REQUEST", "잘못된 요청입니다."),

    // login
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_404", "존재하지 않는 사용자입니다."),
    REFRESH_TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "AUTH_401_1", "유효하지 않은 리프레시 토큰입니다."),
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "AUTH_404_1", "저장된 리프레시 토큰이 존재하지 않습니다."),
    REFRESH_TOKEN_MISMATCH(HttpStatus.UNAUTHORIZED, "AUTH_401_2", "리프레시 토큰이 일치하지 않습니다."),
    REFRESH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "AUTH_401_3", "만료된 리프레시 토큰입니다."),
    USER_PHONE_ALREADY_EXISTS(HttpStatus.CONFLICT, "USER_409_1", "이미 사용 중인 휴대폰 번호입니다."),


    // google
    GOOGLE_TOKEN_REQUEST_FAILED(HttpStatus.BAD_GATEWAY, "OAUTH_502_1", "구글 Access Token 요청에 실패했습니다."),
    GOOGLE_TOKEN_PARSE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "OAUTH_500_1", "구글 토큰 파싱에 실패했습니다."),
    GOOGLE_USERINFO_REQUEST_FAILED(HttpStatus.BAD_GATEWAY, "OAUTH_502_2", "구글 사용자 정보 요청에 실패했습니다."),
    GOOGLE_USERINFO_PARSE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "OAUTH_500_2", "구글 사용자 정보 파싱에 실패했습니다.");



    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public ReasonDTO getReasonHttpStatus() {
        return ReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build();
    }
}
