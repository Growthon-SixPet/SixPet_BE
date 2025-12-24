package growthon.withtail_be.domain.animalhospital.repository;

import growthon.withtail_be.domain.animalhospital.entity.AnimalHospital;
import growthon.withtail_be.domain.model.RegionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AnimalHospitalRepository extends JpaRepository<AnimalHospital, Long> {

    // =========================
    // 1) 검색 (필터 미포함)
    // =========================

    @EntityGraph(attributePaths = {"operatingHours"}) // List 1개 OK
    Page<AnimalHospital> findByRegion(RegionType region, Pageable pageable);

    @EntityGraph(attributePaths = {"operatingHours"})
    Page<AnimalHospital> findByNameContainingIgnoreCase(String name, Pageable pageable);

    @EntityGraph(attributePaths = {"operatingHours"})
    Page<AnimalHospital> findByRegionAndNameContainingIgnoreCase(RegionType region, String name, Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"operatingHours"})
    Page<AnimalHospital> findAll(Pageable pageable);

    // =========================
    // 2) 검색 (필터 포함)
    // =========================

    @EntityGraph(attributePaths = {"operatingHours"}) // List 1개 OK
    @Query(
            value = """
                select distinct h
                from AnimalHospital h
                left join h.specialties hs
                left join hs.specialty s
                left join h.animalTypes hat
                left join hat.animalType at
                where (:region is null or h.region = :region)
                  and (:keyword is null or trim(:keyword) = ''
                       or lower(h.name) like lower(concat('%', :keyword, '%')))
                  and (:open24h is null or h.open24h = :open24h)
                  and (:nightCare is null or h.nightCare = :nightCare)
                  and (:specialtyName is null or s.name = :specialtyName)
                  and (:animalTypeName is null or at.name = :animalTypeName)
                """,
            countQuery = """
                select count(distinct h.id)
                from AnimalHospital h
                left join h.specialties hs
                left join hs.specialty s
                left join h.animalTypes hat
                left join hat.animalType at
                where (:region is null or h.region = :region)
                  and (:keyword is null or trim(:keyword) = ''
                       or lower(h.name) like lower(concat('%', :keyword, '%')))
                  and (:open24h is null or h.open24h = :open24h)
                  and (:nightCare is null or h.nightCare = :nightCare)
                  and (:specialtyName is null or s.name = :specialtyName)
                  and (:animalTypeName is null or at.name = :animalTypeName)
                """
    )
    Page<AnimalHospital> searchWithFilters(
            @Param("region") RegionType region,
            @Param("keyword") String keyword,
            @Param("open24h") Boolean open24h,
            @Param("nightCare") Boolean nightCare,
            @Param("specialtyName") String specialtyName,
            @Param("animalTypeName") String animalTypeName,
            Pageable pageable
    );

    // =========================
    // 3) 상세 조회 (상단 공통)
    // - List 컬렉션을 1개만 fetch: operatingHours만
    // - specialties / animalTypes는 LAZY(배치로 최적화 권장)
    // =========================

    @EntityGraph(attributePaths = {
            "operatingHours"
            // "specialties", "specialties.specialty",
            // "animalTypes", "animalTypes.animalType"
            // -> 여기서 같이 fetch하면 MultipleBagFetchException 위험
    })
    @Query("select h from AnimalHospital h where h.id = :hospitalId")
    Optional<AnimalHospital> findDetailById(@Param("hospitalId") Long hospitalId);

    // =========================
    // 4) 상세 조회 (의료진)
    // - staffList(List) 하나만 fetch
    // - staffSpecialties(List)는 LAZY(배치로 최적화 권장)
    // =========================

    @EntityGraph(attributePaths = {
            "staffList"
            // "staffList.staffSpecialties",
            // "staffList.staffSpecialties.specialty"
            // -> staffList(List) + staffSpecialties(List) 동시 fetch로 터질 수 있음
    })
    @Query("select h from AnimalHospital h where h.id = :hospitalId")
    Optional<AnimalHospital> findStaffDetailById(@Param("hospitalId") Long hospitalId);

    // =========================
    // 5) 상세 조회 (병원 소식)
    // - newsList(List) 하나만 fetch라 OK
    // =========================

    @EntityGraph(attributePaths = {"newsList"})
    @Query("select h from AnimalHospital h where h.id = :hospitalId")
    Optional<AnimalHospital> findNewsDetailById(@Param("hospitalId") Long hospitalId);
}
