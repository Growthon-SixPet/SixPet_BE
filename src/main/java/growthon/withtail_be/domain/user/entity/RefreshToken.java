package growthon.withtail_be.domain.user.entity;

import growthon.withtail_be.domain.model.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String token;
    private LocalDateTime expiration;

    @Builder
    public RefreshToken(User user, String token, LocalDateTime expiration) {
        this.user = user;
        this.token = token;
        this.expiration = expiration;
    }

    public void updateToken(String newToken, LocalDateTime newExpiration) {
        this.token = newToken;
        this.expiration = newExpiration;
    }

    public boolean isExpired() {
        return expiration.isBefore(LocalDateTime.now());
    }
}
