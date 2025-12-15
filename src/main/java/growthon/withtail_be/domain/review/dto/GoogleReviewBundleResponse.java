package growthon.withtail_be.domain.review.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class GoogleReviewBundleResponse {
    private String placeId;
    private Double rating;
    private Integer totalReviews;
    private List<GoogleReviewItemResponse> items;
}
