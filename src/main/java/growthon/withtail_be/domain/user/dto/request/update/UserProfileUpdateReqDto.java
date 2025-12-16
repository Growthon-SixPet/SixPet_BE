package growthon.withtail_be.domain.user.dto.request.update;

import growthon.withtail_be.domain.user.entity.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record UserProfileUpdateReqDto(

        @NotBlank(message = "이름을 입력해주세요.")
        @Size(max = 10, message = "이름은 최대 10자까지 입력 가능합니다.")
        String name,

        @NotBlank(message = "닉네임을 입력해주세요.")
        @Size(max = 10, message = "닉네임은 최대 10자까지 입력 가능합니다.")
        String nickname,

        @NotNull(message = "생년월일을 입력해주세요.")
        LocalDate birthDate,

        @NotNull(message = "성별을 선택해주세요.")
        Gender gender,

        @NotBlank(message = "주소를 입력해주세요.")
        String address
) {
}

