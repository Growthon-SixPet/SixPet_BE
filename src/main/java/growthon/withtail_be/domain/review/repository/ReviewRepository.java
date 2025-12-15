package growthon.withtail_be.domain.review.repository;

import growthon.withtail_be.domain.review.entity.Review;
import growthon.withtail_be.domain.review.entity.TargetType;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

// 대상(병원/장례) 기준으로 후기 조회
public interface ReviewRepository extends JpaRepository<Review, Long> {

    Page<Review> findByTargetTypeAndTargetId(
            TargetType targetType,
            Long targetId,
            Pageable pageable
    );
}
