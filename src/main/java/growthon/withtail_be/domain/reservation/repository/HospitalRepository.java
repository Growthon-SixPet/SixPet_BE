package growthon.withtail_be.domain.reservation.repository;

import growthon.withtail_be.domain.reservation.domain.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HospitalRepository extends JpaRepository<Hospital, Long> {
}
