package growthon.withtail_be.domain.interest.repository;

import growthon.withtail_be.domain.interest.domain.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HospitalRepository extends JpaRepository<Hospital, Long> {
}
