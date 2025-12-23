package growthon.withtail_be.domain.animalhospital.repository;

import growthon.withtail_be.domain.animalhospital.entity.AnimalHospital;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AnimalHospitalRepository extends JpaRepository<AnimalHospital, Long>, JpaSpecificationExecutor<AnimalHospital> {

    // --- 기본(컬렉션 없이) ---
    Optional<AnimalHospital> findById(Long id);

    // --- 상세(기본): 운영시간만 fetch ---
    @Query("""
        select distinct h
        from AnimalHospital h
        left join fetch h.operatingHours oh
        where h.id = :id
    """)
    Optional<AnimalHospital> findWithOperatingHoursById(@Param("id") Long id);

    // --- 상세(기본): 제공서비스만 fetch (HospitalAmenity + Amenity) ---
    @Query("""
        select distinct h
        from AnimalHospital h
        left join fetch h.amenities ha
        left join fetch ha.amenity a
        where h.id = :id
    """)
    Optional<AnimalHospital> findWithAmenitiesById(@Param("id") Long id);

    // --- 상세(기본): 전문분야만 fetch (HospitalSpecialty + Specialty) ---
    @Query("""
        select distinct h
        from AnimalHospital h
        left join fetch h.specialties hs
        left join fetch hs.specialty s
        where h.id = :id
    """)
    Optional<AnimalHospital> findWithSpecialtiesById(@Param("id") Long id);

    // --- 상세(기본): 동물종만 fetch (HospitalAnimalType + AnimalType) ---
    @Query("""
        select distinct h
        from AnimalHospital h
        left join fetch h.animalTypes hat
        left join fetch hat.animalType at
        where h.id = :id
    """)
    Optional<AnimalHospital> findWithAnimalTypesById(@Param("id") Long id);

    // --- 상세(병원소개): 결제수단만 fetch (HospitalPaymentMethod + PaymentMethod) ---
    @Query("""
        select distinct h
        from AnimalHospital h
        left join fetch h.paymentMethods hpm
        left join fetch hpm.paymentMethod pm
        where h.id = :id
    """)
    Optional<AnimalHospital> findWithPaymentMethodsById(@Param("id") Long id);

    // --- 상세(의료진): staff만 fetch ---
    @Query("""
        select distinct h
        from AnimalHospital h
        left join fetch h.staff st
        where h.id = :id
    """)
    Optional<AnimalHospital> findWithStaffById(@Param("id") Long id);

    // --- 상세(이미지): images만 fetch ---
    @Query("""
        select distinct h
        from AnimalHospital h
        left join fetch h.images img
        where h.id = :id
    """)
    Optional<AnimalHospital> findWithImagesById(@Param("id") Long id);
}
