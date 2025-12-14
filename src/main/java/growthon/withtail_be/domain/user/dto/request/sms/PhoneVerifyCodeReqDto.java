package growthon.withtail_be.domain.user.dto.request.sms;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record PhoneVerifyCodeReqDto(
        @NotBlank(message = "휴대폰 번호를 입력해주세요.")
        @Pattern(regexp = "^010-[0-9]{4}-[0-9]{4}$", message = "올바른 전화번호 형식이 아닙니다.")
        String phoneNumber,

        @NotBlank(message = "인증번호를 입력해주세요.")
        @Pattern(regexp = "^[0-9]{6}$", message = "인증번호는 6자리 숫자여야 합니다.")
        String code
) {}
