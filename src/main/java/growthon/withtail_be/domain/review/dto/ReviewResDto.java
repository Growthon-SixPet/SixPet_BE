package growthon.withtail_be.domain.review.dto;

import growthon.withtail_be.domain.review.entity.Review;
import growthon.withtail_be.domain.review.entity.TargetType;

import java.time.LocalDateTime;
import java.util.Objects;

public record ReviewResDto(
        Long reviewId,
        TargetType targetType,
        Long targetId,
        String targetName,

        Integer rating,
        String content,
        String imageUrl,

        String writerNickname,

        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        boolean isMine
) {
    public static ReviewResDto from(Review review, Long userId, String targetName) {
        boolean isMine = (userId != null) && Objects.equals(review.getUser().getId(), userId);

        return new ReviewResDto(
                review.getId(),
                review.getTargetType(),
                review.getTargetId(),
                targetName,

                review.getRating(),
                review.getContent(),
                review.getImageUrl(),

                review.getUser().getNickname(),

                review.getCreatedAt(),
                review.getUpdatedAt(),

                isMine
        );
    }
}
