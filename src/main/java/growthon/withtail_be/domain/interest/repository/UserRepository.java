package growthon.withtail_be.domain.interest.repository;

import growthon.withtail_be.domain.interest.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
