package growthon.withtail_be.domain.review.dto;

import growthon.withtail_be.domain.review.entity.TargetType;

import lombok.Getter;

@Getter
public class ReviewCreateRequest {
    private Long userId;
    private TargetType targetType;
    private Long targetId;
    private Integer rating;
    private String content;
}
