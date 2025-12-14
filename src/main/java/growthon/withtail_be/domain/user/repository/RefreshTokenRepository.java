package growthon.withtail_be.domain.user.repository;

import growthon.withtail_be.domain.user.entity.RefreshToken;
import growthon.withtail_be.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByUser(User user);

    Optional<RefreshToken> findByUserId(Long userId);

    void deleteByUserId(Long userId);

}
