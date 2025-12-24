package growthon.withtail_be.domain.interest.repository;

import growthon.withtail_be.domain.animalfuneral.entity.AnimalFuneral;
import growthon.withtail_be.domain.animalhospital.entity.AnimalHospital;
import growthon.withtail_be.domain.interest.domain.Interest;
import growthon.withtail_be.domain.user.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InterestRepository extends JpaRepository<Interest, Long> {

    boolean existsByUserAndHospital(User user, AnimalHospital hospital);

    boolean existsByUserAndFuneral(User user, AnimalFuneral funeral);

    @EntityGraph(attributePaths = {"hospital", "funeral"})
    List<Interest> findByUserId(Long userId);
}
