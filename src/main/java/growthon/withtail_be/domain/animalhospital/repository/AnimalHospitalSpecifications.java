package growthon.withtail_be.domain.animalhospital.repository;

import growthon.withtail_be.domain.animalhospital.entity.AnimalHospital;
import growthon.withtail_be.domain.animalhospital.entity.HospitalAmenity;
import growthon.withtail_be.domain.animalhospital.entity.HospitalAnimalType;
import growthon.withtail_be.domain.animalhospital.entity.HospitalPaymentMethod;
import growthon.withtail_be.domain.animalhospital.entity.HospitalSpecialty;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

public final class AnimalHospitalSpecifications {

    private AnimalHospitalSpecifications() {
    }

    public static Specification<AnimalHospital> keywordLike(String keyword) {
        return (root, query, cb) -> {
            if (keyword == null || keyword.isBlank()) {
                return cb.conjunction();
            }
            String pattern = "%" + keyword.trim() + "%";
            return cb.like(root.get("name"), pattern);
        };
    }

    public static Specification<AnimalHospital> sidoEq(String sido) {
        return (root, query, cb) -> {
            if (sido == null || sido.isBlank()) {
                return cb.conjunction();
            }
            return cb.equal(root.get("sido"), sido.trim());
        };
    }

    public static Specification<AnimalHospital> sigunguEq(String sigungu) {
        return (root, query, cb) -> {
            if (sigungu == null || sigungu.isBlank()) {
                return cb.conjunction();
            }
            return cb.equal(root.get("sigungu"), sigungu.trim());
        };
    }

    public static Specification<AnimalHospital> open24h(Boolean open24h) {
        return (root, query, cb) -> {
            if (open24h == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("open24h"), open24h);
        };
    }

    public static Specification<AnimalHospital> nightCare(Boolean nightCare) {
        return (root, query, cb) -> {
            if (nightCare == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("nightCare"), nightCare);
        };
    }

    public static Specification<AnimalHospital> emergencyAvailable(Boolean emergencyAvailable) {
        return (root, query, cb) -> {
            if (emergencyAvailable == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("emergencyAvailable"), emergencyAvailable);
        };
    }

    public static Specification<AnimalHospital> hasSpecialtyIds(List<Long> ids) {
        return (root, query, cb) -> {
            if (ids == null || ids.isEmpty()) {
                return cb.conjunction();
            }
            query.distinct(true);
            Join<AnimalHospital, HospitalSpecialty> join = root.join("specialties", JoinType.INNER);
            return join.get("specialty").get("id").in(ids);
        };
    }

    public static Specification<AnimalHospital> hasAnimalTypeIds(List<Long> ids) {
        return (root, query, cb) -> {
            if (ids == null || ids.isEmpty()) {
                return cb.conjunction();
            }
            query.distinct(true);
            Join<AnimalHospital, HospitalAnimalType> join = root.join("animalTypes", JoinType.INNER);
            return join.get("animalType").get("id").in(ids);
        };
    }

    public static Specification<AnimalHospital> hasAmenityIds(List<Long> ids) {
        return (root, query, cb) -> {
            if (ids == null || ids.isEmpty()) {
                return cb.conjunction();
            }
            query.distinct(true);
            Join<AnimalHospital, HospitalAmenity> join = root.join("amenities", JoinType.INNER);
            return join.get("amenity").get("id").in(ids);
        };
    }

    public static Specification<AnimalHospital> hasPaymentMethodIds(List<Long> ids) {
        return (root, query, cb) -> {
            if (ids == null || ids.isEmpty()) {
                return cb.conjunction();
            }
            query.distinct(true);
            Join<AnimalHospital, HospitalPaymentMethod> join = root.join("paymentMethods", JoinType.INNER);
            return join.get("paymentMethod").get("id").in(ids);
        };
    }
}
