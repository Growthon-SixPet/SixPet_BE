package growthon.withtail_be.domain.review.service;

import growthon.withtail_be.domain.review.dto.GoogleReviewBundleResponse;
import growthon.withtail_be.domain.review.dto.ReviewResponse;
import growthon.withtail_be.domain.review.dto.ReviewSummaryResponse;
import growthon.withtail_be.domain.review.entity.TargetType;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

// 상세 화면용: 자체 후기 + 구글 후기 통합
@Service
@RequiredArgsConstructor
public class ReviewSummaryService {

    private final ReviewService reviewService;
    private final GoogleReviewService googleReviewService;

    // 한 번의 요청으로 두 후기 제공
    public ReviewSummaryResponse summary(TargetType targetType, Long targetId, Pageable pageable) {
        Page<ReviewResponse> internal = reviewService.list(targetType, targetId, pageable);
        GoogleReviewBundleResponse google = googleReviewService.getGoogleReviews(targetType, targetId);

        return ReviewSummaryResponse.builder()
                .internal(internal)
                .google(google)
                .build();
    }
}
