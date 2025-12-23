package growthon.withtail_be.domain.animalfuneral.repository;

import growthon.withtail_be.domain.animalfuneral.entity.AnimalFuneral;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AnimalFuneralRepository extends JpaRepository<AnimalFuneral, Long>, JpaSpecificationExecutor<AnimalFuneral> {

    // 기본 상세: operatingHours만 fetch (amenities랑 같이 가져오면 MultipleBagFetchException 가능)
    @EntityGraph(attributePaths = {"operatingHours"})
    Optional<AnimalFuneral> findBasicById(Long id);

    // 상세(브랜드 소개)
    @EntityGraph(attributePaths = {"brandSections"})
    Optional<AnimalFuneral> findBrandById(Long id);

    // 상세(장례 절차)
    @EntityGraph(attributePaths = {"procedures"})
    Optional<AnimalFuneral> findProceduresById(Long id);

    // 상세(장례 비용)
    @EntityGraph(attributePaths = {"costs"})
    Optional<AnimalFuneral> findCostsById(Long id);
}
