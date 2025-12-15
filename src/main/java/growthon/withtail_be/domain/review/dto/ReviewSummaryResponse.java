package growthon.withtail_be.domain.review.dto;

import lombok.Builder;
import lombok.Getter;

import org.springframework.data.domain.Page;

@Getter
@Builder
public class ReviewSummaryResponse {
    private Page<ReviewResponse> internal;
    private GoogleReviewBundleResponse google;
}
