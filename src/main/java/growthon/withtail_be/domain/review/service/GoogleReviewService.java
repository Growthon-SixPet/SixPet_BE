package growthon.withtail_be.domain.review.service;

import growthon.withtail_be.domain.review.dto.GoogleReviewBundleResponse;
import growthon.withtail_be.domain.review.dto.GoogleReviewItemResponse;
import growthon.withtail_be.domain.review.entity.GooglePlaceMapping;
import growthon.withtail_be.domain.review.entity.TargetType;
import growthon.withtail_be.domain.review.repository.GooglePlaceMappingRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

// 구글 리뷰 조회 담당 (외부 API)
@Service
@RequiredArgsConstructor
public class GoogleReviewService {

    private final GooglePlaceMappingRepository mappingRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${google.places.api-key:}")
    private String apiKey;

    // 대상 기준으로 구글 리뷰 조회
    public GoogleReviewBundleResponse getGoogleReviews(
            TargetType targetType,
            Long targetId
    ) {
        // placeId 매핑 조회
        GooglePlaceMapping mapping = mappingRepository
                .findByTargetTypeAndTargetId(targetType, targetId)
                .orElse(null);

        // 매핑이 없으면 빈 응답 반환
        if (mapping == null) {
            return GoogleReviewBundleResponse.builder()
                    .placeId(null)
                    .rating(null)
                    .totalReviews(0)
                    .items(List.of())
                    .build();
        }

        // TODO: 실제 Google Places Details API 호출 + 응답 파싱
        // 지금은 구조만 완성 (PR용/테스트용으로 빈 값 반환)
        return GoogleReviewBundleResponse.builder()
                .placeId(mapping.getPlaceId())
                .rating(null)
                .totalReviews(0)
                .items(List.of())
                .build();
    }
}

// DB에 구글 후기 저장하지 않음 아직 실제 Google API 호출은 추후 구현할 예정..

