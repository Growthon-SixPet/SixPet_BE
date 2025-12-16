package growthon.withtail_be.domain.interest.repository;

import growthon.withtail_be.domain.interest.domain.Funeral;
import growthon.withtail_be.domain.interest.domain.Hospital;
import growthon.withtail_be.domain.interest.domain.Interest;
import growthon.withtail_be.domain.interest.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InterestRepository extends JpaRepository<Interest, Long> {

    boolean existsByUserAndHospital(User user, Hospital hospital);

    boolean existsByUserAndFuneral(User user, Funeral funeral);

    List<Interest> findByUserId(Long userId);
}
