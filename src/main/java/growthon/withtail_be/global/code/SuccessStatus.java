package growthon.withtail_be.global.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessStatus implements BaseCode {

    // Common
    OK(HttpStatus.OK, "COMMON_200", "성공입니다."),
    CREATED(HttpStatus.CREATED, "COMMON_201", "등록이 성공적으로 완료되었습니다."),

    // user
    USER_SIGNUP_SUCCESS(HttpStatus.CREATED, "USER_201_1", "회원가입이 완료되었습니다."),
    USER_PROFILE_GET_SUCCESS(HttpStatus.OK, "USER_200_1", "회원 정보를 조회했습니다."),
    USER_PROFILE_UPDATE_SUCCESS(HttpStatus.OK, "USER_200_2", "회원 정보가 수정되었습니다."),
    USER_PASSWORD_UPDATE_SUCCESS(HttpStatus.OK, "USER_200_3", "비밀번호가 변경되었습니다."),
    USER_DELETE_SUCCESS(HttpStatus.OK, "USER_200_4", "회원 탈퇴가 완료되었습니다."),


    // sms
    PHONE_CODE_SENT(HttpStatus.OK, "SMS_200_1", "인증번호가 발송되었습니다."),
    PHONE_CODE_VERIFIED(HttpStatus.OK, "SMS_200_2", "휴대폰 인증이 완료되었습니다.");


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
                .isSuccess(true)
                .httpStatus(httpStatus)
                .build();
    }
}
