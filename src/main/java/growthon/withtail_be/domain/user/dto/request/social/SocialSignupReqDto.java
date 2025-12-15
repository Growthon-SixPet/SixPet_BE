package growthon.withtail_be.domain.user.dto.request.social;

import growthon.withtail_be.domain.user.entity.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record SocialSignupReqDto(

        @NotBlank(message = "휴대폰 번호를 입력해주세요.")
        @Pattern(
                regexp = "^010-[0-9]{4}-[0-9]{4}$",
                message = "올바른 전화번호 형식이 아닙니다."
        )
        String phoneNumber,

        @NotBlank(message = "이름을 입력해주세요.")
        @Size(max = 10)
        String name,

        @NotBlank(message = "닉네임을 입력해주세요.")
        @Size(max = 10)
        String nickname,

        @NotNull(message = "생년월일을 입력해주세요.")
        LocalDate birthDate,

        @NotNull(message = "성별을 선택해주세요.")
        Gender gender,

        @NotBlank(message = "주소를 입력해주세요.")
        String address
) {
}
