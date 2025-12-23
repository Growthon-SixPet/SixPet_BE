package growthon.withtail_be.domain.animalfuneral.repository;

import growthon.withtail_be.domain.animalfuneral.entity.AnimalFuneral;
import growthon.withtail_be.domain.model.RegionType;
import org.springframework.data.jpa.domain.Specification;

public final class AnimalFuneralSpecifications {

    private AnimalFuneralSpecifications() {
    }

    // 이름 keyword 검색 (대소문자 무시)
    public static Specification<AnimalFuneral> keywordLike(String keyword) {
        return (root, query, cb) -> {
            if (keyword == null || keyword.isBlank()) {
                return cb.conjunction();
            }
            String pattern = "%" + keyword.trim().toLowerCase() + "%";
            return cb.like(cb.lower(root.get("name")), pattern);
        };
    }

    // 권역(region) 필터
    public static Specification<AnimalFuneral> regionEq(RegionType region) {
        return (root, query, cb) -> {
            if (region == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("region"), region);
        };
    }

    // 메모리얼스톤(블리스스톤) 제공 여부
    public static Specification<AnimalFuneral> blissStoneAvailable(Boolean available) {
        return (root, query, cb) -> {
            if (available == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("blissStoneAvailable"), available);
        };
    }

    // 가격 필터 (대표 최소비용 컬럼 minCost 기준)
    public static Specification<AnimalFuneral> minCostBetween(Integer min, Integer max) {
        return (root, query, cb) -> {
            if (min == null && max == null) {
                return cb.conjunction();
            }
            if (min != null && max != null) {
                return cb.between(root.get("minCost"), min, max);
            }
            if (min != null) {
                return cb.greaterThanOrEqualTo(root.get("minCost"), min);
            }
            return cb.lessThanOrEqualTo(root.get("minCost"), max);
        };
    }
}
