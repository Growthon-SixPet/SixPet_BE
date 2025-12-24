package growthon.withtail_be.domain.review.dto;

import growthon.withtail_be.domain.review.entity.TargetType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReviewCreateReqDto {

        @NotNull(message = "후기 대상 타입은 필수입니다.")
        private TargetType targetType;

        @NotNull(message = "후기 대상 ID는 필수입니다.")
        private Long targetId;

        @NotNull(message = "평점은 필수입니다.")
        @Min(value = 1, message = "평점은 1 이상이어야 합니다.")
        @Max(value = 5, message = "평점은 5 이하여야 합니다.")
        private Integer rating;

        @NotBlank(message = "후기 내용은 필수입니다.")
        private String content;
}
