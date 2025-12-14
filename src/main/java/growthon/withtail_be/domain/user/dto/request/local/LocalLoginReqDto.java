package growthon.withtail_be.domain.user.dto.request.local;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record LocalLoginReqDto(
        @NotBlank(message = "휴대폰 번호를 입력해주세요.")
        @Pattern(
                regexp = "^010-[0-9]{4}-[0-9]{4}$",
                message = "올바른 전화번호 형식이 아닙니다."
        )
        String phoneNumber,

        @NotBlank(message = "비밀번호를 입력해주세요.")
        String password,

        @NotNull(message = "로그인 유지 여부를 입력해주세요.")
        Boolean keepLogin
) {
}
