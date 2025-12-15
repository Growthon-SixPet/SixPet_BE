package growthon.withtail_be.domain.review.dto;

import growthon.withtail_be.domain.review.entity.Review;
import growthon.withtail_be.domain.review.entity.TargetType;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ReviewResponse {

    private Long id;
    private Long userId;
    private TargetType targetType;
    private Long targetId;
    private Integer rating;
    private String content;
    private LocalDateTime createdAt;

    public static ReviewResponse from(Review review) {
        return ReviewResponse.builder()
                .id(review.getId())
                .userId(review.getUserId())
                .targetType(review.getTargetType())
                .targetId(review.getTargetId())
                .rating(review.getRating())
                .content(review.getContent())
                .createdAt(review.getCreatedAt())
                .build();
    }
}
