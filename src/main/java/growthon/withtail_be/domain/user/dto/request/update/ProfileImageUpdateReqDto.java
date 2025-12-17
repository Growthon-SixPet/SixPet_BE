package growthon.withtail_be.domain.user.dto.request.update;

import jakarta.validation.constraints.NotBlank;

public record ProfileImageUpdateReqDto(
        @NotBlank(message = "프로필 이미지 URL을 입력해주세요.")
        String profileImage
) {
}
