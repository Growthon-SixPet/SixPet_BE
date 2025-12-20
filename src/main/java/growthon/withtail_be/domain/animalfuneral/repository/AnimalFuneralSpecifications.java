package growthon.withtail_be.domain.animalfuneral.repository;

import growthon.withtail_be.domain.animalfuneral.entity.AnimalFuneral;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

public final class AnimalFuneralSpecifications {

    private AnimalFuneralSpecifications() {
    }

    public static Specification<AnimalFuneral> keywordLike(String keyword) {
        return (root, query, cb) -> {
            if (keyword == null || keyword.isBlank()) {
                return cb.conjunction();
            }
            return cb.like(root.get("name"), "%" + keyword.trim() + "%");
        };
    }

    public static Specification<AnimalFuneral> sidoEq(String sido) {
        return (root, query, cb) -> {
            if (sido == null || sido.isBlank()) {
                return cb.conjunction();
            }
            return cb.equal(root.get("sido"), sido.trim());
        };
    }

    public static Specification<AnimalFuneral> sigunguEq(String sigungu) {
        return (root, query, cb) -> {
            if (sigungu == null || sigungu.isBlank()) {
                return cb.conjunction();
            }
            return cb.equal(root.get("sigungu"), sigungu.trim());
        };
    }

    public static Specification<AnimalFuneral> blissStoneAvailable(Boolean available) {
        return (root, query, cb) -> {
            if (available == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("blissStoneAvailable"), available.booleanValue());
        };
    }

    // 비용 필터: min/max 가격 범위에 해당하는 cost가 하나라도 있으면 매칭
    public static Specification<AnimalFuneral> costBetween(Integer minCost, Integer maxCost) {
        return (root, query, cb) -> {
            if (minCost == null && maxCost == null) {
                return cb.conjunction();
            }

            // join funeral_costs
            Join<Object, Object> costJoin = root.join("costs", JoinType.LEFT);

            List<Predicate> predicates = new ArrayList<>();
            if (minCost != null) {
                predicates.add(cb.greaterThanOrEqualTo(costJoin.get("price"), minCost));
            }
            if (maxCost != null) {
                predicates.add(cb.lessThanOrEqualTo(costJoin.get("price"), maxCost));
            }

            // 페이징에서 중복 제거
            query.distinct(true);
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    // amenityIds: 선택한 amenity 중 하나라도 있으면 매칭(OR)
    public static Specification<AnimalFuneral> hasAmenityIds(List<Long> amenityIds) {
        return (root, query, cb) -> {
            if (amenityIds == null || amenityIds.isEmpty()) {
                return cb.conjunction();
            }

            Join<Object, Object> linkJoin = root.join("amenities", JoinType.INNER);
            Join<Object, Object> amenityJoin = linkJoin.join("amenity", JoinType.INNER);

            query.distinct(true);
            return amenityJoin.get("id").in(amenityIds);
        };
    }
}
