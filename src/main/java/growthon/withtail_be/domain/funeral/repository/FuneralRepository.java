package growthon.withtail_be.domain.funeral.repository;

import growthon.withtail_be.domain.funeral.entity.Funeral;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FuneralRepository extends JpaRepository<Funeral, Long> {

    @Query("""
        select f
        from Funeral f
        where (:keyword is null or :keyword = '' 
               or lower(f.name) like lower(concat('%', :keyword, '%')))
          and (:sido is null or :sido = '' or f.sido = :sido)
          and (:sigungu is null or :sigungu = '' or f.sigungu = :sigungu)
    """)
    Page<Funeral> search(
            @Param("keyword") String keyword,
            @Param("sido") String sido,
            @Param("sigungu") String sigungu,
            Pageable pageable
    );
}
