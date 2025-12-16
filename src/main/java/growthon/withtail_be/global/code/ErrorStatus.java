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

    // auth
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_404", "존재하지 않는 사용자입니다."),
    REFRESH_TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "AUTH_401_1", "유효하지 않은 리프레시 토큰입니다."),
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "AUTH_404_1", "저장된 리프레시 토큰이 존재하지 않습니다."),
    REFRESH_TOKEN_MISMATCH(HttpStatus.UNAUTHORIZED, "AUTH_401_2", "리프레시 토큰이 일치하지 않습니다."),
    REFRESH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "AUTH_401_3", "만료된 리프레시 토큰입니다."),
    USER_PHONE_ALREADY_EXISTS(HttpStatus.CONFLICT, "USER_409_1", "이미 사용 중인 휴대폰 번호입니다."),
    AUTH_PROVIDER_MISMATCH(HttpStatus.BAD_REQUEST, "AUTH_400_1", "해당 계정은 이 로그인 방식으로 로그인할 수 없습니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "AUTH_401_4", "비밀번호가 올바르지 않습니다."),

    // sms
    SMS_SEND_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "SMS_500_1", "인증 문자 발송에 실패했습니다."),
    SMS_CODE_NOT_FOUND(HttpStatus.BAD_REQUEST, "SMS_400_1", "인증번호가 존재하지 않거나 만료되었습니다."),
    SMS_CODE_MISMATCH(HttpStatus.BAD_REQUEST, "SMS_400_2", "인증번호가 일치하지 않습니다."),
    SMS_NOT_VERIFIED(HttpStatus.BAD_REQUEST, "SMS_400_3", "휴대폰 인증이 완료되지 않았습니다."),
    SMS_TOO_MANY_REQUESTS(HttpStatus.TOO_MANY_REQUESTS, "SMS_429_1", "인증 요청 횟수가 초과되었습니다."),

    // user
    PASSWORD_CONFIRM_MISMATCH(HttpStatus.BAD_REQUEST, "AUTH_400_2", "새 비밀번호와 비밀번호 확인이 일치하지 않습니다."),

    // user profile image
    PROFILE_IMAGE_REQUIRED(HttpStatus.BAD_REQUEST, "USER_400_3", "프로필 이미지 파일이 필요합니다."),
    PROFILE_IMAGE_NOT_FOUND(HttpStatus.BAD_REQUEST, "USER_400_4", "삭제할 프로필 이미지가 존재하지 않습니다."),

    // google
    GOOGLE_TOKEN_REQUEST_FAILED(HttpStatus.BAD_GATEWAY, "OAUTH_502_1", "구글 Access Token 요청에 실패했습니다."),
    GOOGLE_TOKEN_PARSE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "OAUTH_500_1", "구글 토큰 파싱에 실패했습니다."),
    GOOGLE_USERINFO_REQUEST_FAILED(HttpStatus.BAD_GATEWAY, "OAUTH_502_2", "구글 사용자 정보 요청에 실패했습니다."),
    GOOGLE_USERINFO_PARSE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "OAUTH_500_2", "구글 사용자 정보 파싱에 실패했습니다."),

    // s3
    S3_FILE_EMPTY(HttpStatus.BAD_REQUEST, "S3_400_1", "업로드할 파일이 비어있습니다."),
    S3_FILE_NAME_INVALID(HttpStatus.BAD_REQUEST, "S3_400_2", "파일명이 올바르지 않습니다."),
    S3_URL_INVALID(HttpStatus.BAD_REQUEST, "S3_400_3", "S3 URL 형식이 올바르지 않습니다."),
    S3_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "S3_500_1", "S3 업로드에 실패했습니다."),
    S3_DELETE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "S3_500_2", "S3 삭제에 실패했습니다.");


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
