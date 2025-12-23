package growthon.withtail_be.domain.review.dto;

import growthon.withtail_be.domain.review.entity.Review;
import growthon.withtail_be.domain.review.entity.TargetType;

import java.time.LocalDateTime;

public record ReviewResDto(
        Long reviewId,
        TargetType targetType,
        Long targetId,

        Integer rating,
        String content,
        String imageUrl,

        String writerNickname,

        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        boolean isMine
) {
    public static ReviewResDto from(Review review, Long userId) {
        return new ReviewResDto(
                review.getId(),
                review.getTargetType(),
                review.getTargetId(),

                review.getRating(),
                review.getContent(),
                review.getImageUrl(),

                review.getUser().getNickname(),

                review.getCreatedAt(),
                review.getUpdatedAt(),

                review.getUser().getId().equals(userId)
        );
    }
}
