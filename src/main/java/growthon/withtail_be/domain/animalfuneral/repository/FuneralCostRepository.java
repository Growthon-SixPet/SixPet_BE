package growthon.withtail_be.domain.animalfuneral.repository;

import growthon.withtail_be.domain.animalfuneral.entity.FuneralCost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FuneralCostRepository extends JpaRepository<FuneralCost, Long> {
    List<FuneralCost> findByFuneralId(Long funeralId);
}
