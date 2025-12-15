package growthon.withtail_be.domain.hospital.repository;

import growthon.withtail_be.domain.hospital.entity.Hospital;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

// 병원 검색 쿼리 담당 (조건 + 페이징)
public interface HospitalRepository extends JpaRepository<Hospital, Long> {

    // keyword/sido/sigungu가 없으면 해당 조건은 무시
    @Query("""
        select h
        from Hospital h
        where (:keyword is null or :keyword = '' 
               or lower(h.name) like lower(concat('%', :keyword, '%')))
          and (:sido is null or :sido = '' or h.sido = :sido)
          and (:sigungu is null or :sigungu = '' or h.sigungu = :sigungu)
    """)
    Page<Hospital> search(
            @Param("keyword") String keyword,
            @Param("sido") String sido,
            @Param("sigungu") String sigungu,
            Pageable pageable
    );
}
