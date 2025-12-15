package growthon.withtail_be.domain.review.repository;

import growthon.withtail_be.domain.review.entity.GooglePlaceMapping;
import growthon.withtail_be.domain.review.entity.TargetType;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GooglePlaceMappingRepository extends JpaRepository<GooglePlaceMapping, Long> {

    Optional<GooglePlaceMapping> findByTargetTypeAndTargetId(TargetType targetType, Long targetId);
}
