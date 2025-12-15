package growthon.withtail_be.domain.review.controller;

import growthon.withtail_be.domain.review.dto.GooglePlaceMappingUpsertRequest;
import growthon.withtail_be.domain.review.dto.ReviewCreateRequest;
import growthon.withtail_be.domain.review.dto.ReviewResponse;
import growthon.withtail_be.domain.review.dto.ReviewSummaryResponse;
import growthon.withtail_be.domain.review.entity.TargetType;
import growthon.withtail_be.domain.review.service.GooglePlaceMappingService;
import growthon.withtail_be.domain.review.service.ReviewService;
import growthon.withtail_be.domain.review.service.ReviewSummaryService;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;
    private final ReviewSummaryService reviewSummaryService;
    private final GooglePlaceMappingService mappingService;

    @PostMapping
    public ReviewResponse create(@RequestBody ReviewCreateRequest request) {
        return reviewService.create(request);
    }

    @GetMapping
    public Page<ReviewResponse> list(
            @RequestParam TargetType targetType,
            @RequestParam Long targetId,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        return reviewService.list(targetType, targetId, pageable);
    }

    // 와이어프레임용: 자체 + 구글 한번에
    @GetMapping("/summary")
    public ReviewSummaryResponse summary(
            @RequestParam TargetType targetType,
            @RequestParam Long targetId,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        return reviewSummaryService.summary(targetType, targetId, pageable);
    }

    // placeId 매핑 등록/수정(테스트/운영용)
    @PutMapping("/google-mapping")
    public void upsertMapping(@RequestBody GooglePlaceMappingUpsertRequest request) {
        mappingService.upsert(request);
    }
}
