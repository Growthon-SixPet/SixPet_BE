package growthon.withtail_be.domain.animalfuneral.repository;

import growthon.withtail_be.domain.animalfuneral.entity.FuneralAmenity;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FuneralAmenityRepository extends JpaRepository<FuneralAmenity, Long> {

    @EntityGraph(attributePaths = {"amenity"})
    List<FuneralAmenity> findByFuneralId(Long funeralId);
}
