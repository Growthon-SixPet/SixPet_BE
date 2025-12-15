package growthon.withtail_be.domain.review.dto;

import growthon.withtail_be.domain.review.entity.TargetType;

import lombok.Getter;

@Getter
public class GooglePlaceMappingUpsertRequest {
    private TargetType targetType;
    private Long targetId;
    private String placeId;
}
