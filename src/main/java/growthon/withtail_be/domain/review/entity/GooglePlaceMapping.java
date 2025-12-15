package growthon.withtail_be.domain.review.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// 병원/장례 대상과 Google placeId를 매핑하는 엔티티
@Entity
@Table(
        name = "google_place_mappings",
        uniqueConstraints = @UniqueConstraint(name = "uk_target", columnNames = {"targetType", "targetId"})
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class GooglePlaceMapping {

    // PK
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 대상 타입 (병원 / 장례)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TargetType targetType;

    // 대상 ID (병원 ID 또는 장례)
    @Column(nullable = false)
    private Long targetId;

    // Google Places place_id
    @Column(nullable = false, length = 200)
    private String placeId;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // 생성 시 시간 설정
    @PrePersist
    void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    // 수정 시 시간 갱신
    @PreUpdate
    void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // placeId 변경
    public void changePlaceId(String placeId) {
        this.placeId = placeId;
    }
}
