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

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// 병원/장례 공통 자체 후기 엔티티
@Entity
@Table(name = "reviews")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Review {
    
    // 후기 PK
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 후기 작성자 ID
    @Column(nullable = false)
    private Long userId;

    // 후기 대상 타입 (HOSPITAL / FUNERAL)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TargetType targetType;

    // 병원 ID 또는 장례 ID
    @Column(nullable = false)
    private Long targetId;

    // 평점 (1~5)
    @Column(nullable = false)
    private Integer rating;
    
    // 후기 내용
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    // 생성 시간
    @Column(nullable = false)
    private LocalDateTime createdAt;

    // 수정 시간
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // 최조 저장 시 시간 자동 설정
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
}

/*
    target Type + targetId 조합으로 병원/장례 구분
 */
