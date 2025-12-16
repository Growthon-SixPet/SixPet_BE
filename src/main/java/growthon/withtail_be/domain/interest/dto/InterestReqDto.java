package growthon.withtail_be.domain.interest.dto;

import growthon.withtail_be.domain.interest.domain.TargetType;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class InterestReqDto {
    private TargetType targetType;
    private Long targetId;
}
