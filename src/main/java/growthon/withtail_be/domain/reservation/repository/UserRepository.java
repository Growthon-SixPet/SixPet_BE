package growthon.withtail_be.domain.reservation.repository;

import growthon.withtail_be.domain.reservation.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
