package growthon.withtail_be.domain.review.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GoogleReviewItemResponse {
    private String authorName;
    private Integer rating;
    private String text;
    private String relativeTime;
}
