package growthon.withtail_be.domain.review.repository;

import growthon.withtail_be.domain.review.entity.Review;
import growthon.withtail_be.domain.review.entity.TargetType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    @EntityGraph(attributePaths = "user")
    List<Review> findByTargetTypeAndTargetIdOrderByCreatedAtDesc(
            TargetType targetType,
            Long targetId
    );

    @EntityGraph(attributePaths = "user")
    List<Review> findByUserIdOrderByCreatedAtDesc(Long userId);

    @Query("""
        select coalesce(avg(r.rating), 0)
        from Review r
        where r.targetType = :targetType
        and r.targetId = :targetId
    """)
    Double findAverageRating(TargetType targetType, Long targetId);

    @Query("""
        select count(r)
        from Review r
        where r.targetType = :targetType and r.targetId = :targetId
    """)
    Long countByTarget(TargetType targetType, Long targetId);
}
