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
    USER_PROFILE_IMAGE_UPDATE_SUCCESS(HttpStatus.OK, "USER_200_5", "프로필 이미지가 변경되었습니다."),
    USER_PROFILE_IMAGE_DELETE_SUCCESS(HttpStatus.OK, "USER_200_6", "프로필 이미지가 삭제되었습니다."),

    // sms
    PHONE_CODE_SENT(HttpStatus.OK, "SMS_200_1", "인증번호가 발송되었습니다."),
    PHONE_CODE_VERIFIED(HttpStatus.OK, "SMS_200_2", "휴대폰 인증이 완료되었습니다."),

    // interest
    INTEREST_CREATE_SUCCESS(HttpStatus.CREATED, "INTEREST_201_1", "즐겨찾기 생성 성공"),
    INTEREST_LIST_GET_SUCCESS(HttpStatus.OK, "INTEREST_200_1", "즐겨찾기 목록 조회 성공"),
    INTEREST_GET_SUCCESS(HttpStatus.OK, "INTEREST_200_2", "즐겨찾기 단건 조회 성공"),
    INTEREST_DELETE_SUCCESS(HttpStatus.OK, "INTEREST_200_3", "즐겨찾기 삭제 성공"),

    // reservation
    RESERVATION_CREATE_SUCCESS(HttpStatus.CREATED, "RESERVATION_201_1", "예약 생성 성공"),
    RESERVATION_LIST_GET_SUCCESS(HttpStatus.OK, "RESERVATION_200_1", "예약 목록 조회 성공"),
    RESERVATION_GET_SUCCESS(HttpStatus.OK, "RESERVATION_200_2", "예약 단건 조회 성공"),
    RESERVATION_UPDATE_SUCCESS(HttpStatus.OK, "RESERVATION_200_3", "예약 수정 성공"),
    RESERVATION_CANCEL_SUCCESS(HttpStatus.OK, "RESERVATION_200_4", "예약 취소 성공"),

    // review
    REVIEW_CREATE_SUCCESS(HttpStatus.CREATED, "REVIEW_201_1", "후기 생성 성공"),
    REVIEW_LIST_GET_SUCCESS(HttpStatus.OK, "REVIEW_200_1", "후기 목록 조회 성공"),
    REVIEW_MY_LIST_GET_SUCCESS(HttpStatus.OK, "REVIEW_200_2", "내 후기 목록 조회 성공"),
    REVIEW_UPDATE_SUCCESS(HttpStatus.OK, "REVIEW_200_3", "후기 수정 성공"),
    REVIEW_DELETE_SUCCESS(HttpStatus.OK, "REVIEW_200_4", "후기 삭제 성공"),
    REVIEW_AVG_GET_SUCCESS(HttpStatus.OK, "REVIEW_200_5", "평균 평점 조회 성공");


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
