package growthon.withtail_be.domain.review.service;

import growthon.withtail_be.domain.review.dto.GooglePlaceMappingUpsertRequest;
import growthon.withtail_be.domain.review.entity.GooglePlaceMapping;
import growthon.withtail_be.domain.review.repository.GooglePlaceMappingRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class GooglePlaceMappingService {

    private final GooglePlaceMappingRepository mappingRepository;

    @Transactional
    public void upsert(GooglePlaceMappingUpsertRequest request) {
        if (request.getTargetType() == null || request.getTargetId() == null) {
            throw new IllegalArgumentException("targetType/targetId는 필수입니다.");
        }
        if (request.getPlaceId() == null || request.getPlaceId().isBlank()) {
            throw new IllegalArgumentException("placeId는 필수입니다.");
        }

        mappingRepository.findByTargetTypeAndTargetId(request.getTargetType(), request.getTargetId())
                .ifPresentOrElse(
                        m -> m.changePlaceId(request.getPlaceId()),
                        () -> mappingRepository.save(
                                GooglePlaceMapping.builder()
                                        .targetType(request.getTargetType())
                                        .targetId(request.getTargetId())
                                        .placeId(request.getPlaceId())
                                        .build()
                        )
                );
    }
}
