package growthon.withtail_be.domain.animalhospital.repository;

import growthon.withtail_be.domain.animalhospital.entity.AnimalHospital;
import growthon.withtail_be.domain.animalhospital.entity.HospitalAmenity;
import growthon.withtail_be.domain.animalhospital.entity.HospitalAnimalType;
import growthon.withtail_be.domain.animalhospital.entity.HospitalPaymentMethod;
import growthon.withtail_be.domain.animalhospital.entity.HospitalSpecialty;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
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
            String like = "%" + keyword.toLowerCase() + "%";
            return cb.like(cb.lower(root.get("name")), like);
        };
    }

    public static Specification<AnimalHospital> sidoEq(String sido) {
        return (root, query, cb) -> {
            if (sido == null || sido.isBlank()) {
                return cb.conjunction();
            }
            return cb.equal(root.get("sido"), sido);
        };
    }

    public static Specification<AnimalHospital> sigunguEq(String sigungu) {
        return (root, query, cb) -> {
            if (sigungu == null || sigungu.isBlank()) {
                return cb.conjunction();
            }
            return cb.equal(root.get("sigungu"), sigungu);
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

    public static Specification<AnimalHospital> emergencyAvailable(Boolean emergencyAvailable) {
        return (root, query, cb) -> {
            if (emergencyAvailable == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("emergencyAvailable"), emergencyAvailable);
        };
    }

    public static Specification<AnimalHospital> hasSpecialtyIds(List<Long> specialtyIds) {
        return (root, query, cb) -> {
            if (specialtyIds == null || specialtyIds.isEmpty()) {
                return cb.conjunction();
            }
            query.distinct(true);

            Join<AnimalHospital, HospitalSpecialty> hs = root.join("hospitalSpecialties", JoinType.INNER);
            return hs.get("specialty").get("id").in(specialtyIds);
        };
    }

    public static Specification<AnimalHospital> hasAnimalTypeIds(List<Long> animalTypeIds) {
        return (root, query, cb) -> {
            if (animalTypeIds == null || animalTypeIds.isEmpty()) {
                return cb.conjunction();
            }
            query.distinct(true);

            Join<AnimalHospital, HospitalAnimalType> hat = root.join("hospitalAnimalTypes", JoinType.INNER);
            return hat.get("animalType").get("id").in(animalTypeIds);
        };
    }

    public static Specification<AnimalHospital> hasAmenityIds(List<Long> amenityIds) {
        return (root, query, cb) -> {
            if (amenityIds == null || amenityIds.isEmpty()) {
                return cb.conjunction();
            }
            query.distinct(true);

            Join<AnimalHospital, HospitalAmenity> ha = root.join("hospitalAmenities", JoinType.INNER);
            return ha.get("amenity").get("id").in(amenityIds);
        };
    }

    public static Specification<AnimalHospital> hasPaymentMethodIds(List<Long> paymentMethodIds) {
        return (root, query, cb) -> {
            if (paymentMethodIds == null || paymentMethodIds.isEmpty()) {
                return cb.conjunction();
            }
            query.distinct(true);

            Join<AnimalHospital, HospitalPaymentMethod> hpm = root.join("hospitalPaymentMethods", JoinType.INNER);
            return hpm.get("paymentMethod").get("id").in(paymentMethodIds);
        };
    }
}
