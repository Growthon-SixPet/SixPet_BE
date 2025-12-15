package growthon.withtail_be.domain.review.service;

import growthon.withtail_be.domain.review.dto.ReviewCreateRequest;
import growthon.withtail_be.domain.review.dto.ReviewResponse;
import growthon.withtail_be.domain.review.entity.Review;
import growthon.withtail_be.domain.review.entity.TargetType;
import growthon.withtail_be.domain.review.repository.ReviewRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

// 자체 후기 생성 및 조회 담당
@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    // 후기 생성
    public ReviewResponse create(ReviewCreateRequest request) {
        validateRequest(request);

        Review saved = reviewRepository.save(
                Review.builder()
                        .userId(request.getUserId())
                        .targetType(request.getTargetType())
                        .targetId(request.getTargetId())
                        .rating(request.getRating())
                        .content(request.getContent())
                        .build()
        );

        return ReviewResponse.from(saved);
    }

    // 대상별 후기 조회
    public Page<ReviewResponse> list(TargetType targetType, Long targetId, Pageable pageable) {
        return reviewRepository.findByTargetTypeAndTargetId(targetType, targetId, pageable)
                .map(ReviewResponse::from);
    }

    // 기본 요청값 검증
    private void validateRequest(ReviewCreateRequest request) {
        if (request.getUserId() == null) {
            throw new IllegalArgumentException("userId는 필수입니다.");
        }
        if (request.getTargetType() == null) {
            throw new IllegalArgumentException("targetType은 필수입니다.");
        }
        if (request.getTargetId() == null) {
            throw new IllegalArgumentException("targetId는 필수입니다.");
        }
        if (request.getRating() == null || request.getRating() < 1 || request.getRating() > 5) {
            throw new IllegalArgumentException("rating은 1~5 범위여야 합니다.");
        }
        if (request.getContent() == null || request.getContent().isBlank()) {
            throw new IllegalArgumentException("content는 필수입니다.");
        }
    }
}
