package growthon.withtail_be.domain.animalfuneral.repository;

import growthon.withtail_be.domain.animalfuneral.entity.AnimalFuneral;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface AnimalFuneralRepository extends JpaRepository<AnimalFuneral, Long>, JpaSpecificationExecutor<AnimalFuneral> {

    // 상세 공통 탭: 운영시간 + 비용까지 같이 필요 (편의시설은 별도 repo로 조회 권장)
    @EntityGraph(attributePaths = {"operatingHours"})
    Optional<AnimalFuneral> findDetailById(Long id);
}
